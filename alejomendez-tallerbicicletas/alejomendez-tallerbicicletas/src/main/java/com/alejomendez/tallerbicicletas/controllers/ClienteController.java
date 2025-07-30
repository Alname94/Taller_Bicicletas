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

import com.alejomendez.tallerbicicletas.models.entities.Cliente;
import com.alejomendez.tallerbicicletas.services.ClienteService;

@Controller
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            model.addAttribute("clientes", clientes);
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar los clientes: " + e.getMessage());
        }
        return "clientes-list";
    }

    @GetMapping("/cliente/alta")
    public String altaClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente-alta";
    }

    @PostMapping("/cliente/guardar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente, Model model) {
        try {
            clienteService.guardarCliente(cliente);
            return "redirect:/clientes";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar el cliente: " + e.getMessage());
            return "cliente-alta";
        }        
    }

    @GetMapping("/cliente/editar")
    public String editarCliente(@RequestParam("id") int id, Model model) {
        try {
            Cliente cliente = clienteService.buscarClientePorId(id);
            if(cliente != null){
                model.addAttribute("cliente", cliente);
                return "cliente-editar";
            } else{
                return "redirect:/clientes";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar el cliente para editar: " + e.getMessage());
            return "redirect:/clientes";
        }
    }

    @PostMapping("/cliente/actualizar")
    public String actualizarCliente(@ModelAttribute("cliente") Cliente cliente, Model model) {
        try {
            clienteService.guardarCliente(cliente);
            return "redirect:/clientes";
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al actualizar el ciente: " + e.getMessage());
        }
        return "cliente-editar";
    }

    @GetMapping("/cliente/eliminar")
    public String eliminarCliente(@RequestParam("id") int id, Model model ) {
        try {
            clienteService.eliminarCliente(id);
            return "redirect:/clientes";
        } catch (SQLException e) {
            model.addAttribute("error", "Error al eliminar el cliente: " + e.getMessage());
            try {
                model.addAttribute("", clienteService.obtenerTodosLosClientes());
            } catch (SQLException ex) {
                ex.printStackTrace();
                model.addAttribute("error", "Error al recargar clientes después de eliminación fallida: " + ex.getMessage());
            }
            return "clientes-list";     
        }
    }

    @GetMapping("/clientes/buscar")
    public String buscarCliente(@RequestParam(value="parametro", required = false) String parametro, Model model) {
        List<Cliente> clientes = new ArrayList<>();
        try {
            if (parametro != null && !parametro.trim().isBlank()) {
                String texto = parametro.trim();
                try {
                    int id = Integer.parseInt(texto);
                    Cliente cliente = clienteService.buscarClientePorId(id);
                    if (cliente != null) {
                        model.addAttribute("clientes", List.of(cliente));
                    }
                } catch (NumberFormatException ex) {
                    clientes = clienteService.buscarClientePorNombre(texto);
                    if (clientes.isEmpty()) {
                    clientes = clienteService.buscarClientePorApellido(texto);
                    }
                    model.addAttribute("clientes", clientes);
                }
            }   
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error en búsqueda: " + e.getMessage());
        }
        return "clientes-list";
    }
}
