package com.alejomendez.tallerbicicletas.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Detalle {
    private int presupuestoNumero;
    private int repuestoCodigo;
    private int cantidadAgregada;
}
