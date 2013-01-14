package model;

public class Specimen implements Comparable<Specimen> {
	private char[][] chromosome = new char[9][9];
	private Integer errors;
	private float adaptation;
			
	public Specimen(char[][] chromosome) {
		super();
		this.setChromosome(chromosome);
	}
	
	public Integer getErrors() {
		return errors;
	}
	public void setErrors(Integer errors) {
		this.errors = errors;
	}	
	@Override
	public int compareTo(Specimen o) {
		return errors.compareTo(o.errors);
	}
	public char[][] getChromosome() {
		return chromosome;
	}
	
	public void setChromosome(char[][] chromosome) {
		this.chromosome = chromosome;
	}

	public float getAdaptation() {
		return adaptation;
	}

	public void setAdaptation(float d) {
		this.adaptation = d;
	}
}
