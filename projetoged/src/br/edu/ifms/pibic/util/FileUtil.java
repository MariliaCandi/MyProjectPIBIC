package br.edu.ifms.pibic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class FileUtil {

	private FileUtil() {
	}

	public static byte[] fromFileToByteArray(File file) {
		try {
			byte[] bFile = new byte[(int) file.length()];
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
			return bFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static File fromByteArrayToTempFile(byte[] byteArray, String fileName) {
		try {
			File file = File.createTempFile(fileName, "");
			FileOutputStream fileOuputStream = new FileOutputStream(file);
			fileOuputStream.write(byteArray);
			fileOuputStream.close();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File fromByteArrayToFile(byte[] byteArray, String fileName) {
		try {
			File file = fileName != null ? new File(fileName) : File
					.createTempFile("temp" + System.currentTimeMillis(), "");
			FileOutputStream fileOuputStream = new FileOutputStream(file);
			fileOuputStream.write(byteArray);
			fileOuputStream.close();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
