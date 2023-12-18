package yve.control;

import jakarta.persistence.Tuple;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.Type;
import yve.entities.*;

import javax.lang.model.element.TypeElement;
import java.util.*;

public class Controller {
    private Session session;

    @FXML
    private Tab Graph1;
    @FXML
    private PieChart VkPage;
    @FXML
    private PieChart Gender;
    @FXML
    private PieChart PageIsClosed;

    @FXML
    private MenuButton listTheme;
    @FXML
    private BarChart<String, Number> progress;
    @FXML
    private AnchorPane root;

    public void outerInit(Session session) {
        this.session = session;

        createVkPage(session);
        createGender(session);
        createIsClosed(session);


        List<String> themes = session.createQuery("SELECT DISTINCT t.nameTheme FROM Task t", String.class).list();

        MenuItem[] items = new MenuItem[themes.size()];
        for (int i = 0; i < themes.size(); i++) {
            items[i] = new MenuItem(themes.get(i));
            final int index = i;
            items[i].setOnAction(event -> {
                listTheme.setText(items[index].getText());
                updateBarChartData(items[index].getText());
            });
        }
        listTheme.getItems().addAll(items);
        filleBarGraph(session, themes);

    }

    private void updateBarChartData(String theme) {
        progress.getData().clear();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        progress.setTitle("Прогресс по темам");
        xAxis.setLabel("Theme");
        yAxis.setLabel("Score");

        Long maxScoreForTheme = session.createQuery(
                "SELECT SUM(maxScore) FROM Task WHERE nameTheme = :theme GROUP BY nameTheme", Long.class
        ).setParameter("theme", theme).getSingleResult();

        Query<Object[]> maxScorePerTypeForTheme = session.createQuery(
                "SELECT type, SUM(maxScore) AS TotalMaxScore FROM Task WHERE nameTheme = :theme " +
                        "GROUP BY nameTheme, type", Object[].class
        ).setParameter("theme", theme);
        System.out.println(maxScorePerTypeForTheme);

        Double scoreForTheme = session.createQuery(
                "SELECT AVG(sub.total) FROM (" +
                        "SELECT SUM(p.score) AS total FROM Progress p WHERE p.task.nameTheme = :theme GROUP BY p.student)" +
                        "AS sub", Double.class
        ).setParameter("theme", theme).getSingleResult();

        Query<Object[]> scorePerTypeForTheme = session.createQuery(
                "SELECT p.task.type, SUM(p.score) AS AverageScore FROM Task t " +
                        "JOIN Progress p ON t.id = p.task.id " +
                        "WHERE p.task.nameTheme = :theme " +
                        "GROUP BY p.task.nameTheme, p.task.type", Object[].class
        ).setParameter("theme", theme);

        System.out.println(maxScorePerTypeForTheme);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("1");

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("2");

        series1.getData().add(new XYChart.Data<>(theme, maxScoreForTheme));
        series2.getData().add(new XYChart.Data<>(theme, scoreForTheme));

        for (Object[] item : maxScorePerTypeForTheme.list()){
            series1.getData().add(new XYChart.Data<>((String) item[0], (Number) item[1]));
        }
        for (Object[] item : scorePerTypeForTheme.list()){
            double i = (double) Integer.parseInt(item[1].toString()) / 1030;
            series2.getData().add(new XYChart.Data<>((String) item[0], (Number) i));
        }
        progress.getData().addAll(series1, series2);
    }

    private void filleBarGraph(Session session, List<String> themes) {
        List<Object[]> maxScoreForTheme = session.createQuery(
                "SELECT nameTheme, SUM(maxScore) FROM Task GROUP BY nameTheme", Object[].class
        ).list();

        Query<Double> scorePerTypeForTheme = session.createQuery(
                "SELECT AVG(sub.total) FROM (" +
                        "SELECT SUM(p.score) AS total FROM Progress p WHERE p.task.nameTheme = :theme GROUP BY p.student)" +
                        "AS sub", Double.class
        );
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        progress.setTitle("Прогресс по темам");
        xAxis.setLabel("Theme");
        yAxis.setLabel("Score");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Макс");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series1.setName("Ср");

        for (Object[] objects : maxScoreForTheme) {
            series1.getData().add(new XYChart.Data<>((String) objects[0], (Number) objects[1]));
        }
        for (String theme : themes) {
            double i = scorePerTypeForTheme.setParameter("theme", theme).getSingleResult();
            series2.getData().add(new XYChart.Data<>(theme, i));
        }
        progress.getData().addAll(series1, series2);
    }

    private void createVkPage(Session session) {
        Query<Long> yVk = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.vkId IS NOT NULL", Long.class);
        Query<Long> nVk = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.vkId IS NULL", Long.class);

        ObservableList<PieChart.Data> dataVkPAge = FXCollections.observableArrayList(
                new PieChart.Data("Yes", yVk.getSingleResult()),
                new PieChart.Data("No", nVk.getSingleResult())
        );

        VkPage.setTitle("VK Page");
        VkPage.setData(dataVkPAge);
    }

    private void createGender(Session session) {
        Query<Long> mSex = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.vkId IS NOT NULL AND s.sex = 'М'", Long.class);
        Query<Long> wSex = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.vkId IS NOT NULL AND s.sex = 'Ж'", Long.class);
        Query<Long> nSex = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.vkId IS NOT NULL AND s.sex = 'Н'", Long.class);

        ObservableList<PieChart.Data> dataGender = FXCollections.observableArrayList(
                new PieChart.Data("Men", mSex.getSingleResult()),
                new PieChart.Data("Women", wSex.getSingleResult()),
                new PieChart.Data("None", nSex.getSingleResult())
        );
        Gender.setTitle("Gender");
        Gender.setData(dataGender);
    }

    private void createIsClosed(Session session) {
        Query<Long> isClosed = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.vkId IS NOT NULL AND s.isClosed = true", Long.class);
        Query<Long> isNotClosed = session.createQuery("SELECT COUNT(s) FROM Student s WHERE s.vkId IS NOT NULL AND s.isClosed = false", Long.class);

        ObservableList<PieChart.Data> dataGender = FXCollections.observableArrayList(
                new PieChart.Data("Closed", isClosed.getSingleResult()),
                new PieChart.Data("Not closed", isNotClosed.getSingleResult())
        );
        PageIsClosed.setTitle("Page VK is closed");
        PageIsClosed.setData(dataGender);
    }
}
