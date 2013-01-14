package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.HillClimbCell;

public class HillClimb implements Solver {
	private static HillClimb instance = new HillClimb();
	private List<HillClimbCell> wrongCells = new ArrayList<HillClimbCell>();
	private char[][] cloneCells = new char[9][9];
	private int lasti, lastj;
	
	
	public static HillClimb getInstance() {
		return instance ;
	}
	
	private HillClimb() {
		
	}

		
	public char[][] solveProblem(char[][] cells) throws Exception {
		if(wrongNumbers(cells, cells) > 0)
			throw new Exception("N‹o Ž poss’vel resolver com a configura‹o inicial");
		
		fillCells(cells);
		wrongNumbers(cloneCells, cells);
		
		while (wrongCells.size() > 0) {
			next(wrongCells.get(0), cells);
		}
		
		return cloneCells;
	}
	
	private void fillCells(char[][] casas) {
		char number;
		Random randomGenerator = new Random();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				number = casas[i][j];								
				if (number == '#') {
					cloneCells[i][j] = Character.forDigit(randomGenerator.nextInt(9) + 1, 10);
				}else {
					cloneCells[i][j] = Character.valueOf(number);
				}
			}
		}
	}

	public void next(HillClimbCell hcc, char[][] cells){
		int i = hcc.getI();
		int j = hcc.getJ();
		Random randomGenerator = new Random();
		
		if(i == lasti && j == lastj && wrongCells.size() > 1) {
			if(randomGenerator.nextInt(10)%10 == 0){
				hcc = wrongCells.get(randomGenerator.nextInt(wrongCells.size()));
				i = hcc.getI();
				j = hcc.getJ();
			}
		}
		lasti = i;
		lastj = j;
		
		if(hcc.getNumber() != cloneCells[i][j]){
			try {
				throw new Exception("Erro no c—digo");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		char number = Character.forDigit(randomGenerator.nextInt(9) + 1, 10);
		while(number == hcc.getNumber()){
			number = Character.forDigit(randomGenerator.nextInt(9) + 1, 10);
		}
		
		cloneCells[i][j] = number;
		wrongCells.clear();
		wrongNumbers(cloneCells, cells);
	}
	
	public int wrongNumbers(char[][] cloneCells, char[][] cells) {
		int wrongNumbers = 0;
		char number;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				
				number = cloneCells[i][j];
				if (number != '#') {
					wrongNumbers += findErrors(cloneCells, number, i, j, cells);					
				}				
			}
		}
		Collections.sort(wrongCells);
		return wrongNumbers/2;
	}

	private int findErrors(char[][] cloneCells, char number, int i,
			int j, char[][] cells) {
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
		
		if (wrongNumbers > 0 && (cloneCells[i][j] != cells[i][j])) {
			wrongCells.add(new HillClimbCell(number, wrongNumbers, i, j));
		}
		
		return wrongNumbers;
	}
}
