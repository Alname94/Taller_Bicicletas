package com.alejomendez.tallerbicicletas.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alejomendez.tallerbicicletas.models.dtos.DetalleRepuestoDTO;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_DetalleRepuestoDTORepository;

@Service
public class DetalleRepuestoDTOService {
    private final I_DetalleRepuestoDTORepository detalleRespuestoDTORepository;

    public DetalleRepuestoDTOService(I_DetalleRepuestoDTORepository detalleRespuestoDTORepository) {
        this.detalleRespuestoDTORepository = detalleRespuestoDTORepository;
    }

    /**
     * Devuelve una lista con los datos codigo, producto, marca, color de cada repuesto y la cantidad agregada del detalle en un presupuesto
     * @param presupuestoNumero
     * @return
     * @throws SQLException
     */
    public List<DetalleRepuestoDTO> obtenerRepuestosAgregados(int presupuestoNumero) throws SQLException {
        return detalleRespuestoDTORepository.findByPresupuestoDTO(presupuestoNumero);
    }
}
