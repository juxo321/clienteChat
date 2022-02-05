import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        TitledPane panelConexion, panelMensajes, panelMensaje;
        TextField txtServidor, txtPuerto, txtApodo;
        TextField txtMensaje;
        TextArea txtMensajes;
        Label lblServidor, lblPuerto, lblApodo;
        Button btnConectar, btnEnviarMensaje;
        HBox contenedorConexion = new HBox();
        lblServidor=new Label ("Servidor:");
        lblPuerto=new Label ("Puerto:");
        lblApodo=new Label ("Apodo:");
        txtServidor = new TextField("localhost");
        txtPuerto = new TextField("4500");
        txtPuerto.setMaxWidth(50);
        txtApodo = new TextField();

        panelConexion = new TitledPane();
        panelConexion.setText("Datos de conexion");
        panelMensajes = new TitledPane();
        panelMensajes.setText("Mensajes");
        panelMensaje = new TitledPane();
        panelMensaje.setText("Mensaje a enviar");
        root.setTop(panelConexion);
        root.setCenter(panelMensajes);
        root.setBottom(panelMensaje);


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
