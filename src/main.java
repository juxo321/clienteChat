import javafx.application.Application;
import javafx.application.Platform;
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
import java.util.Optional;

public class main extends Application {
    private boolean conectado=false;
    private Socket conexion;
    private BufferedReader lector;
    private BufferedWriter escritor;
    private TextField txtServidor, txtPuerto, txtApodo;
    private TextField txtMensaje;
    private TextArea txtMensajes;
    private Button btnConectar, btnEnviarMensaje;
    private HiloEscuchaMensajes hilo;

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
            String apodo = txtApodo.getText();
            if (apodo.trim().length() > 0)
                 conectarServidor(txtServidor.getText(), Integer.parseInt(txtPuerto.getText()),
                    txtApodo.getText());
            else {
                Alert dialogoError = new Alert(Alert.AlertType.ERROR);
                dialogoError.setTitle("ERROR!");
                dialogoError.setHeaderText("Datos insuficientes");
                dialogoError.setContentText("Debe poner el apodo o nombre de usuario para conectarse");
                dialogoError.showAndWait();
            }
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

        habilitar_conexion(true);
        habilitar_envio_mensaje(false);

        primaryStage.setTitle("Bienvenido cliente de Chat FEI");
        primaryStage.setScene(new Scene(root, 680, 500));
        primaryStage.show();

        hilo = new HiloEscuchaMensajes();
        hilo.start();
    }

    private void habilitar_conexion (boolean hab){
        txtServidor.setDisable(!hab); //negacion
        txtPuerto.setDisable(!hab);
        txtApodo.setDisable(!hab);
        if (hab)
            btnConectar.setText("Conectar");
        else
            btnConectar.setText("Desconectar");
    }

    private void habilitar_envio_mensaje (boolean hab){
        txtMensaje.setDisable(!hab); //negacion
        btnEnviarMensaje.setDisable(!hab);
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
            if (enviarMensaje(apodo)){
                conectado = true;
                habilitar_conexion(false);
                habilitar_envio_mensaje(true);
            }else
                throw new IOException("");

        } catch (Exception e){
            Alert dialogoError = new Alert(Alert.AlertType.ERROR);
            dialogoError.setTitle("ERROR!");
            dialogoError.setHeaderText("Existen problemas de conexión");
            dialogoError.setContentText("Revisa tu conexión con el Servidor");
            dialogoError.showAndWait();
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

    class HiloEscuchaMensajes extends Thread{
        public void run(){
            while (true){
                try{
                    String mensaje = lector.readLine();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            txtMensajes.appendText(mensaje + "\n");
                        }
                    });
                }catch (Exception e){

                }
            }
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}
