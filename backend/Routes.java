package backend;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;

public class Routes {

  private TreeMap<String, Node> lista_ady = new TreeMap<>();

  //Representa un arreglo para identificar una ruta y su nodo inicial
  //{3: 'obj:nodo':{A:'obj:aristas'}}
  private TreeMap<Integer, Node> nodoInicialRuta = new TreeMap<>();

  // private TreeMap<String, Nodo> lista_ady = new TreeMap<>();
  // Representación del grafo por lista de adyacencia
  // Representa todo el grafo, pero la idea es dejarlo por fuera para que el TreeMap solo almacene objetos de tipo ruta # TODO
  //A= [{B,60,3},{Z,150,4}]
  public Routes(List<String[]> rutas) {
    creacionGrafo(rutas);
  }

  public void creacionGrafo(List<String[]> rutas) {
    // ruta es la lista que contiene el bus, los paraderos en 1+2n y los tiemps en 2+2n
    Node nodo;
    for (String[] ruta : rutas) {
      //ruta[0] es el bus que pasa por los paraderos
      String bus = ruta[0];  //identificador bus
      for (int j = 1; j <= ruta.length - 1; j = j + 2) {
        String paraderoActual = ruta[j];
        if (j == 1) {
//          String variable = valor != null ? valor : "valor_por_defecto";

          nodo = Objects.requireNonNullElse(lista_ady.get(paraderoActual), new Node(paraderoActual)); //Nodo inicial
//          nodoInicialRuta.putIfAbsent(Integer.parseInt(ruta[0]), nodo);
          nodoInicialRuta.put(Integer.parseInt(ruta[0]), nodo);
        } else {
          nodo = Objects.requireNonNullElse(lista_ady.get(paraderoActual), new Node(paraderoActual));
        }
        if (j == ruta.length - 1) { //final de la linea
          // lista_ady.putIfAbsent(ruta[j], new ArrayList<>());
//          lista_ady.putIfAbsent(paraderoActual, nodo);
          lista_ady.put(paraderoActual, nodo);
          break;
        }
        String paraderoSiguiente = ruta[j + 2];
        String tiempo = ruta[j + 1];
        String[] info = {paraderoActual, paraderoSiguiente, tiempo, bus};
//        nodo.addArista(info);

        // Si la lista no contiene el paradero como vértice, se conecta a él
        // lista_ady.putIfAbsent(paraderoActual, new ArrayList<>());
//        nodo = new Nodo(ruta[j]);
//        lista_ady.putIfAbsent(ruta[j], nodo);
        lista_ady.put(paraderoActual, nodo);
//        nodo = lista_ady.get(paraderoActual);
// Verificar si ya existe una ruta entre los mismos paraderos y actualizar si es necesario
// Revisar que siempre se elija el tiempo menor
        boolean updated = false;
        for (int k = 0; k < nodo.getAristasSize(); k++) {
          String[] existente = nodo.getArista(k).getEdgeInfo();
          if (existente[1].equals(paraderoSiguiente)) {
            updated = true;
            if (Integer.parseInt(existente[2]) > Integer.parseInt(tiempo)) {
              nodo.setArista(k, info);
              nodo.setAristaMejorId(k, Integer.parseInt(info[3]));
              break;
            }else{
              nodo.addIdBus(k, Integer.parseInt(info[3]));
            }
          }
        }
        if (!updated) {
          nodo.addArista(info);
        }
      }
    }
  }

  void imprimirIDRuta() {
    for (Integer id : nodoInicialRuta.keySet()) {
      System.out.println("RUTA # " + id + " - Parada Inicial: " + nodoInicialRuta.get(id).getOrigen());
    }
    System.out.println();
  }

  //Por la implementación del grafo directamente ya está el mejor camino en todo
  void imprimirRutas() {
    if (nodoInicialRuta.isEmpty()) {
      return;
    }
    Node nodoInicial = null;
    for (Integer id : nodoInicialRuta.keySet()) {
      nodoInicial = nodoInicialRuta.get(id);

      System.out.println("RUTA #" + id);
      Node nodo = nodoInicial;
      int index;
      while (nodo.tieneEdge()) {
        boolean perteneceRuta = false;
        for (int i = 0; i < nodo.getAristasSize(); i++) {
          index = nodo.indexIdBus(i, id);
          if (index != -1) {
            System.out.println(nodo.getArista(i));
            nodo = lista_ady.get(nodo.getArista(i).getParaderoDestino());
            perteneceRuta = true;
          }
        }
        if(perteneceRuta == false){
          break;
        }
      }
    }
  }

  public String imprimirMejorRuta(String origen, String destino){
    Path path = new Path(lista_ady);
//    Scanner scanner = new Scanner(System.in);
//    System.out.println("Escribe el paredero de origen: ");
//    String origen = scanner.nextLine();
//    System.out.println("Escribe el paredero de destino: ");
//    String destino = scanner.nextLine();
//    scanner.close();
    return path.imprimirCamino(origen, destino);

  }

}