package com.alejomendez.tallerbicicletas.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alejomendez.tallerbicicletas.models.entities.Bicicleta;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_BicicletaRepository;

@Service
public class BicicletaService {
    private final I_BicicletaRepository bicicletaRepository;

    public BicicletaService(I_BicicletaRepository bicicletaRepository) {
        this.bicicletaRepository = bicicletaRepository;
    }

    /**
     * Obtiene una lista de todas las bicicletas
     * @return
     * @throws SQLException
     */
    public List<Bicicleta> obtenerTodasLasBicicletas() throws SQLException {
        return bicicletaRepository.findAll();
    }

    /**
     * Guarda una bicicleta o la actualiza si la bicicleta ya existe
     * @param bicicleta
     * @return
     * @throws SQLException
     */
    public Bicicleta guardarBicicleta(Bicicleta bicicleta) throws SQLException {
        if(bicicleta.getId()!=0){
            bicicletaRepository.update(bicicleta);
        }else{
            bicicletaRepository.create(bicicleta);
        }
        return bicicleta;
    }

    /**
     * Busca una bicicleta por su Id
     * @param id
     * @return
     * @throws SQLException
     */
    public Bicicleta buscarBicicletaPorId(int id) throws SQLException {
        return bicicletaRepository.findById(id);
    }

    /**
     * Elimina una bicicleta por Id
     * @param id
     * @return
     * @throws SQLException
     */
    public int eliminarBicicleta(int id) throws SQLException {
        return bicicletaRepository.delete(id);
    }

    /**
     * Devuelve una lista de bicicletas por id del cliente
     * @param id_cliente
     * @return
     * @throws SQLException
     */
    public List<Bicicleta> buscarBicicletaPorCliente(int idCliente) throws SQLException {
        return bicicletaRepository.findByCliente(idCliente);
    }

    public List<Bicicleta> buscarBicicletaPorMarca(String marca) throws SQLException {
        return bicicletaRepository.findByMarca(marca);
    }

    public List<Bicicleta> buscarBicicletaPorModelo(String modelo) throws SQLException {
        return bicicletaRepository.findByModelo(modelo);
    }

    public Bicicleta buscarBicicletaPorPresupuesto(int numero) throws SQLException {
        return bicicletaRepository.findByPresupuesto(numero);
    }
}
