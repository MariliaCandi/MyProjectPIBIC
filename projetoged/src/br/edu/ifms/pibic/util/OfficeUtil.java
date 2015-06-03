package br.edu.ifms.pibic.util;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
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
	 * Método para extrair texto doc
	 * @param caminho
	 * @return
	 */
	public static String pegaTextoWordDOC(String caminho){

		POIFSFileSystem pOIFSFileSystem=null;		
		try {
			pOIFSFileSystem = new POIFSFileSystem(new FileInputStream(caminho));
			HWPFDocument doc=new HWPFDocument(pOIFSFileSystem);
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
	public static String pegaTextoExcelXLS(String caminho) {
		
		try{
			FileInputStream in=new FileInputStream(caminho);
			  HSSFWorkbook wb=new HSSFWorkbook(in);
			  ExcelExtractor extractor=new ExcelExtractor(wb);
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

public static String pegaTextoExcelXLSX(String caminho){

		try{
			OPCPackage oPCPackage = OPCPackage.open(caminho);
			XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(oPCPackage);
			XSSFExcelExtractor documento = new XSSFExcelExtractor(xSSFWorkbook);			
			return documento.getText();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
}