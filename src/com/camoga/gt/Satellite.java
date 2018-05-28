package com.camoga.gt;

import static java.lang.Math.*;

import com.camoga.newton.Newton;
import com.camoga.newton.Newton.Function;

public class Satellite {
	
	/**
	 * e = eccentricity
	 * a = semi-major axis
	 */
	public double e, a;
	
	/**
	 * i = inclination
	 * Ω = RAAN
	 */
	public double i, Ω;
	
	/**
	 * ω = argument of periapsis
	 * ν = true anomaly
	 */
	public double ω, ν;
	
	public double n;
	
	public static final double G = 6.67408*Math.pow(10, -11);
	public static final double mu = G*5.9721e24;
	
	public Satellite(double excentricity, double a, double inclination, double RAAN, double AoPeri, double trueAnomaly) {
		this.e = excentricity;
		setA(a);
		this.i = inclination;
		this.Ω = RAAN;
		this.ω = AoPeri;
		this.ν = trueAnomaly;
	}
	
	public void setA(double a) {
		this.a = a;
		n = sqrt(mu/(a*a*a));
	}
	
	public double EtoM(double E) {
		return E- e*sin(E);
	}
	
	public double MtoE(double M) {
		double E = Newton.solve(1, new Function() {
			
			public double compute(double E) {
				return E - e*sin(E)-M;
			}
		}, new Function() {
			public double compute(double E) {
				return 1 -e*cos(E);
			}
		}, 1e-4);
		return E;
	}
	
	public double EtoT(double E) {
		return 2*atan2(sqrt((1+e)/(1-e))*tan(E/2), 1);
	}
	
	public double TtoE(double T) {
		return atan2(sqrt(1-e*e)*sin(T), e+cos(T));
	}
	
	public double MtoT(double M) {
		return EtoT(MtoE(M));
	}
	
	public double TtoM(double T) {
		return EtoM(TtoE(T));
	}
	
	public double getMo() {
		return TtoM(ν);
	}
}