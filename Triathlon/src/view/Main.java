package view;

import java.util.concurrent.Semaphore;

import controller.Circuit;

public class Main {
	
	public static void main(String[] args) {
		
		Semaphore semaph = new Semaphore(5);
		
		for (int i = 0; i < 25; i++) {
			
			Circuit atleta = new Circuit(semaph, (i+1));
			atleta.start();
			
		}
		
	}
	
}
