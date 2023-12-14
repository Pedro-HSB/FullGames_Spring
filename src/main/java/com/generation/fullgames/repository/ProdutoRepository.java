package com.generation.fullgames.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.fullgames.model.Produto;


public interface ProdutoRepository extends JpaRepository<Produto,Long>{

	List<Produto> findAllByNomeContainingIgnoreCase(@Param("nome")String nome);
	List<Produto> findByPrecoLessThan(@Param("preco")double preco);
	List<Produto> findByPrecoGreaterThan(@Param("preco")double preco);


}