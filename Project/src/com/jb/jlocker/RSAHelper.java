package com.jb.jlocker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSAHelper {

	private static PrivateKey privateKey;
	private static PublicKey publicKey;

	public static void RSAKeyPairGenerator() throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair pair = keyGen.generateKeyPair();
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();
	}

	public static void writeToFile(String path, byte[] key) throws IOException {
		File f = new File(path);

		Base64.Encoder encoder = Base64.getEncoder();
		String keyStr = encoder.encodeToString(key);

		FileOutputStream fos = new FileOutputStream(f);
		fos.write(keyStr.getBytes());
		fos.flush();
		fos.close();
	}

	public static PrivateKey getPrivateKey() {
		return privateKey;
	}

	public static PublicKey getPublicKey() {
		return publicKey;
	}

	public static PublicKey getPublicKey(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
		String key = new String(keyBytes);
		byte[] actualKeyBytes = Base64.getDecoder().decode(key);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(actualKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	public static byte[] encrypt(String data, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data.getBytes());
	}

	public static PrivateKey getPrivateKey(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
		String key = new String(keyBytes);
		byte[] actualKeyBytes = Base64.getDecoder().decode(key);

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(actualKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	public static String decrypt(byte[] data, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(data));
	}

}
