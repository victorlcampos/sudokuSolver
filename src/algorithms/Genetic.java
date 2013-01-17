package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.Gem;
import model.Specimen;


public class Genetic implements Solver {

	private static final int POPULATION_SIZE = 2;
	private static final int CROSSOVER_PROBABILITY = 80;
	private static final int MUTATION_PROBABILITY = 20;
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
		ArrayList<Gem> originalChromosome = new ArrayList<Gem>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9 ; j++) {
				originalChromosome.add(new Gem(cells[i][j], i, j));
			}
		}
		if(wrongNumbers(originalChromosome) > 0)
			throw new Exception("Não é possível resolver com a configuração inicial");
		
		char[][] awnser = null;
		fillPopulation(cells);
		while(true){
			awnser = matingPool();
			if(awnser != null) break;
			crossover(cells);
			mutation(cells);
			System.out.println(population.get(0).getErrors());
		}
		
		return awnser;
	}

	private void mutation(char[][] cells) {
		Collections.sort(population);
		Random randomGenerator = new Random();
		int gen;
		List<Character> indexList;
		char number;
		for (Specimen specimen : population) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					gen = i*9 + j;
					if (cells[i][j] == '#' && randomGenerator.nextInt(100) < MUTATION_PROBABILITY ) {
						indexList = specimen.getChromosome().get(gen).getIndexList();
						if (indexList.size() > 0) {
							number = indexList.get(randomGenerator.nextInt(indexList.size()));						
							specimen.getChromosome().get(gen).setNumber(number);
						}						
					}
				}
			}
		}
	}

	private void crossover(char[][] cells) {
		population.clear();
		Specimen specimen1;
		Specimen specimen2;
		char number;
		Random randomGenerator = new Random();
		int nextCopy = 1;
		for (int k = 0; k < POPULATION_SIZE/2; k++) {
			
			specimen1 = copyPopulation.get(0);			
			specimen2 = copyPopulation.get((randomGenerator.nextInt(copyPopulation.size()-1)%4)+1);
			
			while (specimen1.equals(specimen2)) {
				specimen2 = copyPopulation.get((randomGenerator.nextInt(copyPopulation.size()-1)%3)+1);
			}
			nextCopy = 1;
			
			if (randomGenerator.nextInt(100) < CROSSOVER_PROBABILITY) {
				int gen;
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						if(cells[i][j] == '#' && randomGenerator.nextInt(100) < 30){
							gen = i*9 + j;
							number = specimen2.getChromosome().get(gen).getNumber();
							if(specimen1.getChromosome().get(gen).getIndexList().contains(new Character(number))){
								specimen2.getChromosome().get(gen).setNumber(specimen1.getChromosome().get(gen).getNumber());
								specimen1.getChromosome().get(gen).setNumber(number);
							}
						}
					}
				}
			}
			
			population.add(specimen1);
			copyPopulation.remove(specimen1);
			
			population.add(specimen2);
			copyPopulation.remove(specimen2);
		}
	}

	private char[][] matingPool() {
		float sumAdaptation = 0;
		int adaptation = 0;
		int errors = 0;
		Specimen specimen;
		Specimen specimen2;
		copyPopulation.clear();
		for (int i = 0; i < POPULATION_SIZE; i++) {
			specimen = population.get(i);
			errors = wrongNumbers(specimen.getChromosome());
			if(errors > 18){
				specimen.setErrors(errors);
			} else {
				
				char[][] result = new char[9][9];
				int ir, jr;
				for (int k = 0; k < 81; k++) {
					ir = k/9;
					jr = k%9;
					
					result[ir][jr] = specimen.getChromosome().get(k).getNumber();
				}				
				return result;
			}
				
			specimen.setAdaptation((float) (1.0/specimen.getErrors()));
			
			sumAdaptation += specimen.getAdaptation();			
		}
		
		Collections.sort(population);
		
		for (int i = 0; i < POPULATION_SIZE && copyPopulation.size() < POPULATION_SIZE; i++) {
			specimen = population.get(i);
			adaptation = Math.round((specimen.getAdaptation()*POPULATION_SIZE)/sumAdaptation);
			for (int j = 0; j < adaptation && copyPopulation.size() < POPULATION_SIZE; j++) {
				specimen2 = new Specimen(specimen.getChromosome());
				specimen2.setErrors(specimen.getErrors());
				specimen2.setAdaptation(specimen.getAdaptation());
				
				copyPopulation.add(specimen2);
			}
		}
		Collections.sort(copyPopulation);
		Gem newGem;
		while (copyPopulation.size() < POPULATION_SIZE) {
			ArrayList<Gem> chromosome = new ArrayList<Gem>();
			ArrayList<Character> indexList = new ArrayList<Character>();
			
			for (Gem gem : copyPopulation.get(0).getChromosome()) {
				newGem = new Gem(gem.getNumber(), gem.getI(), gem.getJ());
				for (Character character : gem.getIndexList()) {
					indexList.add(new Character(character));
				}
				newGem.setIndexList(indexList);
				chromosome.add(newGem);
			}
			
			specimen2 = new Specimen(chromosome);
			specimen2.setErrors(copyPopulation.get(0).getErrors());
			specimen2.setAdaptation(copyPopulation.get(0).getAdaptation());			
			copyPopulation.add(specimen2);
		}	
		
		Collections.sort(copyPopulation);
		return null;			
	}

	private void fillPopulation(char[][] cells) {
		char number;
		Random randomGenerator = new Random();
		ArrayList<Gem> cloneCells;
		for (int p = 0; p < POPULATION_SIZE; p++) {
			cloneCells = new ArrayList<Gem>();
			
			population.add(new Specimen(cloneCells));			
			int i, j;
			Gem gem;
			for (int k = 0; k < 81; k++) {
				i = k/9;
				j = k%9;
				gem = cloneCells.get(k);
				
				number = cells[i][j];								
				if (number == '#') {
					gem.setNumber(Character.forDigit(randomGenerator.nextInt(9) + 1, 10));
				}else {
					gem.setNumber(Character.valueOf(number));
				}
			}
		}		
	}

	public int wrongNumbers(List<Gem> list) {
		int wrongNumbers = 0;
		Gem gem;
		for (int k = 0; k < 81; k++) {
			gem = list.get(k);
			if (gem.getNumber() != '#') {
				wrongNumbers += findErrors(list, gem, gem.getI(), gem.getJ());					
			}
		}
		return wrongNumbers/2;
	}

	private int findErrors(List<Gem> list, Gem gem, int i,
			int j) {
		int wrongNumbers = 0;
		// Line
		for (int j2 = 0; j2 < 9; j2++) {
			if(j2 != j){
				if (gem.getNumber() == list.get(i*9 + j2).getNumber()) {
					wrongNumbers++;				
				}else {
					gem.getIndexList().remove(new Character(list.get(i*9 + j2).getNumber()));
				}
			}			
		}
		
		// Column
		for (int i2 = 0; i2 < 9; i2++) {
			if (i2 != i) {
				if (gem.getNumber() == list.get(i2*9 + j).getNumber()) {
					wrongNumbers++;
				}else  {
					gem.getIndexList().remove(new Character(list.get(i2*9 + j).getNumber()));
				}	
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
				if((i2 != i || j2 != j)) {
					if (gem.getNumber() == list.get(i2*9 + j2).getNumber()) {
						wrongNumbers++;
					}else {
						gem.getIndexList().remove(new Character(list.get(i2*9 + j2).getNumber()));
					}
				}
			}
		}
		
		return wrongNumbers;
	}

}
