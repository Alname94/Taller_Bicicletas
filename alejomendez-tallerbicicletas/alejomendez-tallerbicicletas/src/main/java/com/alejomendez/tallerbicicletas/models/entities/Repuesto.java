package com.alejomendez.tallerbicicletas.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repuesto {
    private Integer codigo;
    private String producto;
    private String marca;
    private String color;
    private double precioVenta;
    private double precioCosto;
    private int stock;
}
