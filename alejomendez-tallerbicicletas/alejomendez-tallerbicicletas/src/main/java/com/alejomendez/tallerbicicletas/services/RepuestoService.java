package com.alejomendez.tallerbicicletas.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alejomendez.tallerbicicletas.models.entities.Repuesto;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_RepuestoRepository;

@Service
public class RepuestoService {
    private final I_RepuestoRepository repuestoRepository;

    public RepuestoService(I_RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }

    /**
     * Obtiene una lista de todos los repuestos
     * @return
     * @throws SQLException
     */
    public List<Repuesto> obtenerTodosLosRepuestos() throws SQLException {
        return repuestoRepository.findAll();
    }

    /**
     * Permite guardar la actualización de un repuesto
     * @param repuesto
     * @return
     * @throws SQLException
     */
    public Repuesto guardarRepuesto(Repuesto repuesto) throws SQLException {
        repuestoRepository.update(repuesto);
        return repuesto;
    }

    /**
     * Crea y guarda un nuevo repuesto
     * @param repuesto
     * @return
     * @throws SQLException
     */
    public Repuesto crearRepuesto(Repuesto repuesto) throws SQLException {
        repuestoRepository.create(repuesto);
        return repuesto;
    }

    /**
     * Busca un repuesto por su codigo
     * @param codigo
     * @return
     * @throws SQLException
     */
    public Repuesto buscarRepuestoPorCodigo(int codigo) throws SQLException {
        return repuestoRepository.findByCodigo(codigo);
    }

    /**
     * Elimina un repuesto por codigo
     * @param codigo
     * @return
     * @throws SQLException
     */
    public int eliminarRepuesto(int codigo) throws SQLException {
        return repuestoRepository.delete(codigo);
    }

    /**
     * Lista los repuestos que se encuentren en el presupuesto pasado como parametro
     * @param numero
     * @return
     * @throws SQLException
     */
    public List<Repuesto> buscarRepuestoPorPresupuesto(int numero) throws SQLException {
        return repuestoRepository.findByPresupuesto(numero); 
    }

    /**
     * Lista los repuestos que coincidan con la búsqueda por producto
     * @param producto
     * @return
     * @throws SQLException
     */
    public List<Repuesto> buscarRepuestoPorProducto(String producto) throws SQLException {
        return repuestoRepository.findByProducto(producto); 
    }

    /**
     * Lista los repuestos que coincidan con la búsqueda por marca
     * @param marca
     * @return
     * @throws SQLException
     */
    public List<Repuesto> buscarRepuestoPorMarca(String marca) throws SQLException {
        return repuestoRepository.findByMarca(marca); 
    }
}
