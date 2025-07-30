package com.alejomendez.tallerbicicletas.models.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.alejomendez.tallerbicicletas.models.entities.Cliente;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_ClienteRepository;

@Repository
public class ClienteRepository implements I_ClienteRepository{
    private final DataSource DATASOURCE;

    private static final String SQL_CREATE =
        "INSERT INTO clientes (nombre, apellido, dni, telefono, email) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SQL_FIND_BY_ID =
        "SELECT * FROM clientes WHERE id=?";
        
    private static final String SQL_FIND_ALL =
        "SELECT * FROM clientes";

    private static final String SQL_UPDATE =
        "UPDATE clientes SET nombre=?, apellido=?, dni=?, telefono=?, email=? WHERE id=?";
    
    private static final String SQL_DELETE =
        "DELETE FROM clientes WHERE id=?";
        
    private static final String SQL_FIND_BY_NOMBRE_LIKE =
        "SELECT * from clientes WHERE nombre like ?";
        
    private static final String SQL_FIND_BY_APELLIDO_LIKE =
        "SELECT * FROM clientes WHERE apellido like ?";

    private static final String SQL_FIND_BY_PRESUPUESTO =
        "SELECT c.id, c.nombre, c.apellido, c.dni, c.telefono, c.email FROM clientes c JOIN presupuestos p on p.cliente_id = c.id WHERE p.numero=?";    

        
    public ClienteRepository(DataSource DATASOURCE){
        this.DATASOURCE=DATASOURCE;
    }    

    @Override
    public void create(Cliente cliente) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getDni());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if(keys.next()){
                    cliente.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public Cliente findById(int id) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return mapRow(rs);
                }                
            }
        }
        return null;
    }

    @Override
    public List<Cliente> findAll() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    @Override
    public int update(Cliente cliente) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getDni());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());
            ps.setInt(6, cliente.getId());
            int filasAfectadas=ps.executeUpdate();
            return filasAfectadas;
        }
    }

    @Override
    public int delete(int id) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas;
        }
    }

    @Override
    public List<Cliente> findByNombre(String nombre) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_NOMBRE_LIKE)) {
            ps.setString(1, "%"+nombre+"%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Cliente> findByApellido(String apellido) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_APELLIDO_LIKE)) {
            ps.setString(1, "%"+apellido+"%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public Cliente findByPresupuesto(int numero) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PRESUPUESTO)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return mapRow(rs);
                }                
            }
        }
        return null;
    }
    
    private Cliente mapRow(ResultSet rs) throws SQLException{
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellido(rs.getString("apellido"));
        cliente.setDni(rs.getString("dni"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setEmail(rs.getString("email"));
        return cliente;
    }

}
