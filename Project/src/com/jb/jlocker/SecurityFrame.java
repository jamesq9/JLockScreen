package com.jb.jlocker;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SecurityFrame {

	
	private JFrame frame;
	private GraphicsDevice occupiedScreen;
	private static String font = "Cambria";
	private static String centerText = "Enter key to unlock";
	private static String centerSubText = "Java based Lock Screen by james ballari";
	private static int fontSizeMetric = 15;
	private static int fsm2 = 4;
	
	
	public SecurityFrame() {
		
		occupiedScreen = null;
		createNewFrame();
	    
	}
	
	
	public SecurityFrame(GraphicsDevice screen) {
		occupiedScreen = screen;
		createNewFrame();
	}


	private void createNewFrame() {
		frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//frame.setSize(1200, 1200);
		frame.setUndecorated(true);
		//frame.setLayout(new BorderLayout());
	    frame.getContentPane().add(new JPanel() {
	    	
	    	private static final long serialVersionUID = -8034430144118136385L;

			@Override 
	    	public void paint(Graphics g) {
	    		super.paint(g);
	    		//int w = occupiedScreen.getFullScreenWindow().getWidth();
	    		//int h = occupiedScreen.getFullScreenWindow().getHeight();
	    		
	    		int w = getWidth();
	    		
	    		g.setColor(new Color(124,185,232));
	    		g.fillRect(0, 0, getWidth(), getHeight());
	    		
	    		g.setColor(new Color(254,253,255));
	    		
	    		g.setFont(new Font(font, Font.BOLD, w/fontSizeMetric));
	    		FontMetrics fm = g.getFontMetrics( new Font(font,Font.BOLD,w/fontSizeMetric));
	    		int stringWidth = fm.stringWidth(centerText);
	            int stringAccent = fm.getAscent();
	            int xCoordinate = getWidth() / 2 - stringWidth / 2;
	            int yCoordinate = getHeight() / 2 - stringAccent / 2;
	            g.drawString(centerText, xCoordinate , yCoordinate);
	    		
	    		g.setFont(new Font(font, Font.BOLD, w/(fontSizeMetric*fsm2)));
	    		fm = g.getFontMetrics( new Font(font,Font.BOLD,w/(fontSizeMetric*fsm2)));
	    		int stringWidth2 = fm.stringWidth(centerSubText);
	            int stringAccent2 = fm.getAscent();
	            g.drawString(centerSubText, xCoordinate + stringWidth2/2 ,yCoordinate+stringAccent2+50 );
	    	    
	    	    
	    	}
	    	 
	    });
	    frame.setAlwaysOnTop(true);
	    frame.setDefaultCloseOperation(0);
	    frame.pack();
		
	}


	public SecurityFrame setVisible(boolean value) {
		frame.setVisible(value);	
		return this;
	}
	
	public SecurityFrame dispose() {
		frame.setVisible(false);
		frame.dispose();
		return this;
	}
	
	public SecurityFrame occupyScreen() {
		
		
		
		frame.toFront();
		frame.requestFocusInWindow();
		frame.requestFocus();
	    frame.setAlwaysOnTop(true);
	    frame.setDefaultCloseOperation(0);
	    frame.setAutoRequestFocus(true);
	    frame.setVisible(true);
		occupiedScreen.setFullScreenWindow(frame);
		//System.out.println("occupyScreen");
		return this;
	}
	
	public SecurityFrame setScreen(GraphicsDevice screen) {
		occupiedScreen = screen;
		return this;
	}


	public SecurityFrame hide() {
		frame.setVisible(false);
		return this;
	}

	public boolean isActive() {
		System.out.println(occupiedScreen.getIDstring() + "  is Active : " + frame.isActive());
		return frame.isActive() && frame.isFocused();
	}
	
	
	public SecurityFrame reCreate() {
		frame.setVisible(false);
		frame.dispose();
		createNewFrame();
		return this;
	}
	

	
}
