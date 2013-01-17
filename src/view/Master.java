package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import algorithms.Genetic;
import algorithms.HillClimb;

public class Master {

	private JFrame frmSudoku;
	private JPanel jpProblem;
	private JPanel jpGenetic;
	private JPanel jpHillClimb;
	private final static Master instance = new Master();
	private char[][] casas;
	private JLabel lblHillClimbTime;
	private JLabel lblGeneticTime;
	
	public static void main(String[] args) {
		Master window = Master.getInstance();
		window.frmSudoku.setVisible(true);
	}
	
	public static Master getInstance(){
		return instance;
	}
	

	/**
	 * @wbp.parser.entryPoint
	 */
	private Master() {
		initialize();
	}

	private void initialize() {
		casas = new char[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				casas[i][j] = '#';
			}
		}
		
		frmSudoku = new JFrame();
		frmSudoku.setTitle("Sudoku");
		frmSudoku.setResizable(false);
		frmSudoku.setBounds(100, 100, 656, 560);
		frmSudoku.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmSudoku.setJMenuBar(menuBar);

		JMenu mnArquivo = new JMenu("Arquivo");
		menuBar.add(mnArquivo);

		JMenuItem mntmImport = new JMenuItem("Importar");
		mntmImport.addActionListener(new ImportMouseClickListener());

		mnArquivo.add(mntmImport);
		frmSudoku.getContentPane().setLayout(null);
		
		jpProblem = new JPanel();
		createSudokuPanel(jpProblem, 220, 6);		
		frmSudoku.getContentPane().add(jpProblem);
		

		jpHillClimb = new JPanel();
		jpHillClimb.setBackground(Color.WHITE);
		createSudokuPanel(jpHillClimb, 220, 280);
		frmSudoku.getContentPane().add(jpHillClimb);
		
//		jpGenetic = new JPanel();
//		jpGenetic.setBackground(Color.WHITE);
//		createSudokuPanel(jpGenetic, 6, 258);
//		frmSudoku.getContentPane().add(jpGenetic);
//		
//
//		JLabel lblAlgortmoGentico = new JLabel("Algor\u00EDtmo Gen\u00E9tico");
//		lblAlgortmoGentico.setBounds(6, 230, 200, 16);
//		frmSudoku.getContentPane().add(lblAlgortmoGentico);
//
//		JLabel lblTempo = new JLabel("Tempo:");
//		lblTempo.setBounds(6, 470, 61, 16);
//		frmSudoku.getContentPane().add(lblTempo);
//
//		lblGeneticTime = new JLabel("0 segundos");
//		lblGeneticTime.setBounds(58, 470, 148, 16);
//		frmSudoku.getContentPane().add(lblGeneticTime);

		JLabel label_1 = new JLabel("Tempo:");
		label_1.setBounds(220, 494, 61, 16);
		frmSudoku.getContentPane().add(label_1);

		lblHillClimbTime = new JLabel("0 segundos\n");
		lblHillClimbTime.setBounds(272, 494, 154, 16);
		frmSudoku.getContentPane().add(lblHillClimbTime);

		JLabel lblAlgortmoHillClimb = new JLabel("Algor\u00EDtmo Hill Climb");
		lblAlgortmoHillClimb.setBounds(252, 259, 130, 16);
		frmSudoku.getContentPane().add(lblAlgortmoHillClimb);

		JButton btnResolver = new JButton("Resolver");
		btnResolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					long start = System.nanoTime();
					char[][] awnser = HillClimb.getInstance().solveProblem(casas);
					long end = System.nanoTime();
					
					lblHillClimbTime.setText((end-start)/1e9+" segundos");
										
					jpHillClimb.removeAll();
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							JLabel label;
							if(awnser[i][j] == '#'){
								label = new JLabel("");
							} else {
								label = new JLabel(String.valueOf(awnser[i][j]));
							}
							label.setSize(23, 23);
							label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
							jpHillClimb.add(label);
						}
					}
					Master window = Master.getInstance();
					window.frmSudoku.setVisible(false);
					window.frmSudoku.setVisible(true);
					
					
//					start = System.nanoTime();
//					awnser = Genetic.getInstance().solveProblem(casas);
//					end = System.nanoTime();
//					
//					lblGeneticTime.setText((end-start)/1e9+" segundos");
//										
//					jpHillClimb.removeAll();
//					for (int i = 0; i < 9; i++) {
//						for (int j = 0; j < 9; j++) {
//							JLabel label;
//							if(awnser[i][j] == '#'){
//								label = new JLabel("");
//							} else {
//								label = new JLabel(String.valueOf(awnser[i][j]));
//							}
//							label.setSize(23, 23);
//							label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//							jpHillClimb.add(label);
//						}
//					}
//					window = Master.getInstance();
//					window.frmSudoku.setVisible(false);
//					window.frmSudoku.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnResolver.setBounds(265, 218, 117, 29);
		frmSudoku.getContentPane().add(btnResolver);
	}

	private void createSudokuPanel(JPanel jpanel,int posx, int posy) {
		jpanel.setBackground(Color.WHITE);
		jpanel.setBounds(posx, posy, 207, 207);
		jpanel.setLayout(new GridLayout(9, 9, 0, 0));
		
		
		for (Integer i = 0; i < 81; i++) {
			JLabel label = new JLabel();
			label.setSize(23, 23);
			label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			jpanel.add(label);						
		}
	}
	
	public void updateProblem(File file) {
		jpProblem.removeAll();
		try {
			Scanner scanner = new Scanner(file);
			for(int i = 0; scanner.hasNext(); i++){
				String sudokuNumber = scanner.next();
				JLabel label;
				if(sudokuNumber.equals("#")){
					label = new JLabel("");
				} else {
					label = new JLabel(sudokuNumber);
				}
				casas[i/9][i%9] = sudokuNumber.toCharArray()[0];
				label.setSize(23, 23);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				jpProblem.add(label);
			}
			Master window = Master.getInstance();
			window.frmSudoku.setVisible(false);
			window.frmSudoku.setVisible(true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	class ImportMouseClickListener extends AbstractAction {
		JFileChooser filechooser;

		public ImportMouseClickListener() {
			filechooser = new JFileChooser();
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int returnVal = filechooser.showOpenDialog(frmSudoku);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				Master.getInstance().updateProblem(file);									
			}		
		}
	}
	
}
