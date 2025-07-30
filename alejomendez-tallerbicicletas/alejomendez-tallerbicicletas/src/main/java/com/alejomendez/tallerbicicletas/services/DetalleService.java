package com.alejomendez.tallerbicicletas.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alejomendez.tallerbicicletas.models.entities.Detalle;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_DetalleRepository;

@Service
public class DetalleService {
    private final I_DetalleRepository detalleRepository;

    public DetalleService(I_DetalleRepository detalleRepository) {
        this.detalleRepository = detalleRepository;
    }

    /**
     * Obtiene una lista de todos los detalles
     * @return
     * @throws SQLException
     */
    public List<Detalle> obtenerTodosLosDetalles() throws SQLException {
        return detalleRepository.findAll();
    }

    /**
     * Se utiliza para agregar un repuesto dentro de un presupuesto, el detalle creado contiene el número de presupuesto y el código del repuesto
     * @param detalle
     * @return
     * @throws SQLException
     */
    public Detalle guardarDetalle(Detalle detalle) throws SQLException {
        detalleRepository.create(detalle);
        return detalle;
    }

    /**
     * Se utiliza para quitar un repuesto que fue agregado previamente a un presupuesto.
     * @param presupuestoNumero
     * @param repuestoCodigo
     * @return
     * @throws SQLException
     */
    public int eliminarDetalle(int presupuestoNumero, int repuestoCodigo) throws SQLException {
        return detalleRepository.delete(presupuestoNumero, repuestoCodigo);
    }


    /**
     * Obtiene un detalle por numero de presupuesto y codigo de repuesto
     * @param presupuestoNumero
     * @param repuestoCodigo
     * @return
     * @throws SQLException
     */
    public Detalle buscarDetallePorPresupuestoYRepuesto(int presupuestoNumero, int repuestoCodigo) throws SQLException {
        return detalleRepository.findByPresupuestoYRepuesto(presupuestoNumero, repuestoCodigo);
    }

    /**
     * Busca los detalles por numero de presupuesto
     * @param numero
     * @return
     * @throws SQLException
     */
    public List<Detalle> buscarDetallePorPresupuesto(int presupuestoNumero) throws SQLException {
        return detalleRepository.findByPresupuesto(presupuestoNumero);
    }

    /**
     * Busca los detalles por numero de repuesto
     * @param repuestoCodigo
     * @return
     * @throws SQLException
     */
    public List<Detalle> buscarDetallesPorRepuesto(int repuestoCodigo) throws SQLException {
        return detalleRepository.findByRepuesto(repuestoCodigo);
    }
}
