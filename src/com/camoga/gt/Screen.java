package com.camoga.gt;

import static java.lang.Math.PI;

public class Screen {
	
	private int width, height;
	public int[] pixels;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
	}
	
	public void clear() {
		pixels = new int[width*height];
	}
	
	public void drawPoint(double longitude, double latitude, int radius, int color) {
//		longitude = longitude%(PI);
//		latitude = latitude%(PI/2);
//		System.out.println("render "+"lat: " + latitude+", long: " + longitude);
		if(Math.abs(latitude) == PI/2) return;
		for(int y = -radius; y < radius; y++) {
			int dy = (int) (height/2+y - (latitude/PI*height))%height;
			for(int x = -radius; x < radius; x++) {
				int dx = (int) (width/2 + x + (longitude/(2*PI)*width))%width;
				if(latitude == Math.PI/2) dx = width/2+x;
//				System.out.println("dx = " + dx + ", dy = " + dy);

				while(dx < 0) dx += width;
				while(dy < 0) dy += height;
				if(dy < 0 || dx >= this.width || dy >= this.height) continue;
				pixels[dx + dy*width] = color;
				
			}
		}
	}
}