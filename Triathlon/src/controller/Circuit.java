package controller;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Circuit extends Thread {

	private Semaphore semaph;
	private int[] atleta = new int[4];
	private static int[][] rank = new int [25][2];
	private static int index = 0;
	//id, corrida, tiro, ciclismo
	
	private final int dCorrida = 3000;
	private final int dCiclismo = 5000;
	
	private static int pontCorrida = 250;
	private static int pontCiclismo = 250;
	
	//andada na corrida: 20-25 a cada 30ms
	//andada no ciclismo: 30-40 a cada 40ms
	
	Random rand = new Random();
	
	
	public Circuit(Semaphore s, int i) {
		atleta[0] = i;
		this.semaph = s;
	}
	
	private void corrida() {
		
		System.out.println( "\n------------------------------ \n" + "O atleta " + atleta[0] + " iniciou a corrida! \n" + "------------------------------\n");
		
		while (atleta[1] < dCorrida) {
			if (atleta[1] >= 2975) {
				int complemento = (dCorrida - atleta[1]);
				atleta[1] += complemento;
			} else { atleta[1] += rand.nextInt(20,26); }
			
			try {
				sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("O atleta " + atleta[0] + " terminou a corrida e recebeu " + pontCorrida + " pontos pelo trajeto percorrido \n");
		atleta[1] = pontCorrida;
		pontCorrida -= 10;
	}
	
	private void tiro() {
		
		for (int i = 0; i < 3; i++) {
			
			int randomSleep = rand.nextInt(500,3001);
			int pontos = rand.nextInt(0,11);
			
			try {
				sleep(randomSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			atleta[2] += pontos;
			System.out.println("O atleta " + atleta[0] + " atirou em " + (randomSleep/Math.pow(10, 3)) + " segundos e recebeu " + pontos + " pontos \n" );
		}
		
		System.out.println("O atleta " + atleta[0] + " terminou a etapa de tiro e acumulou o total de " + atleta[2] + " pontos");
	}
	
	private void ciclismo() {
		
		System.out.println( "\n------------------------------ \n" + "O atleta " + atleta[0] + " iniciou o ciclismo! \n" + "------------------------------\n");
		
		while (atleta[3] < dCiclismo) {
			if (atleta[3] >= 4960) {
				int complemento = (dCiclismo - atleta[1]);
				atleta[3] += complemento;
			} else { atleta[3] += rand.nextInt(30,41); }
			
			try {
				sleep(40);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("O atleta " + atleta[0] + " terminou o ciclismo e recebeu " + pontCiclismo + " pontos pelo percurso \n");
		atleta[3] = pontCiclismo;
		pontCiclismo -= 10;
		
		int[] vetorIdPontos = {atleta[0], (atleta[1] + atleta[2] + atleta[3])};
		
		preencheRanking(vetorIdPontos);
		
	}
	
	private void preencheRanking(int[] vetorIdPontos) {
		rank[index] = vetorIdPontos;
		index += 1;
		
		if(index == 25) { 
			rank = ordenaMatriz(rank);
			mostraRank(rank);
		}
	}

	private void mostraRank(int [][] rank) {
		
		for (int i = 0; i < 25; i++) {
			System.out.println("\n---------------------------------- \n" + "O atleta " + rank[i][0] + " chegou em " + (i+1) + "o. lugar \n" + "Pontuação: " + rank[i][1] + "\n---------------------------------- \n"); 
		}
	}

	private int[][] ordenaMatriz(int[][] rank) {
		
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 24; j++) {
				
				int[] aux = new int[2];
				
				if (rank[j+1][1] > rank[j][1]) {
					aux = rank[j];
					rank[j] = rank[j+1];
					rank[j+1] = aux;
				}
			}
		}
		return rank;
	}

	@Override
	public void run() {
		corrida();
		
		try {
			semaph.acquire();
			tiro();
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			semaph.release();
		}
		ciclismo();		
	}
}
