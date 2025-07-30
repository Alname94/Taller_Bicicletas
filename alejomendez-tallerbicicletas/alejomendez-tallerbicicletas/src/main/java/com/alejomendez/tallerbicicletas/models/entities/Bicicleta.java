package com.alejomendez.tallerbicicletas.models.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.alejomendez.tallerbicicletas.models.enums.Rodado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bicicleta {
    private int id;
    private Integer idCliente;
    private String marca;
    private String modelo;
    private String color;
    private Rodado rodado;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) //Indica a Spring que formatee (y parsee) el campo LocalDate usando el estándar ISO, es decir, el formato yyyy-MM-dd. Permite que Spring convierta correctamente entre el objeto LocalDate en Java y la representación en cadena que requiere <input type="date">.
    private LocalDate fechaIngreso;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaEgreso;
}
