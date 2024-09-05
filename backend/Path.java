package backend;

import java.util.*;

public class Path {

  TreeMap<String, Node> grafoRutas;

  Path(TreeMap<String, Node> grafoRutas) {
    this.grafoRutas = grafoRutas;
  }

  private List<String> CaminoMasCorto(String origen, String destino) {
    // La distancia es un diccionario que tiene como llave un vértice y como valor la distancia que se recorre
    // desde el vértice inicial hasta el vértice actual
    Map<String, Integer> distancia = new HashMap<>();  //se va acumulando
    // El padre tiene como llave el vértice hijo, y el valor como el vértice padre, esto será util para saber el
    // camino mas corto
    Map<String, String> padre = new HashMap<>(); // Para recorrer el camino
    // Como llave tiene el paradero, como valor tiene el número de rutas que para llegar a este desde el origen en el
    // menor tiempo posible
    Map<String, Integer> numeroRutas = new HashMap<>();
    // Como llave tiene el paradero, como valor tienen la última ruta que ha tenido, es decir, la ruta del padre
    // hacia el paradero
    Map<String, Integer> mejorRuta = new HashMap<>();
    // la cola se basa en comparar cual vértice tiene menor distancia, esto para visitar los vértices más cercanos
    PriorityQueue<String> cola = new PriorityQueue<>(Comparator.comparingInt(distancia::get).thenComparing(numeroRutas::get)); //

    // Se pone como valor infinito a cada recorrido entre paraderos
    for (String paradero : grafoRutas.keySet()) {
      distancia.put(paradero, Integer.MAX_VALUE);
      numeroRutas.put(paradero, Integer.MAX_VALUE);
      mejorRuta.put(paradero, -1);
    }
    // El vértice inicial tiene recorrido 0
    distancia.put(origen, 0);
    numeroRutas.put(origen, 0);
    cola.add(origen);

    while (!cola.isEmpty()) {
      String paraderoActual = cola.poll();

      // Si el paradero actual es igual al final, hemos llegado al destino
      if (paraderoActual.equals(destino)) {
        break;
      }

      // Si el paraderoActual no contiene mas recorridos entre paraderos, continuamos con el siguiente paradero adyacente
      if (!grafoRutas.containsKey(paraderoActual)) {  // Ya se exploró el vertice (evitas los bucles)
        continue;
      }
      // se visita cada recorrido entre paraderos
      for (int i = 0; i < grafoRutas.get(paraderoActual).getAristasSize(); i++) {
        Node vecino = grafoRutas.get(paraderoActual);
        String paraderoSiguiente = vecino.getArista(i).getParaderoDestino();
        int pesoArista = vecino.getArista(i).getTiempo();
        int rutaSiguiente = vecino.getArista(i).getMejorIdBus();
        // Si ya está visitado el vértice siguiente no hay necesidad de ver si tiene un mejor camino
        int nuevaDistancia = distancia.get(paraderoActual) + pesoArista;
        // Se entre al if si la nueva distancia es menor a la actual o si las distancias son iguales pero el número de
        // trasbordos del nuevo nodo es menor que el que ya tenía la arista
        if (nuevaDistancia < distancia.get(paraderoSiguiente) ||
                (nuevaDistancia == distancia.get(paraderoSiguiente) &&
                        numeroRutas.get(paraderoActual) + (rutaSiguiente != mejorRuta.get(paraderoActual) ? 1 : 0)
                                < numeroRutas.get(paraderoSiguiente))) {
          distancia.put(paraderoSiguiente, nuevaDistancia);
          padre.put(paraderoSiguiente, paraderoActual);
          // Mira si hay una nueva ruta o no
          int nuevasRutas = numeroRutas.get(paraderoActual) + (rutaSiguiente != mejorRuta.get(paraderoActual) ? 1 : 0);
          // añade las rutas
          numeroRutas.put(paraderoSiguiente, nuevasRutas);
          mejorRuta.put(paraderoSiguiente, rutaSiguiente);
          // se añade a la cola pues acaba de ser visitado
          cola.add(paraderoSiguiente);
        }
      }
    }

    List<String> camino = new LinkedList<>();
    for (String hijo = destino; hijo != null; hijo = padre.get(hijo)) {
      camino.add(hijo);
    }
    Collections.reverse(camino);

    if (!camino.isEmpty() && camino.get(0).equals(origen)) {
      // System.out.println("\n"+camino.toString()+"\n"); //TEST
      return camino;
    } else {
      return null;
    }
  }


  public  String imprimirCamino(String origen, String destino) {
    List<String> paraderos = CaminoMasCorto(origen, destino);
    if (paraderos == null) {
      // Mensaje cuando no hay un camino posible
      return "No existe un camino posible entre " + origen + " y " + destino + ".";
    } else {
      // Imprime el camino normalmente si existe
      return imprimirCaminoP(paraderos);
    }

  }

  private String imprimirCaminoP(List<String> paraderos) {
    List<Integer> buses = new ArrayList<>();

//    for (String paradero : paraderos) {
//      System.out.println(paradero);
//    }
    Node nodeInfo;
    for (int i = 0; i < paraderos.size() - 1; i++) {
      nodeInfo = grafoRutas.get(paraderos.get(i));
      for (int j = 0; j < nodeInfo.getAristasSize(); j++) {
        if (nodeInfo.getArista(j).getParaderoDestino().equals(paraderos.get(i + 1))) {
          buses.add(nodeInfo.getArista(j).getMejorIdBus());
        }
      }
    }

    //------------------------------
    //DAR LA RESPUESTA FINAL AL USUARIO
    //------------------------------
    StringBuilder respuesta = new StringBuilder();
    // System.out.println("\n"+buses.toString()+"\n");
    respuesta.append("Tomar el bus " + buses.get(0) + " en la parada " + paraderos.get(0));
    for (int j = 0; j < buses.size() - 1; j++) {
      if (buses.get(j) != buses.get(j + 1)) {
        respuesta.append(" luego bajarse en la parada " + paraderos.get(j + 1) + " tomar el bus " + buses.get(j + 1));
      }
    }
    respuesta.append(" hasta llegar a la parada " + paraderos.get(paraderos.size() - 1));
//      System.out.println(respuesta);
    return respuesta.toString();

  }
}