package com.alejomendez.tallerbicicletas.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
}
