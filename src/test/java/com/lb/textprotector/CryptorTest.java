package com.lb.textprotector;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CryptorTest {
	private static final int TEMP_FILE_SIZE = 1000;
	private static File inputTempFile;
	private static File encryptedTempFile;
	private static File decryptedTempFile;
	private static final String KEY = "MyKey";
	
	@DisplayName("Create input file")
	@BeforeAll
	public static void init() throws IOException {
		inputTempFile = File.createTempFile("crypt-", ".input");
		
		try (DataOutputStream stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(inputTempFile)))) {
			for (int i = 0; i < TEMP_FILE_SIZE; i++)
				stream.writeLong(Math.round(Math.random()));
		}
	}
	
	@DisplayName("Encrypt file")
	@Test
	@Order(1)
	void enCrypt() throws IOException {
		encryptedTempFile = File.createTempFile("crypt-", ".encrypted");
		System.out.println(encryptedTempFile.toString());
		new Encryptor().setInputFileName(inputTempFile.toString()).setOutputFileName(encryptedTempFile.toString()).setKey(KEY).crypt();
	}
	
	@DisplayName("Decrypt file")
	@Test
	@Order(2)
	void deCrypt() throws IOException {
		decryptedTempFile = File.createTempFile("crypt-", ".decrypted");
		new Decryptor().setInputFileName(encryptedTempFile.toString()).setOutputFileName(decryptedTempFile.toString()).setKey(KEY).crypt();
	}
	
	@DisplayName("Compare input and decrypted files")
	@Test
	@Order(3)
	void compare() throws IOException {
		assertTrue(FileUtils.contentEquals(inputTempFile, decryptedTempFile));
	}
	
	@DisplayName("Delete temp files")
	@AfterAll
	public static void close() throws IOException {
		Files.deleteIfExists(inputTempFile.toPath());
		Files.deleteIfExists(encryptedTempFile.toPath());
		Files.deleteIfExists(decryptedTempFile.toPath());
	}
}
