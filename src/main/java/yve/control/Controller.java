package yve.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import yve.entities.Progress;

public class Controller {

    @FXML
    private Tab genderGraph;

    @FXML
    private PieChart percentageGender;

    @FXML
    private PieChart percentageGenderIndicated;

    @FXML
    private AnchorPane root;

    @FXML
    private BarChart<String, Number> barChart;

    public void initialize() {




        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart.setTitle("Челы-Баллы");
        xAxis.setLabel("Чел");
        yAxis.setLabel("Баллы");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("2003");
        series1.getData().add(new XYChart.Data<>("Чел_1", 25601.34));
        series1.getData().add(new XYChart.Data<>("Чел_2", 20148.82));
        series1.getData().add(new XYChart.Data<>("Чел_3", 10000));
        series1.getData().add(new XYChart.Data<>("Чел_4", 35407.15));
        series1.getData().add(new XYChart.Data<>("Чел_5", 12000));

        barChart.getData().add(series1);
    }

}
