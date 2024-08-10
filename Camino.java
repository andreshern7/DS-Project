import java.util.*;
import java.util.LinkedList;

public class Camino {
	private TreeMap<String,List<String[]>> lista_ady = new TreeMap<>();  //representación del grafo por lista de adyacencia
    //A= [{B,60,3},{Z,150,4}]
	int inicial = 0;
	public Camino(List<String[]> rutas) {
		creacionGrafo(rutas);
	}
	private void creacionGrafo(List<String[]> rutas) {
		// ruta es la lista que contiene el bus, los paaderos en 1+2n y los tiemps en 2+2n
		for (String[] ruta : rutas) {
			//ruta[0] es el bus que pasa por los paraderos
			String bus = ruta[0];  //identificador bus
			for (int j = 1; j<=ruta.length-1; j = j+2) {
				if (j == ruta.length-1) { //final de la linea
					lista_ady.putIfAbsent(ruta[j], new ArrayList<>());
					break;
				}
				String paraderoActual = ruta[j];
	            String paraderoSiguiente = ruta[j+2];
	            String tiempo = ruta[j+1];
	            String[] info = {paraderoSiguiente, tiempo, bus};
				// Si la lista no contiene el paradero como vértice, se conecta a él
	            lista_ady.putIfAbsent(paraderoActual, new ArrayList<>());
	            List<String[]> listaAdyacencia = lista_ady.get(paraderoActual);
	            // Verificar si ya existe una ruta entre los mismos paraderos y actualizar si es necesario
                // Revisar que siempre se elija el tiempo menor
	            boolean updated = false;
	            for (int k = 0; k < listaAdyacencia.size(); k++) {
	                String[] existente = listaAdyacencia.get(k);
	                if (existente[0].equals(paraderoSiguiente)) {
	                	updated = true;
	                	if (Integer.parseInt(existente[1]) > Integer.parseInt(tiempo)) {
	                    listaAdyacencia.set(k, info);
	                    break;
	                }
	            }
	            }
	            if (!updated) {
	                listaAdyacencia.add(info);
	            }
			}
		}
	}

	public void ImprimirCamino(String inicio, String fin) {
		List<String> paraderos = CaminoMasCorto(inicio,fin);
		ImprimirCaminorec(paraderos);
	}

    //Este es el método privado
	private void ImprimirCaminorec(List<String> paraderos) {
		List<String> buses = new ArrayList<>();
		for (int i = 0; i<paraderos.size()-1; i++) {
			List<String[]> infoLista = lista_ady.get(paraderos.get(i));
			for (String[] info : infoLista ) {
				if (info[0].equals(paraderos.get(i+1))) {
					buses.add(info[2]);
				}
			}
		}

        //------------------------------
        //DAR LA RESPUESTA FINAL AL USUARIO
        //------------------------------
        StringBuilder respuesta = new StringBuilder();
		// System.out.println("\n"+buses.toString()+"\n");
        respuesta.append("Tomar el bus "+ buses.get(0) + " en la parada " + paraderos.get(inicial));
		for (int j = 0; j < buses.size()-1; j++) {
            if(buses.get(j) != buses.get(j+1)){
                respuesta.append(" luego bajarse en la parada " + paraderos.get(j+1) + " tomar el bus " + buses.get(j+1));
            }
		}
        respuesta.append(" hasta llegar a la parada " + paraderos.get(paraderos.size()-1));
        System.out.println(respuesta);			
	}


	private List<String> CaminoMasCorto(String inicio, String fin){
		// La distancia es un diccionario que tiene como llave un vértice y como valor la distancia que se recorre
		// desde el vértice inicial hasta el vértice actual
		Map<String, Integer> distancia = new HashMap<>();  //se va acumulando
		// El padre tiene como llave el vértice hijo, y el valor como el vértice padre, esto será util para saber el 
		// camino mas corto
        Map<String, String> padre = new HashMap<>(); // Para recorrer el camino
        // la cola se basa en comparar cual vértice tiene menor distancia, esto para visitar los vértices más cercanos
        PriorityQueue<String> cola = new PriorityQueue<>(Comparator.comparingInt(distancia::get)); //
        // se pone como valor infinito a cada recorrido entre paraderos
        for (String paradero : lista_ady.keySet()) {
            distancia.put(paradero, Integer.MAX_VALUE);
        }
        // el vértice inicial tiene con recorrido 0
        distancia.put(inicio, 0);
        cola.add(inicio);
        while (!cola.isEmpty()) {
            String paraderoActual =  cola.poll();

            // Si el paradero actual es igual al final, hemos llegado
            if (paraderoActual.equals(fin)) {
                break;
            }
            // Si el paradero no contiene mas recorridos entre paraderos, continuamos con el siguiente paradero adyacente
            if (!lista_ady.containsKey(paraderoActual)) {  // Ya se exploró el vertice (evitas los bucles)
                continue;	
            }
            // se visita cada recorrido entre paraderos
            for (String[] vecino : lista_ady.get(paraderoActual)) {
                String paraderoSiguiente = vecino[0];
                int pesoArista = Integer.parseInt(vecino[1]);
                // Si ya está visitado el vértice siguiente no hay necesidad de ver si tiene un mejor camino
                int nuevaDistancia = distancia.get(paraderoActual) + pesoArista;
                // Si la distancia del recorrido del paradero actual al paradero inicial mas la distancia del recorrido del paradero
                // actual al paradero siguiente es menor a la distancia actual del paradero siguiente, se cambia la distancia actual
                // por la distancia menor
                if (nuevaDistancia < distancia.get(paraderoSiguiente)) {
                    distancia.put(paraderoSiguiente, nuevaDistancia);
                    padre.put(paraderoSiguiente, paraderoActual);
                    // se añade a la cola pues acaba de ser visitado
                    cola.add(paraderoSiguiente);
                }
            }
        }

        List<String> camino = new LinkedList<>();
        for (String hijo = fin; hijo != null; hijo = padre.get(hijo)) {
            camino.add(hijo);
        }
        Collections.reverse(camino);

        if (!camino.isEmpty() && camino.get(0).equals(inicio)) {
        	// System.out.println("\n"+camino.toString()+"\n"); //TEST
            return camino;
        } else {
            return null; 
        }
	}
	
	public static void main(String[] args) {
		Scanner sq = new Scanner(System.in);
		int numeroRutas =sq.nextInt();
		sq.nextLine();
		List<String[]> rutas = new ArrayList<>();
		for (int i = 0; i< numeroRutas; i++) {
			String cadenaRuta = sq.nextLine();
			String[] ruta = cadenaRuta.split(" ");
			rutas.add(ruta);
		}
		Camino cm = new Camino(rutas);
		cm.ImprimirCamino("A","F");
	}
}
