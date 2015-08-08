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

	/**
	 * Metodo para recuperar a pagina de um documento
	 * @param hash
	 * @param numeroPagina
	 * @return
	 */
	public File pegaPaginaDocumento(String hash, Integer numeroPagina) {
		Documento documento = documentoDAO.recupera(hash);
		
		
		if (documento != null) {
			try {
				Pagina pagina = paginaDAO.recuperaPorDocumentoPagina(documento,
						numeroPagina);
				if (pagina != null) {
					byte[] bytesArquivo = pagina.getBytesPagina();
					bytesArquivo = CompressionTools.decompress(bytesArquivo);
					File arquivoPagina = FileUtil.fromByteArrayToTempFile(
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

	/**
	 * Metodo para recuperar um documento
	 * @param hash
	 * @return
	 */
	public File pegaDocumento(String hash) {
		
		File arquivo = new File("");
		Documento documento = documentoDAO.recupera(hash);
		//editado por mim
		String extensao = documento.getExtensao();
		String nome = documento.getNomeArquivo();
		
		if (documento != null) {			
			List<Pagina> paginasDocumento = paginaDAO
					.recuperaPaginasOrdenadas(documento);
			if (paginasDocumento != null && paginasDocumento.size() > 0) {
				try {
					List<File> arquivosPaginas = new ArrayList<File>();
					for (Pagina pagina : paginasDocumento) {
						byte[] bytesPagina = pagina.getBytesPagina();
						arquivo = FileUtil.fromByteArrayToFile(
								bytesPagina, nome);
														
						
						arquivosPaginas.add(arquivo);
					}
					if(extensao.equals(".pdf")){
						File arquivoDestino = PDFUtil.juntaArquivos(
								arquivosPaginas, documento.getNomeArquivo());
						logger.info("Documento gerado.");					
						return arquivoDestino;
					}
					return arquivo;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		logger.warn("Documento não gerado.");
		return null;
	}
}
