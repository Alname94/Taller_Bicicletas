package com.alejomendez.tallerbicicletas.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alejomendez.tallerbicicletas.models.entities.Cliente;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_ClienteRepository;

@Service
public class ClienteService {
    private final I_ClienteRepository clienteRepository;

    public ClienteService(I_ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Obtiene una lista de todos los clientes
     * @return
     * @throws SQLException
     */
    public List<Cliente> obtenerTodosLosClientes() throws SQLException{
        return clienteRepository.findAll();
    }

    /**
     * Guarda un cliente o lo actualiza si el cliente ya existe
     * @param cliente
     * @return
     * @throws SQLException
     */
    public Cliente guardarCliente(Cliente cliente) throws SQLException{
        if(cliente.getId()!=0){
            clienteRepository.update(cliente);
        } else {
            clienteRepository.create(cliente);
        }
        return cliente;
    }

    /**
     * Buscar un cliente por Id
     * @param id
     * @return
     * @throws SQLException
     */
    public Cliente buscarClientePorId(int id) throws SQLException {
        return clienteRepository.findById(id);
    }

    /**
     * Elimina el cliente por Id
     * @param id
     * @return
     * @throws SQLException
     */
    public int eliminarCliente(int id) throws SQLException {
        return clienteRepository.delete(id);
    }

    /**
     * Busca un cliente por numero de presupuesto
     * @param numero
     * @return
     * @throws SQLException
     */
    public List<Cliente> buscarClientePorNombre(String nombre) throws SQLException {
        return clienteRepository.findByNombre(nombre);
    }

    /**
     * Busca un cliente por Id de bicicleta
     * @param id
     * @return
     * @throws SQLException
     */
    public List<Cliente> buscarClientePorApellido (String apellido) throws SQLException {
        return clienteRepository.findByApellido(apellido);
    }

    public Cliente buscarClientePorPresupuesto (int numero) throws SQLException {
        return clienteRepository.findByPresupuesto(numero);
    }
}
