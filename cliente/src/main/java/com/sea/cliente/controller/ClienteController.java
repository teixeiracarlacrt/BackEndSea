package com.sea.cliente.controller;

import com.sea.cliente.ClienteService;
import com.sea.cliente.model.Cliente;
import com.sea.cliente.model.ViaCepResponse;
import com.sea.cliente.services.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ViaCepService viaCepService;

    @GetMapping
    public ModelAndView listClientes() {
        List<Cliente> clientes = clienteService.findAll();
        ModelAndView mav = new ModelAndView("listaClientes");
        mav.addObject("clientes", clientes);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView viewCliente(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente inválido"));
        ModelAndView mav = new ModelAndView("cliente/view");
        mav.addObject("cliente", cliente);
        return mav;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/novo")
    public ModelAndView showForm() {
        ModelAndView mav = new ModelAndView("FormCliente");
        mav.addObject("cliente", new Cliente()); // Inicializa um novo Cliente
        return mav;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String salvarCliente(@ModelAttribute @Valid Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return "FormCliente";
        }

        clienteService.save(cliente); // Salva o cliente
        System.out.println("Cliente salvo com sucesso: " + cliente);

        return "redirect:/clientes";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/editar")
    public String editClienteForm(@PathVariable("id") Long id, Model model) {
        Cliente cliente = clienteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        model.addAttribute("cliente", cliente);
        return "editClient"; // Nome do template Thymeleaf para edição
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/editar")
    public String updateCliente(@PathVariable("id") Long id, @Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            cliente.setId(id); // Mantém o ID para que não seja perdido durante o processo de atualização
            return "editClient"; // Retorna para o formulário de edição com erros
        }

        cliente.setId(id); // Atualiza o ID do cliente
        clienteService.save(cliente); // Atualiza o cliente
        return "redirect:/clientes"; // Redireciona para a lista de clientes
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/deletar")
    public String deleteCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.findById(id);
        if (cliente.isPresent()) {
            clienteService.deleteById(id);
        } else {

            return "redirect:/clientes?error=Cliente não encontrado";
        }

        return "redirect:/clientes";
    }


    @GetMapping("/buscarEndereco")
    public ResponseEntity<ViaCepResponse> buscarEndereco(@RequestParam("cep") String cep) {
        ViaCepResponse response = viaCepService.buscarEnderecoPorCep(cep);
        return ResponseEntity.ok(response);
    }
}
