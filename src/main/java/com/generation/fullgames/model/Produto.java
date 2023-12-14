

package com.generation.fullgames.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="produtos")
public class Produto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=100)
	@NotBlank(message="O Atributo nome e Obrigatorio")
	@Size(min=5,max=100,message="o nome deve ser maior que 5")
	private String nome;
	
	@Column(length=200)
	@NotBlank(message="O Atributo imagem e Obrigatorio")
	@Size(min=5,max=200,message="o imagem deve ser maior que 5")
	private String imagem;
	
	@Column(length=100)
	@NotBlank(message="O Atributo plataforma e Obrigatorio")
	@Size(min=5,max=100,message="o plataforma deve ser maior que 5")
	private String plataforma;

	@Column(columnDefinition = "DECIMAL(5,2)")
	@DecimalMax("10000.00") 
	@DecimalMin("00.00") 
	private double preco;
	
	@Column(length=100)
    private Integer estoque;
	
	@ManyToOne
    @JsonIgnoreProperties("produtos")
    private Categoria categoria;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public String getPlataforma() {
		return plataforma;
	}

	public void setPlataforma(String plataforma) {
		this.plataforma = plataforma;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public Integer getEstoque() {
		return estoque;
	}

	public void setEstoque(Integer estoque) {
		this.estoque = estoque;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}
