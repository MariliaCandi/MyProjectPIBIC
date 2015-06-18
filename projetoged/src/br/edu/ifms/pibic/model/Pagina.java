package br.edu.ifms.pibic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries(value = {
		@NamedQuery(name = Pagina.RECUPERA_POR_DOCUMENTO_PAGINA, query = "FROM Pagina WHERE documento.hash = :hash AND numero = :numero ORDER BY numero"),
		@NamedQuery(name = Pagina.RECUPERA_PAGINAS_ORDENADAS, query = "FROM Pagina WHERE documento.hash = :hash ORDER BY numero"),
		@NamedQuery(name = Pagina.REMOVE_TODAS_PAGINAS, query = "DELETE FROM Pagina WHERE documento.hash = :hash"),
		@NamedQuery(name = Pagina.REMOVE_POR_DOCUMENTO_E_PAGINA, query = "DELETE FROM Pagina WHERE documento.hash = :hash AND numero = :numero") })
public class Pagina {

	public static final String RECUPERA_POR_DOCUMENTO_PAGINA = "recuperaPorDocumentoPagina";

	public static final String RECUPERA_PAGINAS_ORDENADAS = "recuperaPaginasOrdenadas";

	public static final String REMOVE_TODAS_PAGINAS = "removeTodasPaginas";

	public static final String REMOVE_POR_DOCUMENTO_E_PAGINA = "removePorDocumentoEPagina";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private Integer numero;

	@Column(nullable = false)
	private byte[] bytesPagina;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Documento documento;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public byte[] getBytesPagina() {
		return bytesPagina;
	}

	public void setBytesPagina(byte[] bytesPagina) {
		this.bytesPagina = bytesPagina;
	}

}
