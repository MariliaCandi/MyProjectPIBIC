package br.edu.ifms.pibic.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


/**
 * 
 * @author marilia
 *
 */
public class OfficeUtil {
	
	private OfficeUtil(){
		
	}
	
	/**
	 * Método para extrair texto docx
	 * @param caminho
	 * @return
	 */
	public static String pegaTextoWordDOCX(File caminho){		
		try {					   
			XWPFWordExtractor extrai = new XWPFWordExtractor(new XWPFDocument(new FileInputStream(caminho)));			
			return extrai.getText();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return "";			
	}
	
	
	
	/**
	 * Método para extrair texto doc
	 * @param caminho
	 * @return
	 */
	public static String pegaTextoWordDOC(File caminho){		
		try {			
			HWPFDocument doc = new HWPFDocument(new POIFSFileSystem(new FileInputStream(caminho)));				
			WordExtractor we=new WordExtractor(doc);			
			return we.getText();
			
		}catch (  Exception e) {
		    e.printStackTrace();
		}
		return "";
	}
	

	
	
	/**
	 *  Método para extrair texto xls
	 * @param caminho
	 * @return
	 * @throws IOException
	 */
	public static String pegaTextoExcelXLS(File caminho) {
		
		try{			  
			  ExcelExtractor extractor=new ExcelExtractor(new HSSFWorkbook(new FileInputStream(caminho)));
			  extractor.setFormulasNotResults(true);
			  extractor.setIncludeSheetNames(true);		  
			  return extractor.getText();			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";	
		
	}
	
	/**
	 * Método para extrair texto xlsx
	 * @param caminho
	 * @return
	 */

public static String pegaTextoExcelXLSX(File caminho){

		try{			
			XSSFExcelExtractor documento = new XSSFExcelExtractor(new XSSFWorkbook(new FileInputStream(caminho)));			
			return documento.getText();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

public static void main(String[] args) {
	File file = new File("/home/marilia/documentos para teste/texto Para Teste.docx");
	System.out.println(pegaTextoWordDOCX(file));
}

}
