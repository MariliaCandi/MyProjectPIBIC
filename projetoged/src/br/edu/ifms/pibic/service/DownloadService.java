package br.edu.ifms.pibic.service;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.edu.ifms.pibic.business.PaginaBO;

@Path("/download/")
public class DownloadService {

	private PaginaBO paginaBO = new PaginaBO();
	

	@GET
	@Path("/{hash}/{numero}")
	public Response pegaPaginaDocumento(@PathParam("hash") String hash,
			@PathParam("numero") Integer numero) {
		File arquivoPagina = paginaBO.pegaPaginaDocumento(hash, numero);
		if (arquivoPagina != null) {
			ResponseBuilder response = Response.ok((Object) arquivoPagina);
			String nomeArquivo = arquivoPagina.getName().split(".pdf")[0]
					+ ".pdf";
			response.header("Content-Disposition", "inline; filename="
					+ nomeArquivo);
			return response.type("application/pdf").build();
		}
		return Response.noContent().status(Status.NOT_FOUND)
				.entity("Documento não encontrado").build();
	}

	@GET
	@Path("/{hash}")
	public Response pegaDocumento(@PathParam("hash") String hash) {
		File arquivoDocumento = paginaBO.pegaDocumento(hash);
		if (arquivoDocumento != null) {
			ResponseBuilder response = Response.ok((Object) arquivoDocumento);
			String nomeArquivo = arquivoDocumento.getName().split(".pdf")[0]
					+ ".pdf";
			response.header("Content-Disposition", "attachment; filename="
					+ nomeArquivo);
			return response.type("application/pdf").build();
		}
		return Response.noContent().status(Status.NOT_FOUND)
				.entity("Documento não encontrado").build();
	}

	
}
