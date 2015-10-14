package com.example.signaturelockscreen.util;

import java.util.Random;

public class MyNetwork {

	public final static double NEURON_ON = 0.9;
	public final static double NEURON_OFF = 0.1;
	protected int INPUT_NEURAL_COUNT = 30;
	protected int OUTPUT_NEURAL_COUNT = 74;
	protected double output[];

	protected double totalError;
	protected int inputNeuronCount;
	protected int outputNeuronCount;
	protected Random random = new Random(System.currentTimeMillis());

	/**
	 * Tính độ dài vector
	 * 
	 */
	static double vectorLength(double v[]) {
		double rtn = 0.0;
		for (int i = 0; i < v.length; i++)
			rtn += v[i] * v[i];
		return rtn;
	}

	/**
	 * Tính toán tích vô hướng của 2 vector
	 */

	double dotProduct(double vec1[], double vec2[]) {
		int k, v;
		double rtn;

		rtn = 0.0;
		k = vec1.length;
		
		v = 0;
		while ((k--) > 0) {
			rtn += vec1[v] * vec2[v];
			v++;
		}

		return rtn;
	}
	/**
	   * Called to randomize weights.
	   * 
	   * @param weight A weight matrix.
	   */
	  void randomizeWeights( double weight[][] )
	  {
	    double r ;
	    int temp = (int)(3.464101615 / (2. * Math.random() )); 

	    for ( int y=0;y<weight.length;y++ ) {
	      for ( int x=0;x<weight[0].length;x++ ) {
	        r = (double) random.nextInt(Integer.MAX_VALUE) + (double) random.nextInt(Integer.MAX_VALUE) -
	            (double) random.nextInt(Integer.MAX_VALUE) - (double) random.nextInt(Integer.MAX_VALUE) ;
	        weight[y][x] = temp * r ;
	      }
	    }
	  }
}
