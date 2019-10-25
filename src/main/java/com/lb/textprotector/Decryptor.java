package com.lb.textprotector;

import org.apache.commons.codec.binary.Base64InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.util.zip.InflaterInputStream;

public class Decryptor extends Cryptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(Decryptor.class);

	@Override
	public void crypt() {
		try (InputStream fileInputStream = new BufferedInputStream(new FileInputStream(getInputFileName()));
				 Base64InputStream base64InputStream = new Base64InputStream(fileInputStream);
				 CipherInputStream cipherInputStream = new CipherInputStream(base64InputStream, getAESCipher());
				 InputStream inputStream = new InflaterInputStream(cipherInputStream);
				 OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(getOutputFileName())))	{
			inputStream.transferTo(outputStream);
		} catch (FileNotFoundException e) {
			LOGGER.error("Compression", e);
		} catch (IOException e) {
			LOGGER.error("Compression", e);
		}
	}
	
	@Override
	public int getOpmode() {
		return Cipher.DECRYPT_MODE;
	}
}
