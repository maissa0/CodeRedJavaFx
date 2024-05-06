package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Journal;
import edu.CodeRed.services.JournalService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    private final JournalService journalService = new JournalService();

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    private static int selectedJournalId;

    public void setSelectedJournalId(int id) {
        this.selectedJournalId = id;
    }

    // Method to get the selected journal's ID

    public int getSelectedJournalId() {
        return this.selectedJournalId;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private int getJournalIdFromDate(ZonedDateTime date, int dayOfMonth) {
        // Fetch journal entries for the current date from the database
        List<Journal> journalEntries = journalService.getJournalEntriesForDate(date.withDayOfMonth(dayOfMonth));
        // If there are journal entries, return the ID of the first entry (assuming one entry per day)
        if (!journalEntries.isEmpty()) {
            return journalEntries.get(0).getId();
        }
        // Return -1 if no journal entry exists for the clicked date
        return -1;
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        int monthMaxDate = dateFocus.getMonth().maxLength();
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.WHITE);
                rectangle.setStroke(Color.WHITE);
                rectangle.setStrokeWidth(1.5);
                rectangle.setArcWidth(10); // Set arc width for rounded corners
                rectangle.setArcHeight(10); // Set arc height for rounded corners
                double rectangleWidth = (calendar.getPrefWidth() / 7);
                double rectangleHeight = (calendar.getPrefHeight() / 6);
                rectangle.setWidth(rectangleWidth);
                rectangle.setHeight(rectangleHeight);

                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.rgb(40, 97, 85)); // Set shadow color
                shadow.setWidth(10); // Set shadow width
                shadow.setHeight(10); // Set shadow height
                rectangle.setEffect(shadow);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text dateText = new Text(String.valueOf(currentDate));
                        stackPane.getChildren().add(dateText);

                        dateText.setStyle("-fx-font-size: 20");

                        // Set color for today's date
                        if (dateFocus.getYear() == today.getYear() && dateFocus.getMonth() == today.getMonth() && currentDate == today.getDayOfMonth()) {
                            dateText.setFill(Color.rgb(203, 107, 22));
                            dateText.setStyle("-fx-font-weight: bold");
                            dateText.setStyle("-fx-font-size: 24");

                        }

                        // Fetch journal entries for the current date from the database
                        List<Journal> journalEntries = journalService.getJournalEntriesForDate(dateFocus.withDayOfMonth(currentDate));
                        if (!journalEntries.isEmpty()) {
                            int totalCalories = journalEntries.stream().mapToInt(Journal::getCaloriesJournal).sum();
                            Text caloriesText = new Text("Calories: " + totalCalories);
                            StackPane.setMargin(caloriesText, new Insets(50, 5, 5, 5)); // Adjust margins to position calories text
                            stackPane.getChildren().add(caloriesText);

                            // Add event handlers to display journal information when clicked
                            dateText.setOnMouseClicked(event -> {
                                int journalId = getJournalIdFromDate(dateFocus, currentDate);
                                // Set the selected journal's ID
                                setSelectedJournalId(journalId);

                                // Print the selected journal's ID (for testing purposes)
                                System.out.println(getSelectedJournalId());
                                try {
                                    openDetails();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            caloriesText.setOnMouseClicked(event -> {
                                int journalId = getJournalIdFromDate(dateFocus, currentDate);
                                // Set the selected journal's ID
                                setSelectedJournalId(journalId);
                                // Print the selected journal's ID (for testing purposes)
                                System.out.println(getSelectedJournalId());
                                try {
                                    openDetails();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }



    void openDetails()throws IOException {
        // Retrieve the selected journal ID directly from the instance variable
        int selectedId = getSelectedJournalId(); // Retrieve the selected journal ID
        System.out.println("Opening details for journal ID: " + selectedId); // Print selected journal ID

        // Debug: Print the retrieved ID from CalendarController
        System.out.println("Calendar Journal ID: " + selectedId);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/showDetailsJournalCal.fxml"));
        Parent root = loader.load();
        //showDetailsJournalCalController controller = loader.getController();
        // Pass the selected journal ID to the controller
        //controller.setSelectedJournalId(selectedId);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
    }



}
