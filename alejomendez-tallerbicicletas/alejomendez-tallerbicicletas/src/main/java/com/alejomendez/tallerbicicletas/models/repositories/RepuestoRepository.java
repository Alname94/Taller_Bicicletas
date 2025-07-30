package com.alejomendez.tallerbicicletas.models.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.alejomendez.tallerbicicletas.models.entities.Repuesto;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_RepuestoRepository;

@Repository
public class RepuestoRepository implements I_RepuestoRepository {
    private final DataSource DATASOURCE;

    private static final String SQL_CREATE =
        "INSERT into repuestos(codigo, producto, marca, color, precio_venta, precio_costo, stock) VALUES (?,?,?,?,?,?,?)";

    private static final String SQL_FIND_BY_CODIGO =
        "SELECT * FROM repuestos where codigo=?";
        
    private static final String SQL_FIND_ALL =
        "SELECT * FROM repuestos";
        
    private static final String SQL_UPDATE =
        "UPDATE repuestos SET producto=?, marca=?, color=?, precio_venta=?, precio_costo=?, stock=? WHERE codigo=?";
        
    private static final String SQL_DELETE =
        "DELETE FROM repuestos WHERE codigo=?";

    private static final String SQL_FIND_BY_PRODUCTO_LIKE =
        "SELECT * FROM repuestos WHERE producto like ?";     

    private static final String SQL_FIND_BY_MARCA_LIKE =
        "SELECT * FROM repuestos WHERE marca like ?";   

    private static final String SQL_FIND_BY_PRESUPUESTO =
        "SELECT r.codigo, r.producto, r.marca, r.color, r.precio_venta, r.precio_costo, r.stock from repuestos r JOIN detalles d on d.repuesto_codigo = r.codigo JOIN presupuestos p on p.numero = d.presupuesto_numero WHERE p.numero=?";

    public RepuestoRepository(DataSource DATASOURCE){
        this.DATASOURCE=DATASOURCE;
    }


    @Override
    public void create(Repuesto repuesto) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_CREATE)) {
            ps.setInt(1, repuesto.getCodigo());        
            ps.setString(2, repuesto.getProducto());
            ps.setString(3, repuesto.getMarca());
            ps.setString(4, repuesto.getColor());
            ps.setDouble(5, repuesto.getPrecioVenta());
            ps.setDouble(6, repuesto.getPrecioCosto());
            ps.setInt(7, repuesto.getStock());
            ps.executeUpdate();
        }
    }

    @Override
    public Repuesto findByCodigo(int codigo) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_CODIGO)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return mapRow(rs);
                }                
            }
        }
        return null;
    }

    @Override
    public List<Repuesto> findAll() throws SQLException {
        List<Repuesto> lista = new ArrayList<>();
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
    public int update(Repuesto repuesto) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {              
            ps.setString(1, repuesto.getProducto());
            ps.setString(2, repuesto.getMarca());
            ps.setString(3, repuesto.getColor());
            ps.setDouble(4, repuesto.getPrecioVenta());
            ps.setDouble(5, repuesto.getPrecioCosto());
            ps.setInt(6, repuesto.getStock());
            ps.setInt(7, repuesto.getCodigo());
            int filasAfectadas=ps.executeUpdate();
            return filasAfectadas;        
        }
    }

    @Override
    public int delete(int codigo) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, codigo);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas;
        }
    }

    @Override
    public List<Repuesto> findByProducto(String producto) throws SQLException {
        List<Repuesto> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PRODUCTO_LIKE)) {
            ps.setString(1, "%"+producto+"%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Repuesto> findByMarca(String marca) throws SQLException {
        List<Repuesto> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_MARCA_LIKE)) {
            ps.setString(1, "%"+marca+"%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Repuesto> findByPresupuesto(int numero) throws SQLException {
         List<Repuesto> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PRESUPUESTO)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    private Repuesto mapRow(ResultSet rs) throws SQLException{
        Repuesto repuesto = new Repuesto();
        repuesto.setCodigo(rs.getInt("codigo"));
        repuesto.setProducto(rs.getString("producto"));
        repuesto.setMarca(rs.getString("marca"));
        repuesto.setColor(rs.getString("color"));
        repuesto.setPrecioVenta(rs.getDouble("precio_venta"));
        repuesto.setPrecioCosto(rs.getDouble("precio_costo"));
        repuesto.setStock(rs.getInt("stock"));
        return repuesto;
    }

}
