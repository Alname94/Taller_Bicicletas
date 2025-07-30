package com.alejomendez.tallerbicicletas.models.repositories.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.alejomendez.tallerbicicletas.models.entities.Repuesto;

public interface I_RepuestoRepository {
    void create(Repuesto repuesto) throws SQLException;
    Repuesto findByCodigo(int codigo) throws SQLException;
    List<Repuesto> findAll() throws SQLException;
    int update(Repuesto repuesto) throws SQLException;
    int delete(int codigo) throws SQLException;
    List<Repuesto> findByPresupuesto(int numero) throws SQLException;
    List<Repuesto> findByProducto(String producto) throws SQLException;
    List<Repuesto> findByMarca(String marca) throws SQLException;
}
