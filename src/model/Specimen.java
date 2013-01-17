package model;

import java.util.ArrayList;
import java.util.List;


public class Specimen implements Comparable<Specimen> {
	private List<Gem> chromosome = new ArrayList<Gem>();
	private Integer errors;
	private float adaptation;
			
	public Specimen(List<Gem> chromosome) {
		super();
		if(chromosome.size() < 81) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					chromosome.add(new Gem('#', i, j));
				}
			}
		}
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
	public float getAdaptation() {
		return adaptation;
	}

	public void setAdaptation(float d) {
		this.adaptation = d;
	}

	public List<Gem> getChromosome() {
		return chromosome;
	}

	public void setChromosome(List<Gem> chromosome) {
		this.chromosome = chromosome;
	}	
}
