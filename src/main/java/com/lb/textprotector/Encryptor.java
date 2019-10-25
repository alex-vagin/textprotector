package com.lb.textprotector;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class Encryptor extends Cryptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(Encryptor.class);

	@Override
	public void crypt() {
		try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(getInputFileName()));
				 OutputStream bufferedFileOutputStream = new BufferedOutputStream(new FileOutputStream(getOutputFileName()));
				 Base64OutputStream base64OutputStream = new Base64OutputStream(bufferedFileOutputStream);
				 CipherOutputStream cipherOutputStream = new CipherOutputStream(base64OutputStream, getAESCipher());
				 OutputStream outputStream = new DeflaterOutputStream(cipherOutputStream, new Deflater(Deflater.BEST_COMPRESSION)))	{
			inputStream.transferTo(outputStream);
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found", e);
		} catch (IOException e) {
			LOGGER.error("Something went wrong", e);
		}
	}
	
	@Override
	public int getOpmode() {
		return Cipher.ENCRYPT_MODE;
	}
}
