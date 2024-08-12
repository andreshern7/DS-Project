package backend;

//Está clase solo sirve para representar la información de una arista(conceptualmente)
//Clase Modelo
//ParaderoOrigen
//ParaderoDestino
//TiempoEmpleadoEntreParadas

import java.util.ArrayList;
import java.util.List;

public class Edge{

  private String paraderoOrigen;
  private String paraderoDestino;
  private int tiempo;
  private int mejorIdBus;
  private List<Integer> idBuses = new ArrayList<>();  //Por si se actualiza y el edge pertenece a varias rutas

  //No tiene sentido crear un edge sin información (buena practica de programacion)
  public Edge(String paraderoOrigen, String paraderoDestino, int tiempo, int idBus){
    this.paraderoOrigen = paraderoOrigen;
    this.paraderoDestino = paraderoDestino;
    this.tiempo = tiempo;
    this.idBuses.add(idBus);
    this.mejorIdBus = idBus;
  }

  public Edge(String[] info){
    this.paraderoOrigen = info[0];
    this.paraderoDestino = info[1];
    this.tiempo = Integer.parseInt(info[2]);
    this.idBuses.add(Integer.parseInt(info[3]));
    this.mejorIdBus = Integer.parseInt(info[3]);
  }

  public String getParaderoOrigen() {
    return paraderoOrigen;
  }

  public void setParaderoOrigen(String paraderoOrigen) {
    this.paraderoOrigen = paraderoOrigen;
  }
  public String getParaderoDestino() {
    return paraderoDestino;
  }
  public void setParaderoDestino(String paraderoDestino) {
    this.paraderoDestino = paraderoDestino;
  }

  public int getTiempo() {
    return tiempo;
  }
  public void setTiempo(int tiempo){
    this.tiempo = tiempo;
  }
  public int getIdBus(int index) {
    return this.idBuses.get(index);
  }

  public int getMejorIdBus(){
    return  this.mejorIdBus;
  }

  public List<Integer> getIdBuses() {
    return idBuses;
  }
  public void addIdBus(int idBus) {
    this.idBuses.add(idBus);
  }

  public void setMejorIdBus(int idBus) {
    this.mejorIdBus = idBus;
  }


  public int getIdBusesSize(){
    return idBuses.size();
  }



  public String[] getEdgeInfo(){
    String[] info = {paraderoOrigen, paraderoDestino, String.valueOf(tiempo), idBuses.toString(), String.valueOf(mejorIdBus)};
    return info;
  }


  @Override
  public String toString() {
    return "Edge{" +
            "paraderoOrigen='" + paraderoOrigen + '\'' +
            ", paraderoDestino='" + paraderoDestino + '\'' +
            ", tiempo=" + tiempo +
            ", mejorIdBus=" + mejorIdBus +
            ", idBuses=" + idBuses +
            '}';
  }
}
