package br.edu.ifms.pibic.business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.CompressionTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ifms.pibic.dao.DocumentoDAO;
import br.edu.ifms.pibic.dao.PaginaDAO;
import br.edu.ifms.pibic.model.Documento;
import br.edu.ifms.pibic.model.Pagina;
import br.edu.ifms.pibic.util.FileUtil;
import br.edu.ifms.pibic.util.PDFUtil;

public class PaginaBO {

	private Logger logger = LoggerFactory.getLogger(PaginaBO.class);

	private PaginaDAO paginaDAO = new PaginaDAO();

	private DocumentoDAO documentoDAO = new DocumentoDAO();

	public File pegaPaginaDocumento(String hash, Integer numeroPagina) {
		Documento documento = documentoDAO.recupera(hash);
		if (documento != null) {
			try {
				Pagina pagina = paginaDAO.recuperaPorDocumentoPagina(documento,
						numeroPagina);
				if (pagina != null) {
					byte[] bytesArquivo = pagina.getBytesPagina();
					bytesArquivo = CompressionTools.decompress(bytesArquivo);
					File arquivoPagina = FileUtil.fromByteArrayToFile(
							bytesArquivo,
							documento.getNomeArquivo().split(".pdf")[0]
									+ "_PAGINA_" + numeroPagina
									+ documento.getExtensao());
					logger.info("Página gerada.");
					return arquivoPagina;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.warn("Página não gerada.");
		return null;
	}

	public File pegaDocumento(String hash) {
		Documento documento = documentoDAO.recupera(hash);
		if (documento != null) {
			List<Pagina> paginasDocumento = paginaDAO
					.recuperaPaginasOrdenadas(documento);
			if (paginasDocumento != null && paginasDocumento.size() > 0) {
				try {
					List<File> arquivosPaginas = new ArrayList<File>();
					for (Pagina pagina : paginasDocumento) {
						byte[] bytesPagina = pagina.getBytesPagina();
						File arquivo = FileUtil.fromByteArrayToFile(
								bytesPagina, null);
						arquivosPaginas.add(arquivo);
					}
					File arquivoDestino = PDFUtil.juntaArquivos(
							arquivosPaginas, documento.getNomeArquivo());
					logger.info("Documento gerado.");
					return arquivoDestino;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		logger.warn("Documento não gerado.");
		return null;
	}
}
