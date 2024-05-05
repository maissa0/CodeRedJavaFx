package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.RecetteService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private PieChart CategoriePieChart;
    private RecetteService rs = new RecetteService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updatePieChart();

    }

    private void updatePieChart() {
        if (CategoriePieChart != null) {
            CategoriePieChart.getData().clear();
            Map<String, Integer> categorieCounts = rs.getingredientCounts();
            for (String categorie : categorieCounts.keySet()) {
                PieChart.Data slice = new PieChart.Data(categorie, categorieCounts.get(categorie));
                CategoriePieChart.getData().add(slice);
            }
        }
    }
}
