package com.lb.textprotector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public abstract class Cryptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(Cryptor.class);
	
	private String inputFileName;
	private String outputFileName;
	protected String key;

	public abstract void crypt();
	public abstract int getOpmode();
	
	SecretKeySpec getAESKey() {
		byte result[] = new byte[16];
		byte keyBytes[] = key.getBytes(Charset.forName(StandardCharsets.UTF_8.name()));
		for (int idx = 0; idx < result.length; idx++)
			result[idx] = keyBytes[idx % keyBytes.length];
		
		return new SecretKeySpec(result, "AES");
	}
	
	Cipher getAESCipher() {
		try {
			Cipher result = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec secretKeySpec = getAESKey();
			result.init(getOpmode(), secretKeySpec, new IvParameterSpec(new byte[16]));
			return result;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
			LOGGER.error("getAESCipher", e);
		}
		return null;
	}

	// getters and setters
	public String getInputFileName() {return inputFileName;}
	public Cryptor setInputFileName(String inputFileName) {this.inputFileName = inputFileName; return this;}
	public String getOutputFileName() {return outputFileName;}
	public Cryptor setOutputFileName(String outputFileName) {this.outputFileName = outputFileName; return this;}
	public String getKey() {return key;}
	public Cryptor setKey(String key) {this.key = key; return this;}
}
