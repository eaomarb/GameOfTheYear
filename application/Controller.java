package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Controller {
    @FXML
    private GridPane gridPane;
    @FXML
    private Label recordLabel;
    @FXML
    private TextField txtPuntos;
    @FXML
    private Button btnSalir;
    @FXML
    private Button btnReiniciar;

    private ArrayList<String> simbolos;
    private int puntos;
    private int record;
    private boolean juegoTerminado;
    private String archivo = "puntos.txt";

    @FXML
    public void initialize() {
	puntos = 0;
	record = 0;
	juegoTerminado = false;
	btnReiniciar.setText("Empezar");

	cargarRecord();
	actualizarLabels();

	btnSalir.setOnAction(e -> salir());

	btnReiniciar.setOnAction(e -> {
	    crearSimbolos();
	    crearTablero();
	    puntos = 0;
	    juegoTerminado = false;
	    btnReiniciar.setText("Reiniciar");
	    actualizarLabels();
	});
    }

    private void cargarRecord() {
	try {
	    File file = new File(archivo);
	    if (file.exists()) {
		Scanner scanner = new Scanner(file);
		if (scanner.hasNextInt()) {
		    record = scanner.nextInt();
		}
		scanner.close();
	    }
	} catch (Exception e) {
	    record = 0;
	}
    }

    private void crearSimbolos() {
	simbolos = new ArrayList<>();
	Random random = new Random();
	int w = random.nextInt(3) + 1;
	int x = 2;
	int o = 16 - w - x;

	for (int i = 0; i < o; i++)
	    simbolos.add("O");

	for (int i = 0; i < w; i++)
	    simbolos.add("W");

	for (int i = 0; i < x; i++)
	    simbolos.add("X");

	Collections.shuffle(simbolos);
    }

    private void crearTablero() {
	gridPane.getChildren().clear();

	for (int fila = 0; fila < 4; fila++) {
	    for (int col = 0; col < 4; col++) {
		int pos = fila * 4 + col;
		String simbolo = simbolos.get(pos);

		Button button = new Button("?");
		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		button.setOnAction(e -> click(button, simbolo));

		gridPane.add(button, col, fila);
	    }
	}
    }

    private void click(Button boton, String simbolo) {
	if (!juegoTerminado) {
	    boton.setText(simbolo);
	    boton.setDisable(true);

	    if (simbolo.equals("O")) {
		puntos = puntos + 1;
	    } else if (simbolo.equals("W")) {
		puntos = puntos * 2;
	    } else if (simbolo.equals("X")) {
		juegoTerminado = true;
		actualizarRecord();
		btnReiniciar.setText("Empezar");
		bloquearPanel();
	    }

	    actualizarLabels();
	}

    }

    private void actualizarRecord() {
	if (puntos > record) {
	    record = puntos;
	    try {
		PrintWriter writer = new PrintWriter(archivo);
		writer.println(record);
		writer.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    private void bloquearPanel() {
	for (int i = 0; i < gridPane.getChildren().size(); i++) {
	    Button button = (Button) gridPane.getChildren().get(i);
	    button.setDisable(true);
	}
    }

    private void actualizarLabels() {
	txtPuntos.setText(String.valueOf(puntos));
	recordLabel.setText(String.valueOf(record));
    }

    private void salir() {
	Alert alerta = new Alert(Alert.AlertType.INFORMATION);
	alerta.setHeaderText(null);
	alerta.setContentText("Has hecho " + puntos + " puntos");
	alerta.showAndWait();
	Platform.exit();
    }
}
