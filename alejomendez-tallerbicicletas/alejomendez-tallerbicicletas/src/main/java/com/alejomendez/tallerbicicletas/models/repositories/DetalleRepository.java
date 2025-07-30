package com.alejomendez.tallerbicicletas.models.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.alejomendez.tallerbicicletas.models.entities.Detalle;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_DetalleRepository;

@Repository
public class DetalleRepository implements I_DetalleRepository{
    private final DataSource DATASOURCE;

    private static final String SQL_CREATE =
        "INSERT INTO detalles (presupuesto_numero, repuesto_codigo, cantidad_agregada) values (?,?,?)";

    private static final String SQL_FIND_ALL =
        "SELECT * FROM detalles";
        
    private static final String SQL_DELETE =
        "DELETE FROM detalles WHERE presupuesto_numero=? and repuesto_codigo=?";

    private static final String SQL_FIND_BY_PRESUPUESTO_REPUESTO =
        "SELECT * FROM detalles WHERE presupuesto_numero=? and repuesto_codigo=?";     
        
    private static final String SQL_FIND_BY_PRESUPUESTO =
        "SELECT * FROM detalles WHERE presupuesto_numero=?";
        
    private static final String SQL_FIND_BY_REPUESTO =
        "SELECT * FROM detalles WHERE repuesto_codigo=?";    

    
    public DetalleRepository(DataSource DATASOURCE) {
        this.DATASOURCE = DATASOURCE;
    }


    @Override
    public void create(Detalle detalle) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_CREATE)) {
            ps.setInt(1, detalle.getPresupuestoNumero());
            ps.setInt(2, detalle.getRepuestoCodigo());
            ps.setInt(3, detalle.getCantidadAgregada());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Detalle> findAll() throws SQLException {
        List<Detalle> lista = new ArrayList<>();
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
    public int delete(int presupuestoNumero, int repuestoCodigo) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, presupuestoNumero);
            ps.setInt(2, repuestoCodigo);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas;
        }    
    }

    @Override
    public Detalle findByPresupuestoYRepuesto(int presupuestoNumero, int repuestoCodigo) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PRESUPUESTO_REPUESTO)) {
            ps.setInt(1, presupuestoNumero);
            ps.setInt(2, repuestoCodigo);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return mapRow(rs);
                }                
            }
        }
        return null;
    }

    @Override
    public List<Detalle> findByPresupuesto(int presupuestoNumero) throws SQLException {
        List<Detalle> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PRESUPUESTO)) {
            ps.setInt(1, presupuestoNumero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Detalle> findByRepuesto(int repuestoCodigo) throws SQLException {
        List<Detalle> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_REPUESTO)) {
            ps.setInt(1, repuestoCodigo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }
    
    private Detalle mapRow(ResultSet rs) throws SQLException {
        Detalle detalle = new Detalle();
        detalle.setPresupuestoNumero(rs.getInt("presupuesto_numero"));
        detalle.setRepuestoCodigo(rs.getInt("repuesto_codigo"));
        detalle.setCantidadAgregada(rs.getInt("cantidad_agregada"));
        return detalle;
    }

}
