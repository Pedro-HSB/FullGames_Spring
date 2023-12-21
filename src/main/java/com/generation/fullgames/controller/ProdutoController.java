
package com.generation.fullgames.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.fullgames.model.Produto;
import com.generation.fullgames.repository.ProdutoRepository;
import com.generation.fullgames.service.ProdutoService;

@RestController
@RequestMapping("/Produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ProdutoRepository produtoRepository;

	@GetMapping
	public ResponseEntity<List<Produto>> getAll() {
		return ResponseEntity.ok(produtoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) {
		return produtoRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> getByNome(@PathVariable String nome) {
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}

	@PostMapping
	public ResponseEntity<Produto> post(@RequestBody Produto produto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
	}

	@PutMapping
	public ResponseEntity<Produto> put(@RequestBody Produto produto) {
		if (!produtoRepository.existsById(produto.getId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto));
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {

		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		produtoRepository.deleteById(id);
	}

	@GetMapping("/maiorpreco/{preco}")
	public ResponseEntity<List<Produto>> getByPrecoBigger(@PathVariable double preco) {
		return ResponseEntity.ok(produtoRepository.findByPrecoGreaterThan(preco));
	}

	@GetMapping("/menorpreco/{preco}")
	public ResponseEntity<List<Produto>> getByPrecoLess(@PathVariable double preco) {
		return ResponseEntity.ok(produtoRepository.findByPrecoLessThan(preco));
	}

	@PutMapping("curtida/{id}")
	public ResponseEntity<Produto> curtida(@PathVariable Long id) {
		return produtoService.curtir(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.badRequest().build());
	}

}
