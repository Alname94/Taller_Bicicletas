package com.alejomendez.tallerbicicletas.models.repositories.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.alejomendez.tallerbicicletas.models.entities.Cliente;

public interface I_ClienteRepository {
    void create(Cliente cliente) throws SQLException;
    Cliente findById(int id) throws SQLException;
    List<Cliente> findAll() throws SQLException;
    int update(Cliente cliente) throws SQLException;
    int delete(int id) throws SQLException;
    List<Cliente> findByNombre(String nombre) throws SQLException;
    List<Cliente> findByApellido(String apellido) throws SQLException;
    Cliente findByPresupuesto(int numero) throws SQLException;
}
