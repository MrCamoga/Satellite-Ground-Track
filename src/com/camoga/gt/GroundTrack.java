package com.camoga.gt;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.camoga.matrix.Matrix;

public class GroundTrack extends Canvas {
	
	private static final long serialVersionUID = 1L;

	private int WIDTH =  1400,  HEIGHT = WIDTH/2;
	
	private BufferedImage background;
	private BufferedImage raster = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private int[] pixels = ((DataBufferInt) raster.getRaster().getDataBuffer()).getData();
	
	private Screen screen;

	private static final double siderealday = 86164.0905;
	
	public GroundTrack() {
		JFrame f = new JFrame("Satellite Ground Track - by MrCamoga");
		f.add(this);
		f.setSize(WIDTH, HEIGHT);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setResizable(true);
		
		new Thread(() -> run()).start();
		init();
		
		
//		JPanel panel = new JPanel(new GridLayout(1, 1));
//		panel.add(new JLabel("Semi major axis"));
//		
//		JSlider slider = new JSlider(7000000, 405000000, (int) s.a);
//		slider.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				s.setA((double)((JSlider)e.getSource()).getValue());
//				System.out.println(s.a);
//				screen.clear();
//				renderSatellite(s, siderealday, 0xffffff00);
//			}
//		});
//		panel.add(slider);
//		
//		f.add(panel, BorderLayout.NORTH);
		
		Satellite iss = new Satellite(0, 400000+6.371e6, 51.64*Math.PI/180, 0, 0, 0);
		renderSatellite(iss, siderealday, 0xa0ff00ff);
		Satellite s2 = new Satellite(
				0.1,
		Math.cbrt(Math.pow((siderealday/1),2)*Satellite.mu/(4*Math.PI*Math.PI)), 
				Math.PI/3.8,
				-Math.PI/2, 
				Math.PI*1.2, 
				Math.PI);
		renderSatellite(s2, siderealday*1, 0xff00ffff);
		
		Satellite s = new Satellite(
				0.74,
		Math.cbrt(Math.pow((siderealday/2),2)*Satellite.mu/(4*Math.PI*Math.PI)), 
				Math.PI*7/180,
				-Math.PI/2, 
				Math.PI*1, 
				Math.PI);
		renderSatellite(s, siderealday*1, 0xffffff00);
	}
	
	private void run() {
		
		while(true) {
			render();
			try {
				Thread.sleep(31);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void init() {
		Satellite s = new Satellite(0.6, 0, 0, 0, 0, 0);
//		System.out.println(s.MtoE(0.3));
		screen = new Screen(WIDTH, HEIGHT);
		try {
			background = ImageIO.read(getClass().getResourceAsStream("/earth.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		Satellite tundra = new Satellite(0.3, 22849.6391e3, 63.4*Math.PI/180, 0, Math.PI*3/2, Math.PI/2);;
//		renderSatellite(new Satellite(0, 42164014, 80*Math.PI/180, 0, 0, 0), siderealday, 0xffff0000);
//		renderSatellite(new Satellite(0, 42164000, 26.8*Math.PI/180, 0, 0, 0), 24000, 0xffff0000);
		
//		renderSatellite(tundra, siderealday, 0xffff0000);
		
	}
	
	public void renderSatellite(Satellite s, double timespan, int color) {
		double dt = 1;
		double t0 = 0;
		double Mo = s.getMo();
		for(int i = 0; i < timespan/dt; i++) {
			double t = dt*i+t0;
//			if(dt*i > 100) break;
			double Mn = (Mo + s.n*(t))%(2*Math.PI);
//			System.out.println(Mn);
//			System.out.println(s.EtoT(0));
			double TA = s.MtoT(Mn);
//			System.out.println(TA);
//		screen.drawPoint(longitude, latitude, radius);
			Matrix vec = new Matrix(new double[][] {{1},{0},{0}});
			vec=new Matrix(new double[][] {
				{Math.cos(TA+s.ω),-Math.sin(TA+s.ω),0},
				{Math.sin(TA+s.ω),Math.cos(TA+s.ω),0},
				{0,0,1}
			}).multiply(new double[][] {
				{1,0,0},
				{0, Math.cos(s.i),-Math.sin(s.i)},
				{0, Math.sin(s.i),Math.cos(s.i)},
			}).multiply(new double[][] {
				{Math.cos(s.Ω),-Math.sin(s.Ω),0},
				{Math.sin(s.Ω),Math.cos(s.Ω),0},
				{0,0,1}
			});
			double latitude = Math.atan2(vec.matrix[0][2], vec.matrix[2][2]);
			double longitude = -Math.atan2(vec.matrix[0][1], vec.matrix[0][0])- t/siderealday*2*Math.PI;
//			System.out.println("lat: " + latitude+", long: " + longitude);
			screen.drawPoint(longitude, latitude, 1, color);
//			System.out.println(vec);
		}
	}
	
	private int timer = 0;
	
	public void render() {
		timer++;
		BufferStrategy buffer = getBufferStrategy();
		if(buffer==null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = buffer.getDrawGraphics();
		
//		screen.clear();
		
//		System.out.println("eccentricity: "+ ((double)timer/200));
//		Satellite tundra = new Satellite(0.2684,  32164000, 89*Math.PI/180, Math.PI/4, Math.PI*3/2, 96*Math.PI/180);
//		renderSatellite(tundra);
//		System.out.println((double)timer/100);
//		screen.clear();
//		screen.drawPoint(Math.PI/2,Math.PI/2*(timer%101)/100,5);
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i]; 
		}
		
		g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
		g.drawImage(raster, 0, 0, null);
		
		g.dispose();
		buffer.show();
	}
	
	public static void main(String[] args) {
		new GroundTrack();
	}
}