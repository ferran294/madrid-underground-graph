package EDGraph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Scanner;

public class Metro {
	// DEFINICION DE LA CLASE EDLineas
	private class EDLineas {

		private String id;
		private String nombre;
		private String origen;
		private String destino;

		// constructor

		public EDLineas(String id, String nombre, String origen, String destino) {
			this.id = id;
			this.nombre = nombre;
			this.origen = origen;
			this.destino = destino;
		}

	}

	// DEFINICION DE LA CLASE Inforuta

	private class InfoRuta {
		private int index;
		private String linea;

		public InfoRuta(int index, String linea) {
			this.index = index;
			this.linea = linea;
		}

		public InfoRuta() {
			this.index = -1;
			this.linea = null;
		}

		public void setInd(int index) {
			this.index = index;
		}

		public void setLin(String linea) {
			this.linea = linea;
		}
	}

	// Atributos de Metro

	private EDListGraph<String, String> grafo;
	private EDLineas[] lineas;

	// Completar: ejercicio 1 HECHO

	public Metro(String filename) {
		grafo = new EDListGraph<String, String>(false); // grafo no dirigido
		leerGrafo(filename);
		// g.printGraphStructure();
		for (int i = 0; i < lineas.length; i++) {
			System.out.print(lineas[i].id + " ");
		}
		System.out.println();
	}

	// Lee el fichero con la informaci�n de las lineas de metro
	private void leerGrafo(String nomfich) {
		Scanner inputStream = null;
		try {
			inputStream = new Scanner(new FileInputStream(nomfich));
		} catch (FileNotFoundException e) {
			System.out.println("No se puede abrir el fichero");
			System.exit(0);
		}

		int tamVectorLineas = inputStream.nextInt();
		lineas = new EDLineas[tamVectorLineas];

		// Atributos a leer para cada linea
		for (int i = 0; i < lineas.length; i++) {

			String id = inputStream.next();
			String nombre = inputStream.next();
			int numEstacionesLinea = inputStream.nextInt();
			inputStream.nextLine();
			String origen = inputStream.nextLine().toLowerCase();

			System.out.println(origen);
			if (grafo.getNodeIndex(origen) == -1) {
				grafo.insertNode(origen);
			}
			String estacionPrev = origen;

			for (int j = 0; j < numEstacionesLinea - 2; j++) {

				String estacion = inputStream.nextLine().toLowerCase();

				if (grafo.getNodeIndex(estacion) == -1) {
					grafo.insertNode(estacion);
				}
				EDEdge<String> arcoEstacion = new EDEdge<String>(
						grafo.getNodeIndex(estacionPrev),
						grafo.getNodeIndex(estacion), id);
				grafo.insertEdge(arcoEstacion);
				estacionPrev = estacion;
			}

			String destino = inputStream.nextLine().toLowerCase();
			if (grafo.getNodeIndex(destino) == -1) {
				grafo.insertNode(destino);
			}
			EDEdge<String> arcoEstacion = new EDEdge<String>(
					grafo.getNodeIndex(estacionPrev),
					grafo.getNodeIndex(destino), id);
			grafo.insertEdge(arcoEstacion);
			lineas[i] = new EDLineas(id, nombre, origen, destino);
		}
		grafo.printGraphStructure();
		// IMPLEMENTAR: EJERCICIO 2 HECHO

	}

	// Devuelve el n�mero total de estaciones del metro
	public int getNumeroEstaciones() {
		return grafo.getSize();

		// IMPLEMENTAR EJERCICIO 3 HECHO
	}

	// Escribe por pantalla el n�mero de l�neas del metro y para cada l�nea
	// su nombre, estaci�n de origen y estaci�n de destino
	public void printInfoMetro() {
		System.out.println("Informacion de las lineas de metro");
		System.out.println("-----------------------------------");
		System.out.println("");
		System.out.println("Numero de lineas: " + lineas.length);
		System.out.println("");
		for (int i = 0; i < lineas.length; i++) {
			System.out.println(lineas[i].id + "  Origen: " + lineas[i].origen
					+ "\t\t  Destino: " + lineas[i].destino);
		}
		// IMPLEMENTAR EJERCICIO 3 HECHO
	}

	// Devuelve una lista con los identificadores de las l�neas de metro que
	// tienen parada en esa estaci�n
	public List<String> getLineasEstacion(java.lang.String estacion)
			throws IndexOutOfBoundsException {
		int index = grafo.getNodeIndex(estacion);
		if (index < 0) {
			System.out.println(index);
			System.out.println("No existe la estacion");
			throw new IndexOutOfBoundsException();
		} else {

			ArrayList<String> lista = new ArrayList<String>();
			List<EDEdge<String>> listaArcos = grafo.getIncidentArcs(index);
			Iterator iter = listaArcos.iterator();
			
			while (iter.hasNext()) {
				EDEdge<String> identificador = (EDEdge<String>) iter.next();
				if (!lista.contains(identificador.getWeight())) {
					lista.add(identificador.getWeight());
				}
			}
			return lista;
		}
		// IMPLEMENTAR EJERCICIO 3 HECHO

	}

	private ArrayList<InfoRuta> BFS(int start) {
		ArrayList<InfoRuta> lista = new ArrayList<InfoRuta>();

		for (int i = 0; i < grafo.getSize(); i++) {
			InfoRuta ruta = new InfoRuta();
			lista.add(ruta);
		}

		LinkedList<Integer> cola = new LinkedList<Integer>();
		lista.get(start).index = start;
		LinkedList<EDEdge<String>> listaArcos = (LinkedList<EDEdge<String>>) grafo
				.getIncidentArcs(start);

		for (EDEdge<String> arco : listaArcos) {

			cola.add(arco.getTarget());
			lista.get(arco.getTarget()).index = arco.getSource();
			lista.get(arco.getTarget()).linea = arco.getWeight();

		}

		while (cola.size() > 0) {
			int indice = cola.pop();
			listaArcos = (LinkedList<EDEdge<String>>) grafo
					.getIncidentArcs(indice);

			for (EDEdge<String> arco : listaArcos) {
				if (!cola.contains(indice)) {
					if (lista.get(arco.getTarget()).index == -1) {
						cola.add(arco.getTarget());
						lista.get(arco.getTarget()).index = arco.getSource();
						lista.get(arco.getTarget()).linea = arco.getWeight();
					}
				}
			}

		}

		// IMPLEMENTAR EJERCICIO 4
		return lista;
	}

	public void ruta(String origen, String destino) {
		int start = grafo.getNodeIndex(origen);
		int end = grafo.getNodeIndex(destino);
		String cadena = "";

		ArrayList<InfoRuta> ruta = BFS(start);

		int i = end;
		while (i != start) {
			cadena = " " + ruta.get(i).linea + " -> " + grafo.getNodeValue(i)
					+ cadena;
			i = ruta.get(i).index;
		}
		cadena = grafo.getNodeValue(start) + "  " + cadena;
		cadena = "Recorrido: " + cadena;
		System.out.println(cadena);

		// IMPLEMENTAR EJERCICO 4
	}

}
