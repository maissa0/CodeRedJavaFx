package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.RecetteService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatistiquesController implements Initializable {
    @FXML
    private LineChart<?, ?> lineChartRecette;

    @FXML
    private AnchorPane statPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statistique();
    }

    public void statistique() {
        RecetteService cs = new RecetteService();

        List<Recette> recettes = null;
        recettes = cs.getAllDataRecette();



        // Créer les axes pour le graphique
        final NumberAxis yAxis = new NumberAxis();
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Nom Recette");
        yAxis.setLabel("Calories");

        // Créer la série de données à afficher
        XYChart.Series series = new XYChart.Series();
        series.setName("Statistiques des recettes selon leurs calories");
        for (Recette cour : recettes) {
            series.getData().add(new XYChart.Data<>(cour.getNom(), cour.getCalorieRecette()));
        }

        // Créer le graphique et ajouter la série de données
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Statistiques des recettes");
        lineChart.getData().add(series);

        // Afficher le graphique dans votre scène
        lineChartRecette.setCreateSymbols(false);
        lineChartRecette.getData().add(series);

    }
}
