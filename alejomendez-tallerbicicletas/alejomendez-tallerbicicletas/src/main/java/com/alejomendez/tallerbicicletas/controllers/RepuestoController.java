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

import com.alejomendez.tallerbicicletas.models.entities.Repuesto;
import com.alejomendez.tallerbicicletas.services.RepuestoService;

@Controller
public class RepuestoController {
    private final RepuestoService repuestoService;

    public RepuestoController(RepuestoService repuestoService) {
        this.repuestoService = repuestoService;
    }

    @GetMapping("/repuestos")
    public String listarRepuestos(Model model) {
        try {
            List<Repuesto> repuestos = repuestoService.obtenerTodosLosRepuestos();
            model.addAttribute("repuestos", repuestos);
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar los repuestos: " + e.getMessage());
        }
        return "repuestos-list";
    }

    @GetMapping("/repuesto/alta")
    public String altaRepuestoForm(Model model) {
        model.addAttribute("repuesto", new Repuesto());
        return "repuesto-alta";
    }

    @PostMapping("/repuesto/crear")
    public String crearRepuesto(@ModelAttribute("repuesto") Repuesto repuesto, Model model) {
        try {
            repuestoService.crearRepuesto(repuesto);
            return "redirect:/repuestos";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al crear el repuesto: " + e.getMessage());
            return "repuesto-alta";
        }        
    }

    @GetMapping("/repuesto/editar")
    public String editarRepuesto(@RequestParam("codigo") Integer codigo, Model model) {
        try {
            Repuesto repuesto = repuestoService.buscarRepuestoPorCodigo(codigo);
            if(repuesto != null){
                model.addAttribute("repuesto", repuesto);
                return "repuesto-editar";
            } else{
                return "redirect:/repuestos";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar el repuesto para editar: " + e.getMessage());
            return "redirect:/repuestos";
        }
    }

    @PostMapping("/repuesto/actualizar")
    public String actualizarRepuesto(@ModelAttribute("repuesto") Repuesto repuesto, Model model) {
        try {
            repuestoService.guardarRepuesto(repuesto);
            return "redirect:/repuestos";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al actualizar el repuesto: " + e.getMessage());
        }
        return "repuesto-editar";
    }

    @GetMapping("/repuesto/eliminar")
    public String eliminarRepuesto(@RequestParam("codigo") int codigo, Model model ) {
        try {
            repuestoService.eliminarRepuesto(codigo);
            return "redirect:/repuestos";
        } catch (SQLException e) {
            model.addAttribute("error", "Error al eliminar el repuesto: " + e.getMessage());
            try {
                model.addAttribute("", repuestoService.obtenerTodosLosRepuestos());
            } catch (SQLException ex) {
                ex.printStackTrace();
                model.addAttribute("error", "Error al recargar los repuestos después de eliminación fallida: " + ex.getMessage());
            }
            return "repuestos-list";     
        }
    }

    @GetMapping("/repuestos/buscar")
    public String buscarRepuestoPorCodigo(@RequestParam(value="parametro", required = false) String parametro, Model model) {
        List<Repuesto> repuestos = new ArrayList<>();
        try {
            if (parametro != null && !parametro.trim().isBlank()) {
                String texto = parametro.trim();
                try {
                    int codigo = Integer.parseInt(texto);
                    Repuesto repuesto = repuestoService.buscarRepuestoPorCodigo(codigo);
                    if (repuesto != null) {
                        model.addAttribute("repuestos", List.of(repuesto));
                    }
                } catch (NumberFormatException ex) {
                    repuestos = repuestoService.buscarRepuestoPorProducto(texto);
                    if (repuestos.isEmpty()) {
                    repuestos = repuestoService.buscarRepuestoPorMarca(texto);
                    }
                    model.addAttribute("repuestos", repuestos);
                }
            }   
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error en búsqueda: " + e.getMessage());
        }
        return "repuestos-list";
    }
}
