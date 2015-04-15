package EDGraph;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

//import ejer1.EDListGraph.Node;

public class TestMetro {

	/**
	 * @param args
	 */
	
	private static final String menuString = 
			" Test Metro \n" +
	        " i                  Muestra por pantalla la informaci�n del metro \n" +
			" n                  N�mero de estaciones \n" +
			" l estacion         Muestra las l�neas que se pueden tomar en una determinada estaci�n \n" +
			" t                  Trayecto entre dos estaciones que se pedir�n \n" +
			" ?  		         Help\n" +
			" q  		         Quit\n";
	
	private static char interpret(String command, Metro metro) throws IllegalArgumentException, IOException {
		char opt;
		String line = "";
		int i=0;
		
		while(i < command.length() && command.charAt(i) == ' ' ) i++;
		if (i == command.length()) throw new IllegalArgumentException();
		 
		opt = command.charAt(i);
		
		if (i+2 < command.length()) line = command.substring(i+2);
		
		
		switch (opt) {

		case 'i':
			metro.printInfoMetro();
			break;
			
		case 'n': 
			System.out.println("Numero de estaciones: "+metro.getNumeroEstaciones());
			
			break;
			
		case 'l':
			
			List<String> lineas = metro.getLineasEstacion(line.toLowerCase());
			if (lineas != null && !lineas.isEmpty()) {
				System.out.print(lineas.size()+ " lineas: ");
				ListIterator<String> it = lineas.listIterator();
				while (it.hasNext())
						System.out.print(it.next()+ " ");
				System.out.println();
			}
			
			break;
			
		case 't':
			Scanner teclado = new Scanner(System.in);
			System.out.println("Estacion de origen: ");
			String origen = teclado.nextLine();
			System.out.println("Estacion de destino: ");
			String destino = teclado.nextLine();
			try {
				metro.ruta(origen.toLowerCase(),destino.toLowerCase());
			}catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Error en una de las estaciones "+e);
			}
			break;

		case '?':
			System.out.println(menuString);
			break;

		case 'q':
			break;
				
		default:
			throw new IllegalArgumentException();
		}
			
		return opt;
	}
	
	
	public static void main(String[] args) throws IOException {

		Scanner teclado = new Scanner(System.in);
		String filename = "metroMadrid.txt";
		Metro metro = new Metro(filename);
		
		System.out.println("Numero de estaciones: "+ metro.getNumeroEstaciones());
		
		String command;
		char opt =0;
		Scanner sc = new Scanner(System.in);
		 
		do { 
			System.out.println(menuString);
			command = sc.nextLine();
			
			try {
				opt = interpret(command, metro);
					
			} catch (IllegalArgumentException e) {
				System.out.println(" Option not recognized or wrong arguments");

			} catch (FileNotFoundException e) {
				System.out.println("File not found");
				
			}
			
		} while (opt != 'q');
		
		System.out.println("fin del programa.");
	}

}
