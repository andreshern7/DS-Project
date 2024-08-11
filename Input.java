import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Input {

  public static List<String[]> getRutas() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Escribe el nombre del archivo que quieres escanear: ");
    String fileName = scanner.nextLine();  //Nombre del archivo a leer
//    String fileName = "rutas.txt"; //TEST ENVIRONMENT
    if (!fileName.contains(".txt")) {
      fileName = fileName + ".txt";
    }
    List<String[]> rutas = new ArrayList<>();

    // Leer el archivo
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      int numeroRutas = Integer.parseInt(br.readLine().trim());
      if(!(numeroRutas >= 1 && numeroRutas <= 10000)){
        System.out.println("El sistema de transporte publico de Nueva Konigsberg no puede tener más de 10000 rutas");
        System.exit(1);
      }
      for (int i = 0; i < numeroRutas; i++) {
        String[] partesRuta = br.readLine().split(" ");
        if(datosCorrectos(partesRuta)) {
          rutas.add(partesRuta);
        }
      }
    } catch (IOException e) {
      System.out.println("El archivo indicado no se ha encontrado.");
      System.exit(0);
    }
    return rutas;
  }

  private static boolean datosCorrectos(String[] partesRuta){
    int idRuta = Integer.parseInt(partesRuta[0]);
    System.out.println(partesRuta.length);
    if(idRuta < 0){
//      System.out.println("RUTA #" + idRuta + " id invalido");
      return false;
      //identifica cuando una ruta tiene mas de 1000 paraderos
    }if(partesRuta.length > 2000){
//      System.out.println("RUTA #" + idRuta + " muchos paraderos");
      return false;
    }
    //recorrer
    for (int i = 2; i < partesRuta.length; i = i + 2) {
      if(Integer.parseInt(partesRuta[i]) > 1000){
//        System.out.println("RUTA #" + idRuta + " tiempo de viaje muy grande");
        return false;
      }
    }
    return true;
  }
}