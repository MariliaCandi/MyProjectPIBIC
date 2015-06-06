package br.edu.ifms.pibic.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.edu.ifms.pibic.business.DocumentoBO;
import br.edu.ifms.pibic.model.Documento;
import br.edu.ifms.pibic.transport.DocumentoTO;

@Path("/consulta/")
public class ConsultaService {
	
	private DocumentoBO documentoBO = new DocumentoBO();
	public static void main(String[] args) {
		ConsultaService conWEB = new ConsultaService();
		
		System.out.println("Resultado: "+conWEB.testeDoc("marilia"));
	}

	
	
	public List<Documento> testeDoc(String consulta){	
		
		return documentoBO.recuperaPorConteudo(consulta);
		
	}
	

	@GET
	@Path("/{consulta}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<DocumentoTO> buscaDocumentosPorConteudo(@PathParam("consulta") String consulta) {
		return documentoBO.buscaDocumentosPorConteudo(consulta);
	}
}
