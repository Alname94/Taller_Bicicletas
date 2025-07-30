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

import com.alejomendez.tallerbicicletas.models.entities.Bicicleta;
import com.alejomendez.tallerbicicletas.models.enums.Rodado;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_BicicletaRepository;

@Repository
public class BicicletaRepository implements I_BicicletaRepository{
    private final DataSource DATASOURCE;

    private static final String SQL_CREATE =
        "INSERT INTO bicicletas (id_cliente, marca, modelo, color, rodado, fecha_ingreso, fecha_egreso) VALUES (?,?,?,?,?,?,?)";

    private static final String SQL_FIND_BY_ID =
        "SELECT * FROM bicicletas WHERE id=?";

    private static final String SQL_FIND_ALL =
        "SELECT * FROM bicicletas";    
        
    private static final String SQL_UPDATE =
        "UPDATE bicicletas SET id_cliente=?, marca=?, modelo=?, color=?, rodado=?, fecha_ingreso=?, fecha_egreso=? WHERE id=?";
        
    private static final String SQL_DELETE =
        "DELETE FROM bicicletas WHERE id=?";
        
    private static final String SQL_FIND_BY_CLIENTE =
        "SELECT * FROM bicicletas WHERE id_cliente=?";

    private static final String SQL_FIND_BY_MARCA_LIKE =
        "SELECT * FROM bicicletas WHERE marca like ?";
    
    private static final String SQL_FIND_BY_MODELO_LIKE =
        "SELECT * FROM bicicletas WHERE modelo like ?";

    private static final String SQL_FIND_BY_PRESUPUESTO =
        "SELECT b.id, b.id_cliente, b.marca, b.modelo, b.color, b.rodado, b.fecha_ingreso, b.fecha_egreso FROM bicicletas b JOIN presupuestos p on b.id = p.bicicleta_id WHERE p.numero=?";        
    

    public BicicletaRepository(DataSource DATASOURCE){
        this.DATASOURCE=DATASOURCE;
    }

    @Override
    public void create(Bicicleta bicicleta) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bicicleta.getIdCliente());
            ps.setString(2, bicicleta.getMarca());
            ps.setString(3, bicicleta.getModelo());
            ps.setString(4, bicicleta.getColor());
            ps.setObject(5, bicicleta.getRodado().getValorAsociado());
            ps.setObject(6, bicicleta.getFechaIngreso());
            ps.setObject(7, bicicleta.getFechaEgreso());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if(keys.next()){
                    bicicleta.setId(keys.getInt(1));
                }
            }
        }
    }    

    @Override
    public Bicicleta findById(int id) throws SQLException {
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
    public List<Bicicleta> findAll() throws SQLException {
        List<Bicicleta> lista = new ArrayList<>();
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
    public int update(Bicicleta bicicleta) throws SQLException {
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setInt(1, bicicleta.getIdCliente());
            ps.setString(2, bicicleta.getMarca());
            ps.setString(3, bicicleta.getModelo());
            ps.setString(4, bicicleta.getColor());
            ps.setObject(5, bicicleta.getRodado().getValorAsociado());
            ps.setObject(6, bicicleta.getFechaIngreso());
            ps.setObject(7, bicicleta.getFechaEgreso());
            ps.setInt(8, bicicleta.getId());
            int filasAfectadas = ps.executeUpdate();
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
    public List<Bicicleta> findByCliente(int id) throws SQLException {
        List<Bicicleta> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_CLIENTE)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Bicicleta> findByMarca(String marca) throws SQLException {
        List<Bicicleta> lista = new ArrayList<>();
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
    public List<Bicicleta> findByModelo(String modelo) throws SQLException {
        List<Bicicleta> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_MODELO_LIKE)) {
            ps.setString(1, "%"+modelo+"%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public Bicicleta findByPresupuesto(int numero) throws SQLException {
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
        

    private Bicicleta mapRow(ResultSet rs) throws SQLException {
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setId(rs.getInt("id"));
        bicicleta.setIdCliente(rs.getInt("id_cliente"));
        bicicleta.setMarca(rs.getString("marca"));
        bicicleta.setModelo(rs.getString("modelo"));
        bicicleta.setColor(rs.getString("color"));        
        bicicleta.setRodado(Rodado.compararValorAsociado(rs.getString("rodado")));
        bicicleta.setFechaIngreso(rs.getObject("fecha_ingreso", LocalDate.class));
        bicicleta.setFechaEgreso(rs.getObject("fecha_egreso", LocalDate.class));
        return bicicleta;
    }
}



