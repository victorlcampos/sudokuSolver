package model;

public class HillClimbCell implements Comparable<HillClimbCell> {
	private char number;
	private Integer errors;
	private int i, j;
	
	public HillClimbCell(char number, int errors, int i, int j) {
		this.i = i;
		this.j = j;
		this.setNumber(number);
		this.errors = errors;
	}
	
	@Override
	public int compareTo(HillClimbCell o) {		
		return -1*errors.compareTo(o.errors);
	}

	public char getNumber() {
		return number;
	}

	public void setNumber(char number) {
		this.number = number;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}		
}
