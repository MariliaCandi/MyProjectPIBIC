package br.edu.ifms.pibic.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.pt.PortugueseStemFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

@Entity
@Indexed
@AnalyzerDef(name = "analisador", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), filters = {
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
		@TokenFilterDef(factory = PortugueseStemFilterFactory.class),
		@TokenFilterDef(factory = StopFilterFactory.class, params = {
				@Parameter(name = "words", value = "br/edu/ifms/pibic/model/stopwords.txt"),
				@Parameter(name = "ignoreCase", value = "true") }) })
public class Documento {

	@Id
	private String hash;

	@Column(nullable = false, length = 200)
	@Field(analyze = Analyze.YES, index = Index.YES, store = Store.COMPRESS)
	private String nomeArquivo;

	@Column(length = 10, nullable = false)
	@Field(store = Store.YES)
	private String extensao;

	@Temporal(TemporalType.TIMESTAMP)
	@Field(store = Store.YES)
	@Column(name = "data_criacao", nullable = false)
	private Date dataCriacao;

	@Column(name = "numero_paginas", nullable = true)
	@Field(store = Store.YES)
	private Integer numeroPaginas;

	@Column(name = "conteudo_textual", columnDefinition = "TEXT", nullable = false)
	@Analyzer(definition = "analisador")
	@Field(analyze = Analyze.YES, index = Index.YES, store = Store.COMPRESS)
	private String conteudoTextual;

	@Transient
	private Float score;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Integer getNumeroPaginas() {
		return numeroPaginas;
	}

	public void setNumeroPaginas(Integer numeroPaginas) {
		this.numeroPaginas = numeroPaginas;
	}

	public String getConteudoTextual() {
		return conteudoTextual;
	}

	public void setConteudoTextual(String conteudoTextual) {
		this.conteudoTextual = conteudoTextual;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

}
