package br.edu.ifms.pibic.util;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * 
 * @author marilia
 *
 */
public class OfficeUtil {
	
	/**
	 * Método para extrair texto docx
	 * @param caminho
	 * @return
	 */
	public static String pegaTextoWordDOCX(String caminho){
		 XWPFDocument docx = null;
		try {
			docx = new XWPFDocument(new FileInputStream(caminho));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				   
				   XWPFWordExtractor extrai = new XWPFWordExtractor(docx);
				   return extrai.getText();
	}
	
	/**
	 *  Método para extrair texto xls
	 * @param caminho
	 * @return
	 * @throws IOException
	 */
	public static String pegaTextoWordXLS(String caminho) throws IOException{
		 
		FileInputStream in=new FileInputStream(caminho);
		  HSSFWorkbook wb=new HSSFWorkbook(in);
		  ExcelExtractor extractor=new ExcelExtractor(wb);
		  extractor.setFormulasNotResults(true);
		  extractor.setIncludeSheetNames(true);		  
		  String text=extractor.getText();
		  return text;
	}


}
