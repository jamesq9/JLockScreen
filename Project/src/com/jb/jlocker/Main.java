package com.jb.jlocker;

public class Main {

	
	public static void main(String[] args) throws Exception {
		
		//JLockScreen jls = new JLockScreen();
		
		
		if(args.length == 1) {
			
			// generate public and private Key...
			RSAHelper.RSAKeyPairGenerator();
			RSAHelper.writeToFile(JLockScreen.PUBLIC_KEY_FILE_NAME, RSAHelper.getPublicKey().getEncoded());
			RSAHelper.writeToFile(JLockScreen.PRIVATE_KEY_FILE_NAME, RSAHelper.getPrivateKey().getEncoded());
			
		} else {
			
			// start the lock screen
			new JLockScreen();
		}
		
		
		
	}
	
}
