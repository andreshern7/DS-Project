import java.util.Arrays;
import java.util.List;

public class App {
  
  public static void main(String[] args){

    List<String[]> rutas = Input.getRutas();
//    for(String[] ruta : rutas){
//      System.out.println("Ruta: " + Arrays.toString(ruta));
//    }

    Routes rutasGraph = new Routes(rutas);
//    rutasGraph.imprimirIDRuta();
//    System.out.println("------------------------------RUTAS------------------------------");
//    rutasGraph.imprimirRutas();
    rutasGraph.imprimirMejorRuta();


  }
  
}
