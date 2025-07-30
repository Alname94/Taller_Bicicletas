package com.alejomendez.tallerbicicletas.models.repositories.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.alejomendez.tallerbicicletas.models.entities.Bicicleta;

public interface I_BicicletaRepository {
    void create(Bicicleta bicicleta) throws SQLException;
    Bicicleta findById(int id) throws SQLException;
    List<Bicicleta> findAll() throws SQLException;
    int update(Bicicleta bicicleta) throws SQLException;
    int delete(int id) throws SQLException;
    List<Bicicleta> findByCliente(int idCliente) throws SQLException;
    List<Bicicleta> findByMarca(String marca) throws SQLException;
    List<Bicicleta> findByModelo(String modelo) throws SQLException;
    Bicicleta findByPresupuesto(int numero) throws SQLException;
}
