package br.edu.ifms.pibic.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.edu.ifms.pibic.business.DocumentoBO;
import br.edu.ifms.pibic.dao.DocumentoDAO;
import br.edu.ifms.pibic.model.Documento;
import br.edu.ifms.pibic.transport.DocumentoTO;
import br.edu.ifms.pibic.util.TrataMetadados;

@Path("/consulta/")
public class ConsultaService {
	
	private DocumentoBO documentoBO = new DocumentoBO();
	private TrataMetadados trataMetadados = new TrataMetadados();	
	private DocumentoDAO dao = new DocumentoDAO();
	
	@GET
	@Path("/{consulta}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<DocumentoTO> buscaDocumentosPorConteudo(@PathParam("consulta") String consulta) {
		
		//substitui as datas por datas já formatadas para serem apresentadas
		List<DocumentoTO> documentosBO = documentoBO.buscaDocumentosPorConteudo(consulta);
		List<Documento> documentoDAO = dao.recuperaPorConteudo(consulta);
		
		for(int i=0; i<documentosBO.size(); i++){
			documentosBO.get(i).setDataCriacao(trataMetadados.tempoDoDocumento(documentosBO.get(i).getDataCriacao()));
			
			documentosBO.get(i).setConteudoTextual(trataMetadados.trataResumo(documentoDAO.get(i).getConteudoTextual()));			
		}
		
		return documentosBO;
	}
	
	
}
