package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.Specimen;


public class Genetic implements Solver {

	private static final int POPULATION_SIZE = 4; // Must be Even
	private static final int CROSSOVER_PROBABILITY = 70;
	private static final int MUTATION_PROBABILITY = 40;
	private static Genetic instance = new Genetic();
	private List<Specimen> population =  new ArrayList<Specimen>();
	private List<Specimen> copyPopulation =  new ArrayList<Specimen>();
	
	
	public static Genetic getInstance() {
		return instance ;
	}
	
	private Genetic(){
		
	}

	@Override
	public char[][] solveProblem(char[][] cells) throws Exception {
		if(wrongNumbers(cells) > 0)
			throw new Exception("Não é possível resolver com a configuração inicial");
		
		boolean solved = false;
		char[][] awnser = null;
		fillPopulation(cells);
		while(!solved){
			awnser = matingPool(solved);
			crossover(cells);
			mutation(cells);
			System.out.println(population.get(0).getErrors());
		}
		
		return awnser;
	}

	private void mutation(char[][] cells) {
		Collections.sort(population);
		Random randomGenerator = new Random();
		for (Specimen specimen : population) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (cells[i][j] == '#' && randomGenerator.nextInt(100) < MUTATION_PROBABILITY ) {
						specimen.getChromosome()[i][j] = Character.forDigit(randomGenerator.nextInt(9) + 1, 10);
					}
				}
			}
		}
	}

	private void crossover(char[][] cells) {
		System.out.println("------- Entrei no crossover ----------");
		population.clear();
		Specimen specimen1;
		Specimen specimen2;
		char number;
		Random randomGenerator = new Random();
		for (int k = 0; k < POPULATION_SIZE/2; k++) {
			
			specimen1 = copyPopulation.get(0);
			
			specimen2 = copyPopulation.get(randomGenerator.nextInt(copyPopulation.size()-1)+1);
			while (specimen1.equals(specimen2)) {
				specimen2 = copyPopulation.get(randomGenerator.nextInt(copyPopulation.size()-1)+1);
			}
			
			if (randomGenerator.nextInt(100) < CROSSOVER_PROBABILITY) {
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						if(cells[i][j] == '#' && randomGenerator.nextInt(100) < 50){
							number = specimen1.getChromosome()[i][j];
							specimen1.getChromosome()[i][j] = specimen2.getChromosome()[i][j];
							specimen2.getChromosome()[i][j] = number;
						}
					}
				}
			}
			
			population.add(specimen1);
			copyPopulation.remove(specimen1);
			
			population.add(specimen2);
			copyPopulation.remove(specimen2);
		}
		System.out.println("------- Sai do crossover ----------");
	}

	private char[][] matingPool(boolean solved) {
		System.out.println("------- Entrei no matingPool ----------");
		float sumAdaptation = 0;
		int adaptation = 0;
		int errors = 0;
		Specimen specimen;
		Specimen specimen2;
		copyPopulation.clear();
		for (int i = 0; i < POPULATION_SIZE; i++) {
			specimen = population.get(i);
			errors = wrongNumbers(specimen.getChromosome());
			if(errors > 0){
				specimen.setErrors(errors);
			} else {
				solved = true;
				return specimen.getChromosome();
			}
				
			specimen.setAdaptation((float) (1.0/specimen.getErrors()));
			
			sumAdaptation += specimen.getAdaptation();			
		}
		
		Collections.sort(population);
		
		for (int i = 0; i < POPULATION_SIZE && copyPopulation.size() <= POPULATION_SIZE; i++) {
			specimen = population.get(i);
			adaptation = Math.round((specimen.getAdaptation()*POPULATION_SIZE)/sumAdaptation);
			for (int j = 0; j < adaptation && copyPopulation.size() <= POPULATION_SIZE; j++) {
				specimen2 = new Specimen(specimen.getChromosome());
				specimen2.setErrors(specimen.getErrors());
				specimen2.setAdaptation(specimen.getAdaptation());
				
				copyPopulation.add(specimen2);
			}
		}
		Collections.sort(copyPopulation);
		
		for (int i = 0; i < POPULATION_SIZE - copyPopulation.size(); i++) {
			specimen2 = new Specimen(copyPopulation.get(0).getChromosome());
			specimen2.setErrors(copyPopulation.get(0).getErrors());
			specimen2.setAdaptation(copyPopulation.get(0).getAdaptation());			
			copyPopulation.add(specimen2);
		}	
		
		Collections.sort(copyPopulation);
		
		System.out.println("------- Sai do matingPool ----------");
		
		return null;			
	}

	private void fillPopulation(char[][] cells) {
		char number;
		Random randomGenerator = new Random();
		char[][] cloneCells;
		for (int p = 0; p < POPULATION_SIZE; p++) {
			cloneCells = new char[9][9];
			population.add(new Specimen(cloneCells));			
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					number = cells[i][j];								
					if (number == '#') {
						cloneCells[i][j] = Character.forDigit(randomGenerator.nextInt(9) + 1, 10);
					}else {
						cloneCells[i][j] = Character.valueOf(number);
					}
				}
			}
		}		
	}

	public int wrongNumbers(char[][] cloneCells) {
		int wrongNumbers = 0;
		char number;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				
				number = cloneCells[i][j];
				if (number != '#') {
					wrongNumbers += findErrors(cloneCells, number, i, j);					
				}				
			}
		}
		return wrongNumbers/2;
	}

	private int findErrors(char[][] cloneCells, char number, int i,
			int j) {
		int wrongNumbers = 0;
		
		// Line
		for (int j2 = 0; j2 < 9; j2++) {
			if (j2 != j && number == cloneCells[i][j2]) {
				wrongNumbers++;
			}
		}
		
		// Column
		for (int i2 = 0; i2 < 9; i2++) {
			if (i2 != i && number == cloneCells[i2][j]) {
				wrongNumbers++;
			}
		}
		
		// Sector
		int sectorRow = i/3;
		int sectorColumn = j/3;					
		int i2;
		int j2;
		
		for (int is = 0; is < 3; is++) {
			for (int js = 0; js < 3; js++) {							
				i2 = 3*sectorRow + is;
				j2 = 3*sectorColumn + js;
				
				if ((i2 != i || j2 != j) && number == cloneCells[i2][j2]) {
					wrongNumbers++;
				}
			}
		}
		
		return wrongNumbers;
	}

}
