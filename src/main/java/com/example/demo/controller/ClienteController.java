package com.example.demo.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;


import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;

import org.springframework.beans.BeanUtils;


import com.fasterxml.jackson.databind.JsonNode;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;

	@GetMapping
	public List<Cliente> FindAll(){
		return clienteRepository.findAll();
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> FindById(@PathVariable Long id) {
	    Optional<Cliente> clienteExistente = clienteRepository.findById(id);

	    if (clienteExistente.isPresent()) {
	        Cliente clienteEncontrado = clienteExistente.get();
	        return ResponseEntity.ok(clienteEncontrado);
	    } else {
	        String mensagemDeErro = "Cliente com o ID " + id + " não foi encontrado.";
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemDeErro(mensagemDeErro));
	    }
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adicionar(@RequestBody() Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
	    Optional<Cliente> clienteExistente = clienteRepository.findById(id);
	    
	    if (clienteExistente.isPresent()) {
	        Cliente cliente = clienteExistente.get();
	        cliente.setNome(clienteAtualizado.getNome());
	        Cliente clienteSalvo = clienteRepository.save(cliente);
	        return ResponseEntity.ok(clienteSalvo);
	    } else {
	        String mensagemDeErro = "Cliente com o ID " + id + " não foi encontrado.";
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemDeErro(mensagemDeErro));
	    }
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirPorId(@PathVariable Long id) {
	    try {
	        clienteRepository.deleteById(id);
	        return ResponseEntity.noContent().build();
	    } catch (EmptyResultDataAccessException e) {
	        String mensagemDeErro = "Cliente com o ID " + id + " não foi encontrado.";
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemDeErro(mensagemDeErro));
	    }
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> atributos) {
	    Optional<Cliente> optionalCliente = clienteRepository.findById(id);
	    if (optionalCliente.isPresent()) {
	        Cliente cliente = optionalCliente.get();
	        // Itera sobre os atributos recebidos no corpo da requisição
	        for (String chave : atributos.keySet()) {
	            try {
	                // Usa reflexão para atualizar o atributo no objeto cliente
	                Field field = Cliente.class.getDeclaredField(chave);
	                field.setAccessible(true);
	                field.set(cliente, atributos.get(chave));
	            } catch (NoSuchFieldException | IllegalAccessException e) {
	                e.printStackTrace();
	                return ResponseEntity.badRequest().build();
	            }
	        }
	        // Salva o cliente atualizado no banco de dados
	        clienteRepository.save(cliente);
	        return ResponseEntity.ok(cliente);
	    } else {
	        String mensagemDeErro = "Cliente com o ID " + id + " não foi encontrado.";
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemDeErro(mensagemDeErro));
	    }
	}
	
	
}
