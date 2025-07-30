package com.alejomendez.tallerbicicletas.models.repositories.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.alejomendez.tallerbicicletas.models.entities.Presupuesto;

public interface I_PresupuestoRepository {
    void create(Presupuesto presupuesto) throws SQLException;
    Presupuesto findByNumero(int numero) throws SQLException;
    List<Presupuesto> findAll() throws SQLException;
    int update(Presupuesto presupuesto) throws SQLException;
    int delete(int numero) throws SQLException;
    List<Presupuesto> findByBicicleta(String bicicletaMarca) throws SQLException;
    List<Presupuesto> findByCliente(String clienteNombre) throws SQLException;
}
