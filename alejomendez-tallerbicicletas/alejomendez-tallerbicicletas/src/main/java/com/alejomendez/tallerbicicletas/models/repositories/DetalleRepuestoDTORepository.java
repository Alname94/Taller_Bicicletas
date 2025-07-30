package com.alejomendez.tallerbicicletas.models.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.alejomendez.tallerbicicletas.models.dtos.DetalleRepuestoDTO;
import com.alejomendez.tallerbicicletas.models.repositories.interfaces.I_DetalleRepuestoDTORepository;

@Repository
public class DetalleRepuestoDTORepository implements I_DetalleRepuestoDTORepository {
    private final DataSource DATASOURCE;

    
    public DetalleRepuestoDTORepository(DataSource dATASOURCE) {
        DATASOURCE = dATASOURCE;
    }

    private static final String SQL_FIND_BY_PRESUPUESTO_DTO =
        "SELECT r.codigo, r.producto, r.marca, r.color, d.cantidad_agregada FROM detalles d JOIN repuestos r on d.repuesto_codigo = r.codigo JOIN presupuestos p on p.numero = d.presupuesto_numero WHERE d.presupuesto_numero=?";


    @Override
    public List<DetalleRepuestoDTO> findByPresupuestoDTO(int presupuestoNumero) throws SQLException {
        List<DetalleRepuestoDTO> lista = new ArrayList<>();
        try (Connection conn = DATASOURCE.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PRESUPUESTO_DTO)) {
            ps.setInt(1, presupuestoNumero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }            
        }
        return lista;
    }

    private DetalleRepuestoDTO mapRow(ResultSet rs) throws SQLException {
        DetalleRepuestoDTO detalleRepuestoDTO = new DetalleRepuestoDTO();
        detalleRepuestoDTO.setCodigo(rs.getInt("codigo"));
        detalleRepuestoDTO.setProducto(rs.getString("producto"));
        detalleRepuestoDTO.setMarca(rs.getString("marca"));
        detalleRepuestoDTO.setColor(rs.getString("color"));
        detalleRepuestoDTO.setCantidadAgregada(rs.getInt("cantidad_agregada"));
        return detalleRepuestoDTO;
    }
}
