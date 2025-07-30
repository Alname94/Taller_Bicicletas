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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alejomendez.tallerbicicletas.models.dtos.DetalleRepuestoDTO;
import com.alejomendez.tallerbicicletas.models.entities.Bicicleta;
import com.alejomendez.tallerbicicletas.models.entities.Cliente;
import com.alejomendez.tallerbicicletas.models.entities.Presupuesto;
import com.alejomendez.tallerbicicletas.models.entities.Repuesto;
import com.alejomendez.tallerbicicletas.services.BicicletaService;
import com.alejomendez.tallerbicicletas.services.ClienteService;
import com.alejomendez.tallerbicicletas.services.DetalleRepuestoDTOService;
import com.alejomendez.tallerbicicletas.services.PresupuestoService;
import com.alejomendez.tallerbicicletas.services.RepuestoService;

@Controller
public class PresupuestoController {
    private final PresupuestoService presupuestoService;
    private final RepuestoService repuestoService;
    private final ClienteService clienteService;
    private final BicicletaService bicicletaService;
    private final DetalleRepuestoDTOService detalleRepuestoDTOService;

    public PresupuestoController(PresupuestoService presupuestoService, RepuestoService repuestoService,
            ClienteService clienteService, BicicletaService bicicletaService, DetalleRepuestoDTOService detalleRepuestoDTOService) {
        this.presupuestoService = presupuestoService;
        this.repuestoService = repuestoService;
        this.clienteService = clienteService;
        this.bicicletaService = bicicletaService;
        this.detalleRepuestoDTOService = detalleRepuestoDTOService;
    }    

    @GetMapping("/")
    public String home(Model model) {
        try {
            List<Presupuesto> presupuestos = presupuestoService.obtenerTodosLosPresupuestos();
            model.addAttribute("presupuestos", presupuestos);
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("errorPresupuestos", "Error al cargar los presupuestos: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ha ocurrido un error inesperado: " + e.getMessage());
        }
        return "index";
    }

    //Al crear un nuevo presupuesto, se redirige a la vista para editar el presupuesto y así poder agregar repuestos.
    @GetMapping("/presupuesto/alta")
    public String altaPresupuestoForm(@RequestParam(value = "clienteId", required = false) Integer clienteId, Model model) {
        Presupuesto presupuesto = new Presupuesto();
        model.addAttribute("presupuesto", presupuesto);
        if (clienteId != null) {
            presupuesto.setClienteId(clienteId);
        }
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            model.addAttribute("clientes", clientes);
            if(clienteId!=null){
                List<Bicicleta> bicicletas = bicicletaService.buscarBicicletaPorCliente(clienteId);
                model.addAttribute("bicicletas", bicicletas);
            }           
            List<Repuesto> repuestos = repuestoService.obtenerTodosLosRepuestos();
            model.addAttribute("repuestos", repuestos); 
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("errorClientes", "Error al cargar los clientes: " + e.getMessage());
            model.addAttribute("errorBicicletas","Error al cargar las bicicletas: " + e.getMessage());
            model.addAttribute("errorRepuestos", "Error al cargar los repuestos: " + e.getMessage());
        }
        return "presupuesto-alta";
    }

    @PostMapping("/presupuesto/guardar")
    public String guardarPresupuesto(@ModelAttribute("presupuesto") Presupuesto presupuesto, RedirectAttributes ra, Model model) {
        try {
            presupuestoService.guardarPresupuesto(presupuesto);
            int numero = presupuesto.getNumero();
            int clienteId = presupuesto.getClienteId();
            ra.addAttribute("numero", numero);
            ra.addAttribute("clienteId", clienteId);
            editarPresupuesto(numero, clienteId, model);
            return "redirect:/presupuesto/editar";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar el presupuesto: " + e.getMessage());
        }
        return "presupuesto-alta"; 
    }

    @GetMapping("/presupuestos/buscar")
    public String buscarPresupuestoPorNumero(@RequestParam(value="parametro", required = false) String parametro,Model model) {
        List<Presupuesto> presupuestos = new ArrayList<>();
        try {
            if (parametro != null && !parametro.trim().isBlank()) {
                String texto = parametro.trim();
                try {
                    int numero = Integer.parseInt(texto);
                    Presupuesto presupuesto = presupuestoService.buscaPresupuestoPorNumero(numero);
                    if (presupuesto != null) {
                        model.addAttribute("presupuestos", List.of(presupuesto));
                    }
                } catch (NumberFormatException ex) {
                    presupuestos = presupuestoService.buscarPresupuestoPorBicicleta(texto);
                    if (presupuestos.isEmpty()) {
                    presupuestos = presupuestoService.buscarPresupuestoPorCliente(texto);
                    }
                    model.addAttribute("presupuestos", presupuestos);
                }
            }   
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error en búsqueda: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/presupuesto/editar")
    public String editarPresupuesto(@RequestParam("numero") int numero, Integer clienteId,  Model model) {
        try {
            Presupuesto presupuesto = presupuestoService.buscaPresupuestoPorNumero(numero);
            List<Repuesto> repuestos = repuestoService.obtenerTodosLosRepuestos();
            List<DetalleRepuestoDTO> repuestosAgregados = detalleRepuestoDTOService.obtenerRepuestosAgregados(numero);
            model.addAttribute("repuestos", repuestos);
            model.addAttribute("repuestosAgregados", repuestosAgregados);
            if(presupuesto !=null){
                Cliente cliente = clienteService.buscarClientePorPresupuesto(numero);
                Bicicleta bicicleta = bicicletaService.buscarBicicletaPorPresupuesto(numero);
                model.addAttribute("cliente", cliente);
                model.addAttribute("bicicleta", bicicleta);
                model.addAttribute("presupuesto", presupuesto);
                return "presupuesto-editar";
            }else{
                return "redirect:/";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("errorRepuestos", "Error al cargar los presupuestos: " + e.getMessage());
            model.addAttribute("errorDetalles", "Error al cargar los detalles: " + e.getMessage());
            model.addAttribute("error", "Error al cargar el presupuesto para editar: " + e.getMessage());
            return "index";
        }
    }

    @PostMapping("/presupuesto/actualizar")
    public String actualizarPresupuesto(@ModelAttribute("presupuesto") Presupuesto presupuesto, Model model) {
        try {
            presupuestoService.guardarPresupuesto(presupuesto);
            return "redirect:/";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al actualizar el presupuesto: " + e.getMessage());
            return "presupuesto/editar";
        }
    }

    @GetMapping("/presupuesto/eliminar")
    public String eliminarPresupuesto(@RequestParam("numero") int numero, Model model) {
        try {
            presupuestoService.eliminarPresupuesto(numero);
            return "redirect:/";
        } catch (SQLException e) {
            model.addAttribute("error", "Error al eliminar el presupuesto: " + e.getMessage());
            try {
                model.addAttribute("", presupuestoService.obtenerTodosLosPresupuestos());
            } catch (SQLException ex) {
                model.addAttribute("error", "Error al cargar los presupuestos después de la eliminacién fallida: " + ex.getMessage());
            }
            return "index";   
        }
    }
    
    @PostMapping("/presupuesto/agregarRepuesto")
    public String agregarRepuesto(@RequestParam("repuestoCodigo") int repuestoCodigo, @RequestParam("presupuestoNumero") int presupuestoNumero, 
                                @RequestParam("cantidadAgregada") int cantidadAgregada, RedirectAttributes ra, Model model) {
        try {
            presupuestoService.agregarRepuesto(presupuestoNumero, repuestoCodigo, cantidadAgregada);
            ra.addAttribute("numero", presupuestoNumero);
            return "redirect:/presupuesto/editar";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al agregar el repuesto: " + e.getMessage());
            return "index";
        }
    }

    @GetMapping("/presupuesto/eliminarRepuesto")
    public String eliminarRepuesto(@RequestParam("repuestoCodigo") Integer repuestoCodigo, @RequestParam("numero") Integer numero,  
                                RedirectAttributes ra, Model model) {
        try {
            presupuestoService.eliminarRepuesto(numero, repuestoCodigo);
            ra.addAttribute("numero", numero);
            return "redirect:/presupuesto/editar";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al eliminar el repuesto: " + e.getMessage());
            return "index";
        }
    }
}
