package yve;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.graph.Graph;
import yve.control.Controller;

public class RunFX extends Application {

    public static void main(String[] args){
        RunFX.launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(RunFX.class.getResource("/main.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
