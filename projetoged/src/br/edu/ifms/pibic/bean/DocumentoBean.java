package br.edu.ifms.pibic.bean;


import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.edu.ifms.pibic.service.ConsultaService;
import br.edu.ifms.pibic.transport.DocumentoTO;

@ManagedBean
@SessionScoped
public class DocumentoBean {
	
	

	private List<DocumentoTO> documentos;	
	private String url;
	private int numeroPaginas;
	private String consulta;
	private String dataCriacao;
	private String extensao;
	private ConsultaService consultaService = new ConsultaService();
	
	
	//abre documento em uma outra pagina
	public String redirecionaUrl() {
		return "exibirDocumento?faces-redirect=true";			
	}
	
			
	public String redirecionaConsulta() {
		listaDocumentos();
		return "pesquisa?faces-redirect=true"; 
	}
	
	public List<DocumentoTO> listaDocumentos(){
		documentos = consultaService.buscaDocumentosPorConteudo(consulta);				
		return documentos;
	}	
	
	public void setConsultaService(ConsultaService consultaService) {
		this.consultaService = consultaService;
	}
	
	public List<DocumentoTO> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<DocumentoTO> documentos) {
		this.documentos = documentos;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public ConsultaService getConsultaService() {
		return consultaService;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public int getNumeroPaginas() {
		return numeroPaginas;
	}

	public void setNumeroPaginas(int numeroPaginas) {
		this.numeroPaginas = numeroPaginas;
	}

	public String getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}
	
	

}
