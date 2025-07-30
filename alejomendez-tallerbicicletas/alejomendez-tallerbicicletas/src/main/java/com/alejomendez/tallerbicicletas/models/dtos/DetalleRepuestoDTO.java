package com.alejomendez.tallerbicicletas.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleRepuestoDTO {
    //Esta clase permite obtener los datos de la clase Repuesto junto con la cantidad agregada de la clase Detalle
    private int codigo;
    private String producto;
    private String marca;
    private String color;
    private int cantidadAgregada;
}
