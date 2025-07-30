package com.alejomendez.tallerbicicletas.models.enums;

public enum Rodado {
    R14("14"),
    R16("16"),
    R20("20"),
    R22("22"),
    R24("24"),
    R26("26"),
    R27_5("27.5"),
    R28("28"),
    R29("29");

    private final String valorAsociado; //es el valor asociado a cada constante

    Rodado(String valorAsociado) { 
        this.valorAsociado = valorAsociado; 
    }

    public String getValorAsociado() { 
        return valorAsociado; 
    }

    public static Rodado compararValorAsociado(String valorAsociado) {
        for (Rodado r : values()) {
            if (r.valorAsociado.equals(valorAsociado)) return r;
        }
        throw new IllegalArgumentException("Rodado inválido: " + valorAsociado);
    }

    //este operador ternario verifica si el numero es entero o double, ya que si el rodado elegido es 28 y no se hace la especificación de que es un int, Java lo toma como 28.0 y ese valor no coincide con el 28 de la BD, generando una Exception.
    //Utilizando este método, al crear una instancia de Bicicleta, podemos pasar como parámetro del rodado tanto un int(28) como un double(27.5)
    public static Rodado obtenerRodado(double numero) {
        return compararValorAsociado(numero % 1 == 0 ? String.valueOf((int) numero) : String.valueOf(numero));
    }
}
