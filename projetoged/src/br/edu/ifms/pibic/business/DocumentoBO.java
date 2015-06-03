package br.edu.ifms.pibic.business;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.document.CompressionTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ifms.pibic.dao.DocumentoDAO;
import br.edu.ifms.pibic.dao.PaginaDAO;
import br.edu.ifms.pibic.model.Documento;
import br.edu.ifms.pibic.model.Pagina;
import br.edu.ifms.pibic.transport.DocumentoTO;
import br.edu.ifms.pibic.util.CompressionUtil;
import br.edu.ifms.pibic.util.FileUtil;
import br.edu.ifms.pibic.util.PDFUtil;

public class DocumentoBO {

	private Logger logger = LoggerFactory.getLogger(DocumentoBO.class);

	private DocumentoDAO documentoDAO = new DocumentoDAO();

	public static final String URL = "/rest/download/";

	/**
	 * Metodo para reindexar a base de dados
	 */
	public void reindexaBaseInteira() {
		documentoDAO.reindexaBaseInteira();
	}

	
	/**
	 * Metodo para listar documentos pesquisados 	 
	 * @param consulta
	 * @return
	 */
	public List<DocumentoTO> buscaDocumentosPorConteudo(String consulta) {
		List<DocumentoTO> documentosEncontrados = new ArrayList<DocumentoTO>();
		List<Documento> documentos = recuperaPorConteudo(consulta);
		if (documentos != null && documentos.size() > 0) {
			for (Documento documento : documentos) {
				DocumentoTO documentoTO = deEntidadeParaObjetoTransporte(documento);
				documentosEncontrados.add(documentoTO);
			}
		}
		return documentosEncontrados;
	}

	/**
	 * Metodo para selecionar os documentos que contenhão as palavras pesquisadas
	 * @param consulta
	 * @return
	 */
	public List<Documento> recuperaPorConteudo(String consulta) {
		return documentoDAO.recuperaPorConteudo(consulta);
	}

	public void varreDiretorio(String caminhoDiretorio) {
		logger.info("Iniciando a varredura no diretório '" + caminhoDiretorio
				+ "'.");
		File arquivo = abreArquivo(caminhoDiretorio);
		if (arquivo != null && arquivo.exists() && arquivo.isDirectory()) {
			arquivo = null;
			varreSubdiretorio(caminhoDiretorio);
		}
	}

	/**
	 * Metodo para pegar dados de cada documento selecionado e transforma-los em 
	 * objeto de transporte 
	 * @param documento
	 * @return
	 */
	private DocumentoTO deEntidadeParaObjetoTransporte(Documento documento) {
		DocumentoTO documentoTO = new DocumentoTO();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Date dataCriacao = documento.getDataCriacao();
		documentoTO.setDataCriacao(formatador.format(dataCriacao));
		documentoTO.setExtensao(documento.getExtensao());
		documentoTO.setHash(documento.getHash());
		documentoTO.setNomeArquivo(documento.getNomeArquivo());
		documentoTO.setNumeroPaginas(documento.getNumeroPaginas());
		documentoTO.setScore(documento.getScore());
		documentoTO.setUrl(URL + documento.getHash());
		return documentoTO;
	}

	/**
	 * Metodo para abrir os arquivos
	 * @param caminhoArquivo
	 * @return
	 */
	private File abreArquivo(String caminhoArquivo) {
		File arquivo = new File(caminhoArquivo);
		if (arquivo.exists()) {
			return arquivo;
		}
		return null;
	}

	/**
	 * Metodo para pegar a extensão dos arquivos selecionados
	 * @param arquivo
	 * @return
	 */
	private String pegaExtensaoArquivo(File arquivo) {
		if (arquivo != null && arquivo.exists()) {
			String nomeArquivo = arquivo.getName().trim();
			String[] aux = nomeArquivo.split("\\.");
			return aux.length > 0 ? aux[aux.length - 1] : "";
		}
		return "";
	}

	/**
	 * Metodo para varrer toda a pasta e suas sub-pastas
	 * @param caminhoDiretorio
	 */
	private void varreSubdiretorio(String caminhoDiretorio) {
		File diretorio = new File(caminhoDiretorio);
		String[] listaDiretorios = diretorio.list();
		if (listaDiretorios != null && listaDiretorios.length > 0) {
			for (String nomeSubDiretorio : diretorio.list()) {
				nomeSubDiretorio = diretorio.getPath() + "/" + nomeSubDiretorio;
				File subDiretorio = abreArquivo(nomeSubDiretorio);
				if (subDiretorio != null && subDiretorio.exists()) {
					if (subDiretorio.isDirectory()) {
						String caminhoSubdiretorio = subDiretorio.getPath();
						logger.info("Abrindo subdiretório '"
								+ caminhoSubdiretorio + "'.");
						subDiretorio = null;
						varreSubdiretorio(caminhoSubdiretorio);
					} else if (pegaExtensaoArquivo(subDiretorio)
							.equalsIgnoreCase("pdf")) {
						logger.info("Arquivo '"
								+ subDiretorio.getName()
								+ "' encontrado. Iniciando o armazenamento e indexação.");
						salvaDocumento(subDiretorio);
					}
				}
			}
		}
	}

	/**
	 * Metodo para salvar todos os metaddos de cada arquivo no banco de dados
	 * @param arquivo
	 * @return
	 */
	private boolean salvaDocumento(File arquivo) {
		try {
			String nomeArquivo = arquivo.getName();
			String caminhoArquivo = arquivo.getPath();
			byte[] bytesArquivo = FileUtil.fromFileToByteArray(arquivo);
			String hash = nomeArquivo.trim() + caminhoArquivo.trim()
					+ new String(bytesArquivo, "UTF-8");
			hash = CompressionUtil.generateMd5Hash(hash);

			if (documentoDAO.recupera(hash) != null) {
				logger.info("Documento já existente na base de dados. Armazenamento e indexação abortados.");
				return false;
			}

			Integer numeroPaginas = PDFUtil.pegaNumeroPaginas(arquivo);
			String conteudoTextual = PDFUtil.pegaTextoPDF(arquivo);

			Documento documento = new Documento();
			documento.setConteudoTextual(conteudoTextual);
			documento.setDataCriacao(new Date());
			documento.setExtensao(".pdf");
			documento.setHash(hash);
			documento.setNomeArquivo(nomeArquivo);
			documento.setNumeroPaginas(numeroPaginas);
			if (documentoDAO.salva(documento)) {
				int paginasSalvas = salvaPaginasDoDocumento(documento, arquivo);
				if (paginasSalvas < documento.getNumeroPaginas()) {
					logger.error("Ocorreu um erro ao tentar armazenar as páginas do documento. O documento será removido.");
					if (!documentoDAO.remove(documento)) {
						logger.error("Ocorreu um erro ao tentar remover o documento. Verificar integridade do banco de dados.");
					}
				} else {
					return true;
				}
			} else {
				logger.error("Ocorreu um erro ao tentar salvar o arquivo '"
						+ arquivo.getName() + "'.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Metodo para salvar em uma outra tabela cada pagina de cada documento
	 * @param documento
	 * @param arquivo
	 * @return
	 */
	private int salvaPaginasDoDocumento(Documento documento, File arquivo) {
		logger.info("Armazenando as páginas.");
		PaginaDAO paginaDAO = new PaginaDAO();
		int contador = 0;
		for (int i = 1; i <= documento.getNumeroPaginas(); i++) {
			File arquivoPagina = PDFUtil.pegaPaginaDocumento(arquivo, i);
			byte[] bytesPagina = FileUtil.fromFileToByteArray(arquivoPagina);
			bytesPagina = CompressionTools.compress(bytesPagina);

			Pagina pagina = new Pagina();
			pagina.setDocumento(documento);
			pagina.setNumero(i);
			pagina.setBytesPagina(bytesPagina);
			if (paginaDAO.salva(pagina)) {
				contador++;
			} else {
				logger.error("Erro ao tentar salvar a página " + i);
				break;
			}
		}
		return contador;
	}

	public static void main(String[] args) {
		DocumentoBO documentoBO = new DocumentoBO();
		documentoBO.varreDiretorio("/home/marilia");
		 //documentoBO.reindexaBaseInteira();
		System.out.println("Terminado");
	}
}
