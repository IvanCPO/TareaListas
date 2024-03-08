package org.a22ivancp.tareatontisima;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.a22ivancp.tareatontisima.datamodel.TarefaFicheiro;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ventaPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 250);
        stage.setTitle("Tarea List");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void init() throws Exception {
        try {
            TarefaFicheiro.getFicheiroInstance().lerTarefas();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void stop() throws Exception {
        TarefaFicheiro.getFicheiroInstance().gardarTarefas();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}