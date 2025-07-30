package com.alejomendez.tallerbicicletas.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alejomendez.tallerbicicletas.models.entities.Detalle;
import com.alejomendez.tallerbicicletas.models.entities.Presupuesto;
import com.alejomendez.tallerbicicletas.models.entities.Repuesto;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_DetalleRepository;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_PresupuestoRepository;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_RepuestoRepository;

@Service
public class PresupuestoService {
    private final I_PresupuestoRepository presupuestoRepository;
    private final I_DetalleRepository detalleRepository;
    private final I_RepuestoRepository repuestoRepository;    

    public PresupuestoService(I_PresupuestoRepository presupuestoRepository, I_DetalleRepository detalleRepository,
            I_RepuestoRepository repuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
        this.detalleRepository = detalleRepository;
        this.repuestoRepository = repuestoRepository;
    }

    /**
     * Obtiene una lista de todos los presupuestos
     * @return
     * @throws SQLException
     */
    public List<Presupuesto> obtenerTodosLosPresupuestos() throws SQLException {
        return presupuestoRepository.findAll();
    }

    /**
     * Guarda un presupuesto o lo actualiza si el presupuesto ya existe
     * @param presupuesto
     * @return
     * @throws SQLException
     */
    public Presupuesto guardarPresupuesto(Presupuesto presupuesto) throws SQLException {
        if(presupuesto.getNumero()!=0){
            presupuestoRepository.update(presupuesto);
        }else{
            presupuestoRepository.create(presupuesto);
        }
        return presupuesto;
    }
    
    /**
     * Busca un presupuesto por su numero
     * @param numero
     * @return
     * @throws SQLException
     */
    public Presupuesto buscaPresupuestoPorNumero(int numero) throws SQLException {
        return presupuestoRepository.findByNumero(numero);
    }

    /**
     * Permite eliminar un presupuesto por número, primero elimina los repuestos asociados a ese presupuesto.
     * @param numero
     * @return
     * @throws SQLException
     */
    public int eliminarPresupuesto(int numero) throws SQLException {
        List<Repuesto> repuestos = repuestoRepository.findByPresupuesto(numero);
        for (Repuesto repuesto : repuestos) {
            eliminarRepuesto(numero, repuesto.getCodigo());
        }
        return presupuestoRepository.delete(numero);
    }

    /**
     * Lista los presupuestos pertenecientes a una bicicleta
     * @param bicicletaId
     * @return
     * @throws SQLException
     */
    public List<Presupuesto> buscarPresupuestoPorBicicleta(String bicicletaMarca) throws SQLException {
        return presupuestoRepository.findByBicicleta(bicicletaMarca);
    }

    /**
     * Lista los presupuestos pertenecientes a un cliente
     * @param clienteId
     * @return
     * @throws SQLException
     */
    public List<Presupuesto> buscarPresupuestoPorCliente(String clienteNombre) throws SQLException {
        return presupuestoRepository.findByCliente(clienteNombre);
    }


    /**
     * Permite agregar un repuesto al presupuesto, verificando primero si hay stock disponible.
     * Y también actualiza el valor total del presupuesto y el stock del repuesto agregado.
     * @param presupuestoNumero
     * @param repuestoCodigo
     * @param cantidadAgregada
     * @throws SQLException
     */
    public void agregarRepuesto(int presupuestoNumero, int repuestoCodigo, int cantidadAgregada) throws SQLException {
        Presupuesto presupuesto = presupuestoRepository.findByNumero(presupuestoNumero);
        Repuesto repuesto = repuestoRepository.findByCodigo(repuestoCodigo);
        if(repuesto.getStock()>0){
            Detalle detalle = new Detalle(presupuestoNumero, repuestoCodigo, cantidadAgregada);
            detalleRepository.create(detalle);
            repuesto.setStock(repuesto.getStock() - detalle.getCantidadAgregada());
            repuestoRepository.update(repuesto);
            presupuesto.setValorTotal(presupuesto.getValorTotal() + (repuesto.getPrecioVenta()*detalle.getCantidadAgregada()));
            presupuestoRepository.update(presupuesto);
        }else{
            throw new SQLException("No hay stock disponible para el repuesto " + repuestoCodigo);
        }        
    }

    /**
     * Permite eliminar un repuesto agregado previamente en un presupuesto.
     * Actualiza el valor total del presupuesto y el stock del repuesto quitado del presupuesto.
     * @param presupuestoNumero
     * @param repuestoCodigo
     * @throws SQLException
     */
    public void eliminarRepuesto(int presupuestoNumero, int repuestoCodigo) throws SQLException {
        Presupuesto presupuesto = presupuestoRepository.findByNumero(presupuestoNumero);
        Repuesto repuesto = repuestoRepository.findByCodigo(repuestoCodigo);
        Detalle detalle = detalleRepository.findByPresupuestoYRepuesto(presupuestoNumero, repuestoCodigo);
        detalleRepository.delete(presupuestoNumero, repuestoCodigo);
        repuesto.setStock(repuesto.getStock() + detalle.getCantidadAgregada());
        repuestoRepository.update(repuesto);
        presupuesto.setValorTotal(presupuesto.getValorTotal() - (repuesto.getPrecioVenta()*detalle.getCantidadAgregada()));        
        presupuestoRepository.update(presupuesto);
    }
}
