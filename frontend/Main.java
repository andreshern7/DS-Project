package frontend;

import backend.Input;
import backend.Routes;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Botón para seleccionar archivo
        Button seleccionarArchivoButton = new Button("Seleccionar Archivo de Rutas");

        // Crear etiquetas y campos de texto para origen y destino
        Label origenLabel = new Label("Paradero de Origen:");
        TextField origenInput = new TextField();

        Label destinoLabel = new Label("Paradero de Destino:");
        TextField destinoInput = new TextField();

        // Crear botón para calcular la ruta
        Button calcularRutaButton = new Button("Calcular Ruta");

        // Crear etiqueta para mostrar el resultado
        Label resultadoLabel = new Label();

        // Variable para almacenar el archivo seleccionado
        final File[] archivoSeleccionado = {null};

        // Acción del botón para seleccionar archivo
        seleccionarArchivoButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Archivo de Rutas");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            archivoSeleccionado[0] = fileChooser.showOpenDialog(primaryStage);

            Alert a = new Alert(AlertType.NONE);

            if(archivoSeleccionado[0] == null) {
                // set alert type
                a.setAlertType(AlertType.ERROR);
                // set content text
                a.setContentText("No ha elegido un archivo");
            }else{
                // set alert type
                a.setAlertType(AlertType.INFORMATION);
                // set content text
                a.setContentText("Se ha cargado el archivo " + archivoSeleccionado[0].getName());
//                System.out.println("Se está cargando el archivo " + archivoSeleccionado[0].getName());  //Solo el nombre
//                System.out.println("Se está cargando el archivo " + archivoSeleccionado[0].toString()); //Todo el path


            }
            // show the dialog
            a.show();
        });

        // Acción del botón para calcular la ruta
        calcularRutaButton.setOnAction(event -> {
            if (archivoSeleccionado[0] != null) {
                // Obtener el origen y destino de la interfaz
                String origen = origenInput.getText();
                String destino = destinoInput.getText();

                // Procesar el archivo seleccionado y obtener las rutas
                List<String[]> rutas = Input.getRutas(archivoSeleccionado[0]);

                // Crear una instancia de Routes y calcular la mejor ruta
                Routes rutasGraph = new Routes(rutas);

                // Usar la clase Path para calcular la mejor ruta y capturar la salida
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                PrintStream ps = new PrintStream(baos);
//                PrintStream old = System.out;
//                System.setOut(ps);

                // Calcular la mejor ruta
                String respuesta = rutasGraph.imprimirMejorRuta(origen, destino);

                // Restaurar la salida estándar
//                System.out.flush();
//                System.setOut(old);

                // Mostrar la salida en el resultadoLabel
//                String resultado = baos.toString();
//                resultadoLabel.
                resultadoLabel.setText(respuesta.isEmpty() ? "No se encontró una ruta entre " + origen + " y " + destino : respuesta);
            } else {
                Alert a = new Alert(AlertType.NONE);
                a.setAlertType(AlertType.ERROR);
                a.setContentText("Por favor selecciona un archivo primero.");
                a.show();
//                resultadoLabel.setText("Por favor selecciona un archivo primero.");
            }
        });

        // Crear un layout y agregar los componentes
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(seleccionarArchivoButton, origenLabel, origenInput, destinoLabel, destinoInput, calcularRutaButton, resultadoLabel);

        // Crear la escena y mostrarla
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Planificador de Rutas");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
