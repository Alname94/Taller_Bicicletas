package com.alejomendez.tallerbicicletas.models.repositories.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.alejomendez.tallerbicicletas.models.entities.Detalle;

public interface I_DetalleRepository {
    void create(Detalle detalle) throws SQLException;
    List<Detalle> findAll() throws SQLException;
    int delete(int presupuestoNumero, int repuestoCodigo) throws SQLException;
    Detalle findByPresupuestoYRepuesto(int presupuestoNumero, int repuestoCodigo) throws SQLException;
    List<Detalle> findByPresupuesto(int presupuestoNumero) throws SQLException;
    List<Detalle> findByRepuesto(int repuestoCodigo) throws SQLException;
}
