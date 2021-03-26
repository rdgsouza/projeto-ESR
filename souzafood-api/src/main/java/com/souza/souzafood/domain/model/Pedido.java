package com.souza.souzafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pedido {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	
	@Embedded
	private Endereco enderecoEntrega;
	
	private StatusPedido status;
	
	@CreationTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private OffsetDateTime dataCriacao;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private OffsetDateTime dataConfirmacao;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private OffsetDateTime dataCancelamento;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private OffsetDateTime dataEntrega;
	
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Restaurante restaurante;
	
	@ManyToOne
	@JoinColumn(name = "usuario_cliente_id", nullable = false)
	private Usuario cliente;

	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itens = new ArrayList<>();
}
