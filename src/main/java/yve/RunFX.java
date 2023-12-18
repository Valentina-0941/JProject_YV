package yve;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import yve.control.Controller;

public class RunFX extends Application {
    private static SessionFactory factory;

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        factory = configuration.buildSessionFactory();

        RunFX.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Session session = factory.openSession();
        FXMLLoader loader = new FXMLLoader(RunFX.class.getResource("/main.fxml"));
        AnchorPane root = loader.load();
        Controller controller = loader.getController();
        controller.outerInit(session);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            session.close();
            factory.close();
        });
    }
}
