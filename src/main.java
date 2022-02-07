import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class main extends Application {
    private Socket conexion;
    private BufferedReader lector;
    private BufferedWriter escritor;
    private TextField txtServidor, txtPuerto, txtApodo;
    private TextField txtMensaje;
    private TextArea txtMensajes;
    private Button btnConectar, btnEnviarMensaje;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        TitledPane panelConexion, panelMensajes, panelMensaje;
        Label lblServidor, lblPuerto, lblApodo;
        HBox contenedorConexion = new HBox();
        contenedorConexion.setSpacing(10);
        contenedorConexion.setPadding(new Insets(10));
        lblServidor=new Label ("Servidor:");
        lblServidor.setMinHeight(25);
        lblPuerto=new Label ("Puerto:");
        lblPuerto.setMinHeight(25);
        lblApodo=new Label ("Apodo:");
        lblApodo.setMinHeight(25);
        txtServidor = new TextField("localhost");
        txtServidor.setMaxWidth(70);
        txtPuerto = new TextField("4500");
        txtPuerto.setMaxWidth(50);
        txtApodo = new TextField();
        btnConectar=new Button("Conectar");
        btnConectar.setOnAction(event -> {
            conectarServidor(txtServidor.getText(), Integer.parseInt(txtPuerto.getText()),
                    txtApodo.getText());
        });
        contenedorConexion.getChildren()
                .addAll(lblServidor, txtServidor, lblPuerto, txtPuerto,
                        lblApodo, txtApodo,btnConectar);

        panelConexion = new TitledPane();
        panelConexion.setText("Datos de conexion");
        panelConexion.setContent(contenedorConexion);

        txtMensajes = new TextArea();
        txtMensajes.setMinHeight(280);

        panelMensajes = new TitledPane();
        panelMensajes.setText("Mensajes");
        panelMensajes.setContent(txtMensajes);

        HBox contenedorMensaje = new HBox();
        contenedorMensaje.setSpacing(10);
        contenedorMensaje.setPadding(new Insets(10));
        txtMensaje = new TextField();
        txtMensaje.setPromptText("Escribe aquí el mensaje a enviar: ");
        txtMensaje.setMinWidth(400);
        btnEnviarMensaje = new Button("Enviar");
        btnEnviarMensaje.setOnAction(event -> {
            enviarMensaje(txtMensaje.getText());
            txtMensaje.setText("");
            txtMensaje.requestFocus();
        });
        contenedorMensaje.getChildren().addAll(txtMensaje, btnEnviarMensaje);


        panelMensaje = new TitledPane();
        panelMensaje.setText("Mensaje a enviar");
        panelMensaje.setContent(contenedorMensaje);

        root.setTop(panelConexion);
        root.setCenter(panelMensajes);
        root.setBottom(panelMensaje);


        primaryStage.setTitle("Bienvenido al chat");
        primaryStage.setScene(new Scene(root, 680, 500));
        primaryStage.show();
    }
    private void conectarServidor(String servidor, int puerto, String apodo){
        try {
            conexion = new Socket(servidor, puerto);
            lector =
                    new BufferedReader(
                            new InputStreamReader(conexion.getInputStream()));
            escritor =
                    new BufferedWriter(
                            new OutputStreamWriter(conexion.getOutputStream()));
            enviarMensaje(apodo);
        } catch (Exception e){

        }
    }

    public boolean enviarMensaje(String mensaje){
        try {
            escritor.write(mensaje+"\n");
            escritor.flush();
            return true;
        } catch (IOException e) {
            return false;
        }

    }
    public static void main(String[] args) {

        launch(args);
    }
}
