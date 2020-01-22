package com.jb.jlocker;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;

// Java based approach for a custom lock screen
public class JLockScreen implements Runnable {

	
	private GraphicsDevice[] screenDevices;
	private ArrayList<SecurityFrame> securityFrames;
	private Robot robot;
	public static boolean killing_task = false;
	private static final String TEST_STRING = "Sixty9WeeksX7Years!";
	private boolean isLocked = false;
	private PublicKey publicKey = null;
	public static final String PUBLIC_KEY_FILE_NAME = ".jlock";
	public static final String PRIVATE_KEY_FILE_NAME = ".junlock";
	private static String unlock_file_path = null;

	
	public JLockScreen() {
		begin();
	}
	
	public void initData() throws Exception {
		
		
		screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		
		if(securityFrames == null || securityFrames.size() != screenDevices.length) {
		
			screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
			
			securityFrames = new ArrayList<SecurityFrame>();
			
			robot = new Robot();
			
			// for each screen create a security frame
			for(int i=0;i<screenDevices.length; i++) {
				
				GraphicsDevice screen = screenDevices[i];
				SecurityFrame frame = new SecurityFrame(screen);
				
				securityFrames.add(frame);
				
			}
		
//        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
//
//            @Override
//            public boolean dispatchKeyEvent(KeyEvent ke) {
//                synchronized (this) {
//                    
//                	System.out.println(ke.getKeyChar());
//                	
//                    return false;
//                }
//            }
//        });
		}
		
	}
	
	public void lockScreen() {
		
		Iterator<SecurityFrame> itr = securityFrames.iterator();
		
		boolean isAtleastOneScreenActive = false;
		while(itr.hasNext()) {
			if(itr.next().isActive()) {
				isAtleastOneScreenActive = true;
			}
		}
		//System.out.println("isAtleastOneScreenActive: " + isAtleastOneScreenActive);
		itr = securityFrames.iterator();
		while(itr.hasNext()) {
			SecurityFrame frame = itr.next();
			if(!isAtleastOneScreenActive) {
				
				frame.reCreate();
				
			}
			frame.occupyScreen();
			
		}
		
	}
	
	public void unLockScreen()  {
		
		
		Iterator<SecurityFrame> itr = securityFrames.iterator();
		while(itr.hasNext()) {
			itr.next().dispose();
		}
		
		//startTask("explorer.exe");
	}


	private void startTask(String task) {
		try {
			Runtime.getRuntime().exec(task).waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			
		} 
	}

	@Override
	public void run() {
		
		try {
			
			int secInterval = 0;
			int lockInterval = 10;
			while(true) {
				
				Thread.sleep(20);
				
				// get File Systems
				File[] paths;
				paths = File.listRoots();
				boolean unlock = false;
				
				
				if(unlock_file_path != null && new File(unlock_file_path).exists()) {
				
					unlock = true;
				
				} else {
					unlock_file_path = null;
					for(File path:paths)
					{
					    if(new File(path+PRIVATE_KEY_FILE_NAME).exists()) {
					    	
					    	PrivateKey privatekey = RSAHelper.getPrivateKey(path+PRIVATE_KEY_FILE_NAME);
					    	
					    	
					    	String message = RSAHelper.decrypt(RSAHelper.encrypt(TEST_STRING, publicKey), privatekey);
					    	
					    	if(message.equals(TEST_STRING)) {
					    		unlock=true;
					    		unlock_file_path = path+PRIVATE_KEY_FILE_NAME;
						    	break;
					    	}
					    	
					    	
					    }
					    
					}
					
				}
				
				if(unlock && isLocked) {
					unLockScreen();
					isLocked = false;
					secInterval=0;
				}  else if(!unlock) {
					
					initData();
					
					
					
					
					if(secInterval%lockInterval == 0) {
						//taskkill("taskmgr.exe");
						lockScreen();
						unlock_file_path = null;
						//taskkill("explorer.exe");
					}
					secInterval = (secInterval + 1) % lockInterval;
//					if(oneSecInterval%400 == 0) {
//						unLockScreen();
//						return;
//					}
					isLocked = true;
					robot.keyRelease(KeyEvent.VK_ALT);
					robot.keyRelease(KeyEvent.VK_TAB);
					robot.keyRelease(KeyEvent.VK_WINDOWS);
					robot.keyRelease(KeyEvent.VK_CONTROL);
					robot.keyRelease(KeyEvent.VK_DELETE);
					robot.keyRelease(KeyEvent.VK_SHIFT);
					robot.keyRelease(KeyEvent.VK_ENTER);
					robot.mouseMove(0, 0);
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			unLockScreen();
		}
		
	}



	private void taskkill(String task) {
		  
		  if(!killing_task) {
		   
			  killing_task = true;
			  
			  new Thread()
			  {
			      public void run() {
			    	  try {
					      Runtime.getRuntime().exec("taskkill /F /IM " + task).waitFor();
					    } catch (Exception e) {
					    	e.printStackTrace();
					    } finally {
					    	JLockScreen.killing_task = false;
					    }
			      }
			  }.start();
			  
		  }
	  }
	
	public void begin() {
		
		
		try {
			
			publicKey = RSAHelper.getPublicKey(PUBLIC_KEY_FILE_NAME);
			
			Thread t = new Thread(this, "JLockScreenThread");
			t.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	
}
