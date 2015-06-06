package br.edu.ifms.pibic.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class CompressionUtil {

	private CompressionUtil() {
	}

	/**
	 * Comprime uma string utilizando o GZIP
	 * 
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static byte[] gzipCompress(String str, String encoding) {
		try {
			if (str == null || str.length() == 0) {
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			Writer writer = new OutputStreamWriter(gzip, encoding);
			writer.write(str);
			writer.close();
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Descomprime uma string utilizando o GZIP
	 * 
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String gzipDecompress(byte[] bytes, String encoding) {
		String decompressedString = "";

		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			GZIPInputStream gzip = new GZIPInputStream(bais);
			Reader reader = new InputStreamReader(gzip, encoding);
			StringBuffer sbuf = new StringBuffer();
			char[] buffer = new char[32 * 1024];
			int nread;
			while ((nread = reader.read(buffer)) >= 0) {
				sbuf.append(buffer, 0, nread);
			}
			decompressedString = sbuf.toString();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return decompressedString;
	}

	/**
	 * Comprime uma string utilizando o GZIP
	 * 
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String gzipCompressString(String str, String encoding) {
		try {
			if (str == null || str.length() == 0) {
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			Writer writer = new OutputStreamWriter(gzip, encoding);
			writer.write(str);
			writer.close();
			byte[] compressedBytes = out.toByteArray();
			return new String(Base64.encodeBase64(compressedBytes), encoding);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Descomprime uma string utilizando o GZIP
	 * 
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String gzipDecompressString(String str, String encoding) {
		String decompressedString = "";

		try {
			byte[] bytes = Base64.decodeBase64(str.getBytes(encoding));
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			GZIPInputStream gzip = new GZIPInputStream(bais);
			Reader reader = new InputStreamReader(gzip, encoding);
			StringBuffer sbuf = new StringBuffer();
			char[] buffer = new char[32 * 1024];
			int nread;
			while ((nread = reader.read(buffer)) >= 0) {
				sbuf.append(buffer, 0, nread);
			}
			decompressedString = sbuf.toString();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return decompressedString;
	}

	public static String compressString(String str, String encoding) {
		String compressedString = "";

		try {
			byte[] input = str.getBytes(encoding);
			Deflater d = new Deflater();
			d.setLevel(Deflater.BEST_COMPRESSION);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DeflaterOutputStream dout = new DeflaterOutputStream(out, d);
			dout.write(input);
			dout.close();
			compressedString = new String(Hex.encodeHex(out.toByteArray()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return compressedString;
	}

	public static String decompressString(String str, String encoding) {
		String decompressedString = "";

		try {
			byte[] input = Hex.decodeHex(str.toCharArray());
			ByteArrayInputStream in = new ByteArrayInputStream(input);
			InflaterInputStream inputStream = new InflaterInputStream(in);
			ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
			int b;
			while ((b = inputStream.read()) != -1) {
				bout.write(b);
			}
			in.close();
			bout.close();
			decompressedString = new String(bout.toByteArray(), encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return decompressedString;
	}

	/**
	 * Comprime um conjunto de arquivos com o padrão ZIP
	 * 
	 * @param fileName
	 */
	public static void zipFiles(String root, String[] fileNames,
			String destinyFileName) {
		byte[] buf = new byte[1024];

		try {
			String destinyFilePath = root + destinyFileName;
			File existingFile = new File(destinyFilePath);
			if (existingFile.exists() && !existingFile.isDirectory()
					&& existingFile.canWrite()) {
				existingFile.delete();
			}

			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					destinyFilePath));

			for (String fileName : fileNames) {
				String name = new File(fileName).getName();
				FileInputStream in = new FileInputStream(fileName);
				out.putNextEntry(new ZipEntry(name));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String generateMd5Hash(String str) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(str.getBytes("UTF8"));
			byte s[] = m.digest();
			String result = "";
			for (int i = 0; i < s.length; i++) {
				result += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00)
						.substring(6);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}