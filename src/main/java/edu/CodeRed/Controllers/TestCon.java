package edu.CodeRed.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class TestCon {

    @FXML
    private ProgressBar progbar;

    public void initialize() {
        // Set the progress of the ProgressBar to one-third (0.33)
        progbar.setProgress(0.33);

        // Apply custom style to change the color to green
        progbar.setStyle("-fx-accent: green;");
    }
}
