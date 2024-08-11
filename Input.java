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
      for (int i = 0; i < numeroRutas; i++) {
        String[] partesRuta = br.readLine().split(" ");
        int idRuta = Integer.parseInt(partesRuta[0]);
        rutas.add(partesRuta);
      }
    } catch (IOException e) {
      System.out.println("El archivo indicado no se ha encontrado.");
      System.exit(0);
    }
    return rutas;
  }
}