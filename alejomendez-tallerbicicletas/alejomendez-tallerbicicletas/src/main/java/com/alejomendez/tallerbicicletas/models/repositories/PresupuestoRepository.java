package com.alejomendez.tallerbicicletas.models.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.alejomendez.tallerbicicletas.models.entities.Presupuesto;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_PresupuestoRepository;

@Repository
public class PresupuestoRepository implements I_PresupuestoRepository{
    private final DataSource DATASOURCE;

    private static final String SQL_CREATE =
        "INSERT INTO presupuestos (fecha, cliente_id, bicicleta_id, valor_total, detalle) VALUES (?,?,?,?,?)";

    private static final String SQL_FIND_BY_NUMERO =
        "SELECT * FROM presupuestos where numero=?";
        
    private static final String SQL_FIND_ALL =
        "SELECT * FROM presupuestos";
        
    private static final String SQL_UPDATE =
        "UPDATE presupuestos SET fecha=?, cliente_id=?, bicicleta_id=?, valor_total=?, detalle=? WHERE numero=?";
        
    private static final String SQL_DELETE =
        "DELETE FROM presupuestos WHERE numero=?";

    private static final String SQL_FIND_BY_BICICLETA_MARCA_LIKE =
        "SELECT p.numero, p.fecha, p.cliente_id, p.bicicleta_id, p.valor_total, p.detalle FROM presupuestos p JOIN bicicletas b on p.bicicleta_id = b.id WHERE b.marca like ?";    
        
    private static final String SQL_FIND_BY_CLIENTE_NOMBRE_LIKE =
        "SELECT p.numero, p.fecha, p.cliente_id, p.bicicleta_id, p.valor_total, p.detalle FROM presupuestos p JOIN clientes c on p.cliente_id = c.id WHERE c.nombre like ?";
        

    public PresupuestoRepository(DataSource DATASOURCE){
        this.DATASOURCE=DATASOURCE;
    }


    @Override
    public void create(Presupuesto presupuesto) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, presupuesto.getFecha());
            ps.setInt(2, presupuesto.getClienteId());
            ps.setInt(3, presupuesto.getBicicletaId());
            ps.setDouble(4, presupuesto.getValorTotal());
            ps.setString(5, presupuesto.getDetalle());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if(keys.next()){
                    presupuesto.setNumero(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public Presupuesto findByNumero(int numero) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_NUMERO)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return mapRow(rs);
                }                
            }
        }
        return null;
    }    

    @Override
    public List<Presupuesto> findAll() throws SQLException {
        List<Presupuesto> lista = new ArrayList<>();
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
    public int update(Presupuesto presupuesto) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setObject(1, presupuesto.getFecha());
            ps.setInt(2, presupuesto.getClienteId());
            ps.setInt(3, presupuesto.getBicicletaId());
            ps.setDouble(4, presupuesto.getValorTotal());
            ps.setString(5, presupuesto.getDetalle());
            ps.setInt(6, presupuesto.getNumero());
            int filasAfectadas=ps.executeUpdate();
            return filasAfectadas;
        }
    }

    @Override
    public int delete(int numero) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, numero);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas;
        }
    }

    @Override
    public List<Presupuesto> findByBicicleta(String bicicletaMarca) throws SQLException {
        List<Presupuesto> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_BICICLETA_MARCA_LIKE)) {
            ps.setString(1, "%"+bicicletaMarca+"%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Presupuesto> findByCliente(String clienteNombre) throws SQLException {
        List<Presupuesto> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_CLIENTE_NOMBRE_LIKE)) {
            ps.setString(1, "%"+clienteNombre+"%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }        

    private Presupuesto mapRow(ResultSet rs) throws SQLException{
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setNumero(rs.getInt("numero"));
        presupuesto.setFecha(rs.getObject("fecha", LocalDate.class));
        presupuesto.setClienteId(rs.getInt("cliente_id"));
        presupuesto.setBicicletaId(rs.getInt("bicicleta_id"));
        presupuesto.setValorTotal(rs.getDouble("valor_total"));
        presupuesto.setDetalle(rs.getString("detalle"));
        return presupuesto;
    }

}
