package com.example.signaturelockscreen.util;

public class TrainingSet {

	protected int inputCount;
	protected int outputCount;
	protected double input[][];
	protected double output[][];
	protected int trainingSetCount;

	public TrainingSet(int inputCount, int outputCount) {
		this.inputCount = inputCount;
		this.outputCount = outputCount;
		trainingSetCount = 0;
	}

	public int getInputCount() {
		return inputCount;
	}

	public int getOutputCount() {
		return outputCount;
	}

	public void setTrainingSetCount(int trainingSetCount) {
		this.trainingSetCount = trainingSetCount;
		input = new double[trainingSetCount][inputCount];
		output = new double[trainingSetCount][outputCount];
	}

	public int getTrainingSetCount() {
		return trainingSetCount;
	}

	public void setInput(int set, int index, double value) throws RuntimeException {
		if ((set < 0) || (set >= trainingSetCount))
			throw (new RuntimeException("Training set out of range:" + set));
		if ((index < 0) || (index >= inputCount))
			throw (new RuntimeException("Training input index out of range:"
					+ index));
		input[set][index] = value;
	}

	public void setOutput(int set, int index, double value) throws RuntimeException {
		if ((set < 0) || (set >= trainingSetCount))
			throw (new RuntimeException("Training set out of range:" + set));
		if ((index < 0) || (set >= outputCount))
			throw (new RuntimeException("Training input index out of range:"
					+ index));
		output[set][index] = value;
	}

	public double getInput(int set, int index) throws RuntimeException {
		if ((set < 0) || (set >= trainingSetCount))
			throw (new RuntimeException("Training set out of range:" + set));
		if ((index < 0) || (index >= inputCount))
			throw (new RuntimeException("Training input index out of range:"
					+ index));
		return input[set][index];
	}

	public double getOutput(int set, int index) throws RuntimeException {
		if ((set < 0) || (set >= trainingSetCount))
			throw (new RuntimeException("Training set out of range:" + set));
		if ((index < 0) || (set >= outputCount))
			throw (new RuntimeException("Training input index out of range:"
					+ index));
		return output[set][index];
	}

	public double[] getOutputSet(int set) throws RuntimeException {
		if ((set < 0) || (set >= trainingSetCount))
			throw (new RuntimeException("Training set out of range:" + set));
		return output[set];
	}

	public double[] getInputSet(int set) throws RuntimeException {
		if ((set < 0) || (set >= trainingSetCount))
			throw (new RuntimeException("Training set out of range:" + set));
		return input[set];
	}
}
