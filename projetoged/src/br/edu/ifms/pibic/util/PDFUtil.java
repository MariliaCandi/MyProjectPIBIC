package br.edu.ifms.pibic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;

public class PDFUtil {

	private PDFUtil() {
	}

	public static File juntaArquivos(List<File> arquivos,
			String nomeArquivoDestino) {
		if (arquivos != null && arquivos.size() > 0) {
			try {
				List<InputStream> listaIS = new ArrayList<InputStream>();

				for (File arquivo : arquivos) {
					listaIS.add(new FileInputStream(arquivo));
				}
				File arquivoDestino = new File(nomeArquivoDestino);
				OutputStream os = new FileOutputStream(arquivoDestino);
				juntaArquivos(listaIS, os);
				return arquivoDestino;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static void juntaArquivos(List<InputStream> arquivos,
			OutputStream outputStream) {
		try {
			if (arquivos != null && arquivos.size() > 0) {
				Document document = new Document();
				PdfWriter writer = PdfWriter
						.getInstance(document, outputStream);
				document.open();
				PdfContentByte cb = writer.getDirectContent();

				for (InputStream in : arquivos) {
					PdfReader reader = new PdfReader(in);
					for (int i = 1; i <= reader.getNumberOfPages(); i++) {
						document.newPage();
						// import the page from source pdf
						PdfImportedPage page = writer
								.getImportedPage(reader, i);
						// add the page to the destination pdf
						cb.addTemplate(page, 0, 0);
					}
				}

				outputStream.flush();
				document.close();
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Integer pegaNumeroPaginas(File arquivo) {
		try {
			PdfReader leitorPDF = new PdfReader(arquivo.getPath());
			return leitorPDF.getNumberOfPages();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static File pegaPaginaDocumento(File arquivo, Integer numeroPagina) {
		try {
			InputStream inputStream = new FileInputStream(arquivo);
			File arquivoPagina = File.createTempFile(arquivo.getName()
					.toLowerCase().split(".pdf")[0]
					+ "_pagina" + numeroPagina + ".pdf", "");
			OutputStream outputStream = new FileOutputStream(arquivoPagina);
			pegaParteDoDocumento(inputStream, outputStream, numeroPagina,
					numeroPagina);
			return arquivoPagina;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String pegaTextoPaginaPDF(File arquivo, Integer numeroPagina) {
		try {
			PdfReader leitorPDF = new PdfReader(arquivo.getPath());
			String conteudoPagina = PdfTextExtractor.getTextFromPage(leitorPDF,
					numeroPagina, new SimpleTextExtractionStrategy());
			byte[] bytesTexto = conteudoPagina.getBytes("UTF-8");
			for (int i = 0; i < bytesTexto.length; i++) {
				if (bytesTexto[i] == 0x00) {
					bytesTexto[i] = 95;
				}
			}
			conteudoPagina = new String(bytesTexto);
			return conteudoPagina;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String pegaTextoPDF(File arquivo) {
		try {
			String conteudoTextual = "";
			PdfReader leitorPDF = new PdfReader(arquivo.getPath());
			for (int i = 1; i <= leitorPDF.getNumberOfPages(); i++) {
				conteudoTextual += pegaTextoPaginaPDF(arquivo, i);
			}
			return conteudoTextual;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static void pegaParteDoDocumento(InputStream inputStream,
			OutputStream outputStream, int paginaInicial, int paginaFinal) {
		Document document = new Document();
		try {
			PdfReader inputPDF = new PdfReader(inputStream);

			Rectangle dimensoes = inputPDF.getPageSize(paginaInicial);
			if (dimensoes != null) {
				float altura = dimensoes.getHeight();
				float largura = dimensoes.getWidth();
				if (altura < largura) {
					document = new Document(PageSize.LETTER.rotate());
				}
			}

			int totalPaginas = inputPDF.getNumberOfPages();

			// make fromPage equals to toPage if it is greater
			if (paginaInicial > paginaFinal) {
				paginaInicial = paginaFinal;
			}
			if (paginaFinal > totalPaginas) {
				paginaFinal = totalPaginas;
			}

			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data
			PdfImportedPage page;

			while (paginaInicial <= paginaFinal) {
				document.newPage();
				page = writer.getImportedPage(inputPDF, paginaInicial);
				cb.addTemplate(page, 0, 0);
				paginaInicial++;
			}
			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
