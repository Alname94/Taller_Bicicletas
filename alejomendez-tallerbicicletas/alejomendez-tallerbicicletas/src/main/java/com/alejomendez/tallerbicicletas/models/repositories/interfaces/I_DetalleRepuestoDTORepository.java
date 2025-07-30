package com.alejomendez.tallerbicicletas.models.repositories.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.alejomendez.tallerbicicletas.models.dtos.DetalleRepuestoDTO;

public interface I_DetalleRepuestoDTORepository {
    List<DetalleRepuestoDTO> findByPresupuestoDTO(int presupuestoNumero) throws SQLException;
}
