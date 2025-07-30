package com.alejomendez.tallerbicicletas.models.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Presupuesto {
    private int numero;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;
    private Integer clienteId;
    private Integer bicicletaId;
    private double valorTotal;
    private String detalle;
}
