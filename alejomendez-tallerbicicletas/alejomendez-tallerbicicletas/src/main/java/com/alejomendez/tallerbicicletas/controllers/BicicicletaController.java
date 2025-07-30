package com.alejomendez.tallerbicicletas.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alejomendez.tallerbicicletas.models.entities.Bicicleta;
import com.alejomendez.tallerbicicletas.models.entities.Cliente;
import com.alejomendez.tallerbicicletas.services.BicicletaService;
import com.alejomendez.tallerbicicletas.services.ClienteService;

@Controller
public class BicicicletaController {
    private final BicicletaService bicicletaService;
    private final ClienteService clienteService;

    public BicicicletaController(BicicletaService bicicletaService, ClienteService clienteService) {
        this.bicicletaService = bicicletaService;
        this.clienteService = clienteService;
    }

    @GetMapping("/bicicletas")
    public String listarBicicletas(Model model) {
        try {
            List<Bicicleta> bicicletas = bicicletaService.obtenerTodasLasBicicletas();
            model.addAttribute("bicicletas", bicicletas);
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar las bicicletas: " + e.getMessage());
        }
        return "bicicletas-list";        
    }

    @GetMapping("/bicicleta/alta")
    public String altaBicicletaForm(Model model) {
        model.addAttribute("bicicleta", new Bicicleta());
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            model.addAttribute("clientes", clientes);
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("errorClientes", "Error al cargar los clientes: " + e.getMessage());
        }
        return "bicicleta-alta";
    }

    @PostMapping("/bicicleta/guardar")
    public String guardarBicicleta(@ModelAttribute("bicicleta") Bicicleta bicicleta, Model model) {
        try {
            bicicletaService.guardarBicicleta(bicicleta);
            return "redirect:/bicicletas";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar la bicicleta: " + e.getMessage());
        }
        return "bicicleta-alta"; 
    }

    @GetMapping("/bicicleta/editar")
    public String editarBicicleta(@RequestParam("id") int id, Model model) {
        try {
            Bicicleta bicicleta = bicicletaService.buscarBicicletaPorId(id);
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            model.addAttribute("clientes", clientes);
            if(bicicleta !=null){
                model.addAttribute("bicicleta", bicicleta);
                return "bicicleta-editar";
            }else{
                return "redirect:/bicicletas";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar la bicicleta para editar: " + e.getMessage());
            return "redirect:/bicicletas";
        }
    }

    @PostMapping("/bicicleta/actualizar")
    public String actualizarBicicleta(@ModelAttribute("bicicleta") Bicicleta bicicleta, Model model) {
        try {
            bicicletaService.guardarBicicleta(bicicleta);
            return "redirect:/bicicletas";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al actualizar la bicicleta: " + e.getMessage());
            return "bicicleta/editar";
        }
    }

    @GetMapping("/bicicleta/eliminar")
    public String eliminarBicicleta(@RequestParam("id") int id, Model model) {
        try {
            bicicletaService.eliminarBicicleta(id);
            return "redirect:/bicicletas";
        } catch (SQLException e) {
            model.addAttribute("error", "Error al eliminar la bicicleta: " + e.getMessage());
            try {
                model.addAttribute("", bicicletaService.obtenerTodasLasBicicletas());
            } catch (SQLException ex) {
                model.addAttribute("error", "Error al cargar las bicicletas después de la eliminacién fallida: " + ex.getMessage());
            }
            return "bicicletas-list";   
        }
    }

    @GetMapping("/bicicletas/buscar")
    public String buscarBicicleta(@RequestParam(value="parametro", required=false) String parametro, Model model) {
        List<Bicicleta> bicicletas = new ArrayList<>();
        try {
            if (parametro != null && !parametro.trim().isBlank()) {
                String texto = parametro.trim();
                try {
                    int id = Integer.parseInt(texto);
                    Bicicleta bicicleta = bicicletaService.buscarBicicletaPorId(id);
                    if (bicicleta != null) {
                        model.addAttribute("bicicletas", List.of(bicicleta));
                    }
                } catch (NumberFormatException ex) {
                    bicicletas = bicicletaService.buscarBicicletaPorMarca(texto);
                    if (bicicletas.isEmpty()) {
                    bicicletas = bicicletaService.buscarBicicletaPorModelo(texto);
                    }
                    model.addAttribute("bicicletas", bicicletas);
                }
            }    
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error en búsqueda: " + e.getMessage());
        }
        return "bicicletas-list";
    }
}
