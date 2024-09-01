package frontend;

import backend.Input;
import backend.Routes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Configurar la interfaz
        VBox layout = configurarInterfaz(primaryStage);

        // Crear la escena y aplicar el CSS
        Scene scene = new Scene(layout, 600, 400); // Tamaño inicial mayor para mejor adaptabilidad
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Planificador de Rutas");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.show();
    }

    private VBox configurarInterfaz(Stage primaryStage) {
        // Botones y campos de texto
        Button seleccionarArchivoButton = new Button("Seleccionar Archivo de Rutas");
        Label archivoSeleccionadoLabel = new Label("Archivo seleccionado: Ninguno");
        TextField origenInput = new TextField();
        TextField destinoInput = new TextField();
        Button calcularRutaButton = new Button("Calcular Ruta");
        Label resultadoLabel = new Label();
        resultadoLabel.setWrapText(true);  // Permitir que el texto se ajuste a la ventana


        // ScrollPane para el resultado
        ScrollPane scrollPane = new ScrollPane(resultadoLabel);
        scrollPane.setFitToWidth(true);  // Ajustar el ancho del scroll pane al contenido


        File[] archivoSeleccionado = {null};

        // Barra de progreso
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false); // Inicialmente oculta

        configurarBotones(seleccionarArchivoButton, archivoSeleccionadoLabel, calcularRutaButton, origenInput, destinoInput, resultadoLabel, archivoSeleccionado, progressIndicator);

        // Layout para los campos de origen y destino
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        Label origenLabel = new Label("Paradero de Origen:");
        Label destinoLabel = new Label("Paradero de Destino:");
        origenInput.setPromptText("Ingrese el origen");
        destinoInput.setPromptText("Ingrese el destino");

        // Asegurar que los campos de texto crezcan con el tamaño de la ventana
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        gridPane.getColumnConstraints().addAll(col1, col2);

        gridPane.add(origenLabel, 0, 0);
        gridPane.add(origenInput, 1, 0);
        gridPane.add(destinoLabel, 0, 1);
        gridPane.add(destinoInput, 1, 1);

        // Layout principal
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(
                seleccionarArchivoButton,
                archivoSeleccionadoLabel,
                gridPane,
                calcularRutaButton,
                progressIndicator,
                scrollPane  // Usar el ScrollPane en lugar del Label directamente
        );

        // Hacer que los elementos crezcan con el espacio disponible
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return layout;
    }

    private void configurarBotones(Button seleccionarArchivoButton, Label archivoSeleccionadoLabel, Button calcularRutaButton,
                                   TextField origenInput, TextField destinoInput,
                                   Label resultadoLabel, File[] archivoSeleccionado,
                                   ProgressIndicator progressIndicator) {
        // Acción del botón para seleccionar archivo
        seleccionarArchivoButton.setOnAction(event -> seleccionarArchivo(archivoSeleccionado, archivoSeleccionadoLabel));

        // Acción del botón para calcular la ruta
        calcularRutaButton.setOnAction(event -> {
            if (archivoSeleccionado[0] == null) {
                mostrarAlerta(true, "Error", "No ha seleccionado un archivo.");
                return;
            }

            String origen = origenInput.getText().trim();
            String destino = destinoInput.getText().trim();

            if (origen.isEmpty() || destino.isEmpty()) {
                mostrarAlerta(true, "Error", "Por favor, ingrese tanto el origen como el destino.");
                return;
            }

            // Crear una tarea para calcular la ruta
            Task<String> tareaCalculo = new Task<>() {
                @Override
                protected String call() throws Exception {
                    // Simular un cálculo que toma tiempo
                    Thread.sleep(550); // Simulación de espera

                    List<String[]> rutas = Input.getRutas(archivoSeleccionado[0]);
                    Routes rutasGraph = new Routes(rutas);
                    String respuesta = rutasGraph.imprimirMejorRuta(origen, destino);
                    return respuesta.isEmpty() ? "No se encontró una ruta entre " + origen + " y " + destino : respuesta;
                }
            };

            // Mostrar el indicador de progreso
            progressIndicator.setVisible(true);
            resultadoLabel.setText(""); // Limpiar resultado anterior

            // Manejar el resultado de la tarea
            tareaCalculo.setOnSucceeded(e -> {
                String resultado = tareaCalculo.getValue();
                resultadoLabel.setText(resultado);
                progressIndicator.setVisible(false);
            });

            tareaCalculo.setOnFailed(e -> {
                Throwable ex = tareaCalculo.getException();
                mostrarAlerta(true, "Error durante el cálculo", "Ocurrió un error al calcular la ruta. Por favor, revise los datos y vuelva a intentarlo.");
                ex.printStackTrace(); // Para depuración
                progressIndicator.setVisible(false);
            });

            // Ejecutar la tarea en un hilo separado
            Thread hilo = new Thread(tareaCalculo);
            hilo.setDaemon(true); // Para que el hilo se cierre al cerrar la aplicación
            hilo.start();
        });
    }

    private void seleccionarArchivo(File[] archivoSeleccionado, Label archivoSeleccionadoLabel) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Archivo de Rutas");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File archivo = fileChooser.showOpenDialog(null);
            if (archivo != null) {
                archivoSeleccionado[0] = archivo;
                archivoSeleccionadoLabel.setText("Archivo seleccionado: " + archivo.getName());
                mostrarAlerta(false, "Archivo Cargado", "Se ha cargado el archivo " + archivo.getName());
            } else {
                mostrarAlerta(true, "No ha elegido un archivo", "No se ha seleccionado ningún archivo.");
            }
        } catch (Exception e) {
            mostrarAlerta(true, "Error al seleccionar archivo", "Ocurrió un error al intentar seleccionar el archivo. Por favor, inténtelo de nuevo.");
            e.printStackTrace(); // Para depuración
        }
    }

    private void mostrarAlerta(boolean isError, String mensajeTitulo, String mensajeContenido) {
        Platform.runLater(() -> { // Asegurar que se ejecuta en el hilo de la UI
            Alert a = new Alert(isError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
            a.setTitle(mensajeTitulo);
            a.setHeaderText(null);
            a.setContentText(mensajeContenido);
            a.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
