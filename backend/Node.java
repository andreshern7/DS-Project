package backend;

import java.util.ArrayList;
import java.util.List;

class Node {

  //                 {paradero , infomacion}
  //Representación ruta {A, [{B,60,3},{Z,150,4}]}

  // private int idBus;
  private String origen;
  //Esto va a representar los edges del grafo
  private List<Edge> aristas;

  // private int aristaCounter = 0;
  // private String[3] informacion;
  Node(String pOrigen) {
    this.origen = pOrigen;
    aristas = new ArrayList<Edge>();
  }

  // public agregarParadero(){
  // }

  public boolean tieneEdge() {
    return !(this.aristas.size() == 0);
  }

  public String getOrigen() {
    return this.origen;
  }

  public Edge getArista(int index) {
    return this.aristas.get(index);
  }

  public void setArista(int index, String[] info) {
    Edge edge = aristas.get(index);
    edge.setParaderoOrigen(info[0]);
    edge.setParaderoDestino(info[1]);
    edge.setTiempo(Integer.parseInt(info[2]));
    edge.addIdBus(Integer.parseInt(info[3]));
    this.aristas.set(index, edge);
  }

  public void addIdBus(int index, int idBus){
    aristas.get(index).addIdBus(idBus);
  }

  public void setAristaMejorId(int index, int idBus){
      aristas.get(index).setMejorIdBus(idBus);
  }


  public void addArista(String[] info) {
    this.aristas.add(getEdge(info));
  }

  public int getAristasSize() {
    return this.aristas.size();
  }


  private Edge getEdge(String[] info) {
    return new Edge(info);
  }

  public int indexIdBus(int idArista, int idBus) {
    Edge arista = aristas.get(idArista);
    for (int i = 0; i < arista.getIdBusesSize(); i++) {
      if (arista.getIdBus(i) == idBus) {
        return i;
      }
    }
    return -1;
  }


}