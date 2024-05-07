import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import java.io.*;
import java.util.*;

public class MainMenuJFX extends Application {

    private static final String SLSFILE = "SLS2024.txt";

    private ContestList contestList;
    private SkaterList skaterList;
    private SkaterScore skaterScore;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        contestList = new ContestList();
        skaterList = new SkaterList();
        skaterScore = new SkaterScore();

        loadDataFromFile(); // Load data from the file

        primaryStage.setTitle("Street League Skateboarding 2024");

        VBox layout = new VBox();
        layout.setSpacing(10);

        // Creating buttons
        Button addContestButton = new Button("Add contest");
        Button addAthleteButton = new Button("Add athlete");
        Button addScoresButton = new Button("Add/Update scores");
        Button viewContestStandingsButton = new Button("View all standings ");
        Button viewOneContestStandingsButton = new Button("View one standing");
        Button viewAllContestInfoButton = new Button("View all contests");
        Button viewOneContestInfoButton = new Button("View one contest");
        Button viewAllAthleteInfoButton = new Button("View all athletes");
        Button viewOneAthleteInfoButton = new Button("View one athlete");
        Button deleteOneContestButton = new Button("Remove a contest");
        Button deleteOneSkaterButton = new Button("Remove an athlete");
        Button exitButton = new Button("Exit application");

        // Applying CSS styles to buttons
        addContestButton.getStyleClass().add("button");
        addAthleteButton.getStyleClass().add("button");
        addScoresButton.getStyleClass().add("button");
        viewContestStandingsButton.getStyleClass().add("button");
        viewOneContestStandingsButton.getStyleClass().add("button");
        viewAllContestInfoButton.getStyleClass().add("button");
        viewOneContestInfoButton.getStyleClass().add("button");
        viewAllAthleteInfoButton.getStyleClass().add("button");
        viewOneAthleteInfoButton.getStyleClass().add("button");
        deleteOneContestButton.getStyleClass().add("button");
        deleteOneSkaterButton.getStyleClass().add("button");
        exitButton.getStyleClass().add("button");

        // Adding actions to buttons
        addContestButton.setOnAction(e -> addContest(primaryStage));
        addAthleteButton.setOnAction(e -> addAthlete(primaryStage));
        addScoresButton.setOnAction(e -> addScores(primaryStage));
        viewContestStandingsButton.setOnAction(e -> viewContestStandings(primaryStage));
        viewOneContestStandingsButton.setOnAction(e -> viewOneContestStandings(primaryStage));
        viewAllContestInfoButton.setOnAction(e -> viewAllContestInfo(primaryStage));
        viewOneContestInfoButton.setOnAction(e -> viewOneContestInfo(primaryStage));
        viewAllAthleteInfoButton.setOnAction(e -> viewAllAthleteInfo(primaryStage));
        viewOneAthleteInfoButton.setOnAction(e -> viewOneAthleteInfo(primaryStage));
        deleteOneContestButton.setOnAction(e -> deleteOneContest(primaryStage));
        deleteOneSkaterButton.setOnAction(e -> deleteOneSkater(primaryStage));
        exitButton.setOnAction(e -> {
            saveDataToFile(); // Save data to file
            primaryStage.close();
        });

        // Adding buttons to layout
        layout.getChildren().addAll(
                addContestButton, addAthleteButton, addScoresButton,
                viewContestStandingsButton, viewOneContestStandingsButton,
                viewAllContestInfoButton, viewOneContestInfoButton,
                viewAllAthleteInfoButton, viewOneAthleteInfoButton,
                deleteOneContestButton, deleteOneSkaterButton, exitButton
        );

        // Load CSS
        String cssPath = getClass().getResource("style.css").toExternalForm();
        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(cssPath);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void displayMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //      LOAD FILE DATA
    public void loadDataFromFile() {
        File file = new File(SLSFILE); // This will always be the name of the file
        try {
            if (!file.exists()) { // if the file does not exist, create a new file
                file.createNewFile();
                System.out.println("File created: " + SLSFILE);
            }
            Scanner scanner = new Scanner(file); // We use the scanner to look through the text in the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine(); // each line of the file
                String[] parts = line.split(":"); // splitting the data by the colon into two parts
                String type = parts[0].trim(); // this will be the switch case
                String data = parts[1].trim(); // this will be the data we use in the application

                switch (type) {
                    case "Destination":
                        String[] contestData = data.split(","); // splitting the data on the comma into two parts
                        String location = contestData[0].trim(); // first part will be the location
                        String date = contestData[1].trim(); // second part will be the date
                        contestList.addContest(new Contest(location, date)); // create a new contest with this information
                        break;
                    case "Skater":
                        String[] skaterData = data.split(","); // splitting the data on the comma into 4 parts
                        String name = skaterData[0].trim(); // first part is the name of the skater
                        String stance = skaterData[1].trim(); // this will be the stance
                        String nationality = skaterData[2].trim(); // this will be the nationality
                        String gender = skaterData[3].trim(); // this will be the gender
                        // SINCE WE CHECK THE DATA IS CORRECTLY FORMATTED INSIDE THE APPLICATION, NO CHECKS ARE REQUIRED HERE
                        skaterList.addSkater(new Skater(name, stance, nationality, gender)); // create a new skater with this information
                        break;
                    case "Scores":
                        String[] scoresData = data.split(",", 3); // Limit the split to 3 parts as the array also uses comma
                        if (scoresData.length < 3) { // if we have less than three parts of data throw an error
                            System.out.println("Invalid Scores data: " + data);
                            break; // this was mainly used for debugging
                        }
                        String skaterName = scoresData[0].trim(); // the name is the first part of the data
                        String contestLocation = scoresData[1].trim(); // the contest location is the second
                        String scores = scoresData[2].trim(); // and the scores are the third, but they are a String here

                        // Convert the scores string to an array of Double[]
                        String[] scoresStringArray = scores.substring(1, scores.length()-1).split(", "); // return a string that is a substring of our string and split it at the commas
                        Double[] scoresArray = new Double[scoresStringArray.length]; // create a new Double[] with the length of our string array
                        for (int i = 0; i < scoresStringArray.length; i++) { // parse each part of the string into a Double and add it to our score array
                            scoresArray[i] = Double.parseDouble(scoresStringArray[i]);
                        }

                        // Find the skater and contest
                        Skater skater = skaterList.getSkaterByName(skaterName); // since the skater and contest information has already been read
                        Contest contest = contestList.getContestByName(contestLocation); // we need to get this information by its name using a simple getXByName method

                        // Add the scores to the skater
                        if (skater != null && contest != null) { // checking to see if the skater and contest exists
                            System.out.println("Adding scores to skater: " + skater.getName() + " for contest: " + contest.getLocation()); // Print the skater's name and contest location
                            skater.addScores(contest, scoresArray); // add the scores to the skater for that contest
                        } else { // if they are not found then print an error
                            System.out.println("Skater or contest not found: " + skaterName + ", " + contestLocation);
                        }
                        break;
                    default:
                        System.out.println("Unknown data type in file: " + type); // this should never occur due to the checks we have in place in our application, but this is here just in case
                }
            }
            scanner.close(); // Close the scanner after use to prevent resource leaks
        } catch (IOException e) { // If for some reason we cannot load the file
            System.out.println("An error occurred while loading data from file: " + e.getMessage());
        }
    }


    //      SAVE FILE DATA
    public void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SLSFILE))) {
            // Save contest details to the file
            for (int i = 1; i <= contestList.getTotal(); i++) {
                writer.println("Destination: " + contestList.getContest(i).getLocation() + ", "
                        + contestList.getContest(i).getDate());
            }

            // Save skater details to the file
            for (int i = 1; i <= skaterList.getTotal(); i++) { // looping for the length of the skater list
                writer.println("Skater: " + skaterList.getSkater(i).getName()
                        // "Skater: " will be used to sort the data in the file loading method
                        + ", " + skaterList.getSkater(i).getStance() + ", "
                        + skaterList.getSkater(i).getNationality() + ", "
                        + skaterList.getSkater(i).getGender()); // again using the PrintWriter to write the data to our file
            }

            // Save scores to the file
            for (int i = 1; i <= contestList.getTotal(); i++) {
                Contest contest = contestList.getContest(i);
                for (int j = 1; j <= skaterList.getTotal(); j++) {
                    Skater skater = skaterList.getSkater(j);
                    Double[] scores = skater.getScores(contest);
                    if (scores != null && scores.length > 0) {
                        String scoresString = Arrays.toString(scores);
                        writer.println("Scores: " + skater.getName() + ", " + contest.getLocation() + ", " + scoresString);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    //      OPTION 1
    public void addContest(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField locationField = new TextField();
        locationField.setPromptText("Contest Location");
        TextField dateField = new TextField();
        dateField.setPromptText("Contest Date (DD/MM/YYYY)");

        gridPane.add(locationField, 0, 0);
        gridPane.add(dateField, 0, 1);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Contest");
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String location = locationField.getText();
                String date = dateField.getText();
                if (isValidDate(date)) {
                    // Add the contest to contestList
                    boolean addedSuccessfully = contestList.addContest(new Contest(location, date));
                    showResultPopup(addedSuccessfully);
                } else {
                    Alert invalidDateAlert = new Alert(Alert.AlertType.ERROR);
                    invalidDateAlert.setTitle("Invalid Date");
                    invalidDateAlert.setHeaderText(null);
                    invalidDateAlert.setContentText("Please enter a valid date in the format DD/MM/YYYY between 2010 and 2025.");
                    invalidDateAlert.showAndWait();
                }
            }
        });
    }

    private boolean isValidDate(String date) {
        String dateRegex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((20)1[0-9]|202[0-5])$";
        return Pattern.matches(dateRegex, date);
    }

    private void showResultPopup(boolean addedSuccessfully) {
        Alert resultAlert = new Alert(addedSuccessfully ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        resultAlert.setTitle("Result");
        resultAlert.setHeaderText(null);
        resultAlert.setContentText(addedSuccessfully ? "Contest added successfully!" : "Failed to add contest!");
        resultAlert.showAndWait();
    }


    //      OPTION 2
    public void addAthlete(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField stanceField = new TextField();
        stanceField.setPromptText("Stance (Regular/Goofy)");
        TextField nationalityField = new TextField();
        nationalityField.setPromptText("Nationality (3 Letters)");
        TextField genderField = new TextField();
        genderField.setPromptText("Gender (Male/Female)");

        gridPane.add(nameField, 0, 0);
        gridPane.add(stanceField, 0, 1);
        gridPane.add(nationalityField, 0, 2);
        gridPane.add(genderField, 0, 3);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Athlete");
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String name = nameField.getText();
                String stance = stanceField.getText().toLowerCase(); // Convert to lowercase for case-insensitive comparison
                String nationality = nationalityField.getText();
                String gender = genderField.getText().toLowerCase(); // Convert to lowercase for case-insensitive comparison

                boolean validName = name.matches("[a-zA-Z]+");
                boolean validStance = stance.equals("regular") || stance.equals("goofy");
                boolean validNationality = nationality.length() == 3 && nationality.matches("[a-zA-Z]+");
                boolean validGender = gender.equals("male") || gender.equals("female");

                if (validName && validStance && validNationality && validGender) {
                    // Add the athlete to skaterList
                    boolean addedSuccessfully = skaterList.addSkater(new Skater(name, stance, nationality, gender));
                    showAthleteResultPopup(addedSuccessfully);
                } else {
                    Alert invalidInputAlert = new Alert(Alert.AlertType.ERROR);
                    invalidInputAlert.setTitle("Invalid Input");
                    invalidInputAlert.setHeaderText(null);
                    invalidInputAlert.setContentText("Please enter valid inputs:\n" +
                            "Name should only contain letters.\n" +
                            "Stance should be 'Regular' or 'Goofy'.\n" +
                            "Nationality should be exactly 3 letters long.\n" +
                            "Gender should be 'Male' or 'Female'.");
                    invalidInputAlert.showAndWait();
                }
            }
        });
    }

    private void showAthleteResultPopup(boolean addedSuccessfully) {
        Alert resultAlert = new Alert(addedSuccessfully ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        resultAlert.setTitle("Result");
        resultAlert.setHeaderText(null);
        resultAlert.setContentText(addedSuccessfully ? "Athlete added successfully!" : "Failed to add athlete!");
        resultAlert.showAndWait();
    }

    //      OPTION 3
    public void addScores(Stage primaryStage) {
        // Create a list of skater names for the choice dialog
        List<String> skaterNames = new ArrayList<>();
        for (int i = 1; i <= skaterList.getTotal(); i++) { // Adjusted index to start from 1
            Skater skater = skaterList.getSkater(i);
            if (skater != null) {
                skaterNames.add(skater.getName());
            }
        }

        if (skaterNames.isEmpty()) {
            showAlert("No Skaters", "No skaters currently in the list!");
            return;
        }

        // Create a choice dialog to select the skater
        ChoiceDialog<String> skaterDialog = new ChoiceDialog<>(null, skaterNames);
        skaterDialog.setTitle("Select Skater");
        skaterDialog.setHeaderText("Select the skater to add scores:");
        skaterDialog.setContentText("Skater:");

        // Show the skater choice dialog and wait for user input
        Optional<String> skaterResult = skaterDialog.showAndWait();
        if (skaterResult.isPresent()) {
            String skaterName = skaterResult.get();
            Skater selectedSkater = skaterList.getSkaterByName(skaterName);
            if (selectedSkater != null) {
                List<String> contestLocations = new ArrayList<>();
                for (int i = 1; i <= contestList.getTotal(); i++) { // Adjusted index to start from 1
                    Contest contest = contestList.getContest(i);
                    if (contest != null) {
                        contestLocations.add(contest.getLocation());
                    }
                }

                if (contestLocations.isEmpty()) {
                    showAlert("No Contests", "No contests currently in the list!");
                    return;
                }

                // Create a choice dialog to select the contest
                ChoiceDialog<String> contestDialog = new ChoiceDialog<>(null, contestLocations);
                contestDialog.setTitle("Select Contest");
                contestDialog.setHeaderText("Select the contest to add scores:");
                contestDialog.setContentText("Contest:");

                // Show the contest choice dialog and wait for user input
                Optional<String> contestResult = contestDialog.showAndWait();
                if (contestResult.isPresent()) {
                    String contestLocation = contestResult.get();
                    Contest selectedContest = contestList.getContestByName(contestLocation);
                    if (selectedContest != null) {
                        // Get scores from the user
                        List<Double> scores = new ArrayList<>();
                        for (int i = 0; i < 7; i++) {
                            double score;
                            do {
                                try {
                                    score = showScoreInputDialog("Enter score " + (i + 1) + ":");
                                    if (score < 0 || score > 100) {
                                        showAlert("Invalid Score", "Score must be between 0.0 and 100.0");
                                        score = -1.0; // Reset score to prompt user again
                                    }
                                } catch (NumberFormatException e) {
                                    showAlert("Invalid Input", "Please enter a valid number.");
                                    score = -1.0; // Reset score to prompt user again
                                }
                            } while (score < 0 || score > 100);
                            scores.add(score);
                        }
                        // Add the scores to the skater
                        selectedSkater.addScores(selectedContest, scores.toArray(new Double[0]));
                        showScoreResultPopup(true);
                    }
                }
            }
        }
    }

    private double showScoreInputDialog(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Score");
        dialog.setHeaderText(message);
        dialog.setContentText("Score:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Double.parseDouble(result.get());
            } catch (NumberFormatException e) {
                // If parsing fails, return a negative value to indicate error
                return -1.0;
            }
        }
        // If user cancels the dialog, return a negative value to indicate cancellation
        return -1.0;
    }

    private void showAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void showScoreResultPopup(boolean addedSuccessfully) {
        Alert resultAlert = new Alert(addedSuccessfully ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        resultAlert.setTitle("Result");
        resultAlert.setHeaderText(null);
        resultAlert.setContentText(addedSuccessfully ? "Scores added successfully!" : "Failed to add scores!");
        resultAlert.showAndWait();
    }


    //      OPTION 4
    public void viewContestStandings(Stage primaryStage) {
        if (!contestList.isEmpty()) { // if the list is not empty
            StringBuilder standings = new StringBuilder("Current SLS tour standings:\n");

            for (int i = 1; i <= contestList.getTotal(); i++) { // loop through the contests
                standings.append("\n");
                Contest contest = contestList.getContest(i);
                standings.append("Contest information: ").append(contest.getLocation().toUpperCase())
                        .append(": ").append(contest.getDate()).append("\n");

                double highScore = 0; // to store the highest score
                boolean hasScores = false; // flag to check if any skater has scores for this contest

                for (int j = 1; j <= skaterList.getTotal(); j++) { // loop through the skaters
                    Skater skater = skaterList.getSkater(j);
                    double skaterContestScore = skater.getTotalScore(contest); // get the total score for each skater in the contest

                    if (skaterContestScore > 0) { // if the skater has a score for this contest
                        hasScores = true;
                        standings.append(skater.getName().toUpperCase()).append(": ")
                                .append(skaterContestScore).append("\n"); // append their score

                        if (skaterContestScore > highScore) { // update high score
                            highScore = skaterContestScore;
                        }
                    }
                }

                if (!hasScores) { // if no skater has scores for this contest
                    standings.append("No scores recorded for this contest\n");
                } else { // if at least one skater has scores for this contest
                    standings.append("The high score of this contest was: ").append(highScore).append("\n"); // append the highest score in the contest
                }
            }

            // Display the standings in a message dialog
            displayMessage("Contest Standings", standings.toString());
        } else { // if there are no contests in the list then
            displayMessage("No Contests", "No contests currently in list!");
        }
    }

    //      OPTION 5
    public void viewOneContestStandings(Stage primaryStage) {
        if (!contestList.isEmpty()) {
            List<String> contestLocations = new ArrayList<>();
            for (int i = 1; i <= contestList.getTotal(); i++) {
                Contest contest = contestList.getContest(i);
                if (contest != null) {
                    contestLocations.add(contest.getLocation());
                }
            }

            if (contestLocations.isEmpty()) {
                showAlert("No Contests", "No contests currently in the list!");
                return;
            }

            // Create a choice dialog to select the contest
            ChoiceDialog<String> contestDialog = new ChoiceDialog<>(null, contestLocations);
            contestDialog.setTitle("Select Contest");
            contestDialog.setHeaderText("Select the contest to view standings:");
            contestDialog.setContentText("Contest:");

            // Show the contest choice dialog and wait for user input
            Optional<String> contestResult = contestDialog.showAndWait();
            if (contestResult.isPresent()) {
                String contestLocation = contestResult.get();
                Contest selectedContest = contestList.getContestByName(contestLocation);
                if (selectedContest != null) {
                    StringBuilder standings = new StringBuilder("Standings for contest: " + selectedContest.getLocation().toUpperCase() + "\n");

                    for (int i = 1; i <= skaterList.getTotal(); i++) {
                        Skater skater = skaterList.getSkater(i);
                        if (skater != null) {
                            Double[] scores = skater.getScores(selectedContest);
                            if (scores != null) {
                                standings.append(skater.getName().toUpperCase()).append(": ").append(Arrays.toString(scores)).append("\n");
                            }
                        }
                    }

                    // Display the standings in a message dialog
                    displayMessage("Contest Standings", standings.toString());
                }
            }
        } else {
            showAlert("No Contests", "No contests currently in list!");
        }
    }

    //      OPTION 6
    public void viewAllContestInfo(Stage primaryStage) {
        StringBuilder contestInfo = new StringBuilder();

        // Loop through all contests and append their information to the StringBuilder
        for (int i = 1; i <= contestList.getTotal(); i++) {
            Contest contest = contestList.getContest(i);
            contestInfo.append("Contest Location: ").append(contest.getLocation()).append(", Date: ").append(contest.getDate()).append("\n");
        }

        // Display the collected contest information
        displayMessage("All Contest Information", contestInfo.toString());
    }

    //      OPTION 7
    public void viewOneContestInfo(Stage primaryStage) {
        if (!contestList.isEmpty()) {
            // Create a list of contest locations for the choice dialog
            List<String> contestLocations = new ArrayList<>();
            for (int i = 1; i <= contestList.getTotal(); i++) {
                Contest contest = contestList.getContest(i);
                if (contest != null) {
                    contestLocations.add(contest.getLocation());
                }
            }

            if (contestLocations.isEmpty()) {
                showAlert("No Contests", "No contests currently in the list!");
                return;
            }

            // Create a choice dialog to select the contest
            ChoiceDialog<String> contestDialog = new ChoiceDialog<>(null, contestLocations);
            contestDialog.setTitle("Select Contest");
            contestDialog.setHeaderText("Select the contest to view information:");
            contestDialog.setContentText("Contest:");

            // Show the contest choice dialog and wait for user input
            Optional<String> contestResult = contestDialog.showAndWait();
            if (contestResult.isPresent()) {
                String contestLocation = contestResult.get();
                Contest selectedContest = contestList.getContestByName(contestLocation);
                if (selectedContest != null) {
                    // Display contest information
                    displayMessage("Contest Information", selectedContest.toString());
                } else {
                    showAlert("Error", "Selected contest not found!");
                }
            }
        } else {
            showAlert("No Contests", "No contests currently in the list!");
        }
    }

    //      OPTION 8
    public void viewAllAthleteInfo(Stage primaryStage) {
        if (!skaterList.isEmpty()) {
            // Create a StringBuilder to store athlete information
            StringBuilder athleteInfo = new StringBuilder("Current SLS tour athlete information:\n");

            // Loop through the skater list and append each skater's information
            for (int i = 1; i <= skaterList.getTotal(); i++) {
                athleteInfo.append("Name: ").append(skaterList.getSkater(i).getName()).append(", ")
                        .append("Stance: ").append(skaterList.getSkater(i).getStance()).append(", ")
                        .append("Nationality: ").append(skaterList.getSkater(i).getNationality()).append(", ")
                        .append("Gender: ").append(skaterList.getSkater(i).getGender()).append("\n");
            }

            // Display the collected athlete information
            displayMessage("All Athlete Information", athleteInfo.toString());
        } else { // If the skater list is empty
            // Display a message indicating no athletes are currently in the list
            displayMessage("No Athletes", "No athletes currently in the list!");
        }
    }

    //      OPTION 9
    public void viewOneAthleteInfo(Stage primaryStage) {
        if (!skaterList.isEmpty()) { // If the skater list is not empty
            // Create a list of skater names for the choice dialog
            List<String> skaterNames = new ArrayList<>();
            for (int i = 1; i <= skaterList.getTotal(); i++) {
                Skater skater = skaterList.getSkater(i);
                if (skater != null) {
                    skaterNames.add(skater.getName());
                }
            }

            if (skaterNames.isEmpty()) {
                showAlert("No Skaters", "No skaters currently in the list!");
                return;
            }

            // Create a choice dialog to select the skater
            ChoiceDialog<String> skaterDialog = new ChoiceDialog<>(null, skaterNames);
            skaterDialog.setTitle("Select Skater");
            skaterDialog.setHeaderText("Select the skater to view information:");
            skaterDialog.setContentText("Skater:");

            // Show the skater choice dialog and wait for user input
            Optional<String> skaterResult = skaterDialog.showAndWait();
            if (skaterResult.isPresent()) {
                String skaterName = skaterResult.get();
                Skater selectedSkater = skaterList.getSkaterByName(skaterName);
                if (selectedSkater != null) {
                    // Display skater information
                    displayMessage("Skater Information", "Name: " + selectedSkater.getName() + "\n"
                            + "Stance: " + selectedSkater.getStance() + "\n"
                            + "Nationality: " + selectedSkater.getNationality() + "\n"
                            + "Gender: " + selectedSkater.getGender());
                } else {
                    showAlert("Error", "Selected skater not found!");
                }
            }
        } else { // If the skater list is empty
            // Display a message indicating no skaters are currently in the list
            displayMessage("No Skaters", "No skaters currently in the list!");
        }
    }

    //      OPTION 10
    public void deleteOneContest(Stage primaryStage) {
        if (!contestList.isEmpty()) { // If the contest list is not empty
            // Create a ChoiceDialog for the user to select a contest
            ChoiceDialog<String> contestDialog = new ChoiceDialog<>();
            contestDialog.setTitle("Select Contest to Delete");
            contestDialog.setHeaderText("Select the contest to delete:");

            // Add contest names to the dialog
            for (int i = 1; i <= contestList.getTotal(); i++) {
                contestDialog.getItems().add(contestList.getContest(i).getLocation().toUpperCase() + ", "
                        + contestList.getContest(i).getDate());
            }

            // Add an option to return to the main menu
            contestDialog.getItems().add("Return to main menu");

            // Show the contest choice dialog and wait for user input
            contestDialog.showAndWait().ifPresent(contestName -> {
                // Find the index of the selected contest
                int index = contestDialog.getItems().indexOf(contestName);

                // If the user selected a contest to delete
                if (index != contestDialog.getItems().size() - 1) {
                    // Remove the selected contest
                    contestList.removeContest(index);
                    // Show a confirmation message
                    displayMessage("Contest Deleted", "The contest has been successfully deleted.");
                } else {
                    // If the user selected to return to the main menu, do nothing
                }
            });
        } else { // If the contest list is empty
            // Display a message indicating no contests are currently in the list
            displayMessage("No Contests", "No contests currently in the list!");
        }
    }

    //      OPTION 11
    public void deleteOneSkater(Stage primaryStage) {
        if (!skaterList.isEmpty()) { // If the skater list is not empty
            // Create a ChoiceDialog for the user to select a skater
            ChoiceDialog<String> skaterDialog = new ChoiceDialog<>();
            skaterDialog.setTitle("Select Skater to Delete");
            skaterDialog.setHeaderText("Select the skater to delete:");

            // Add skater names to the dialog
            for (int i = 1; i <= skaterList.getTotal(); i++) {
                skaterDialog.getItems().add(skaterList.getSkater(i).getName().toUpperCase());
            }

            // Add an option to return to the main menu
            skaterDialog.getItems().add("Return to main menu");

            // Show the skater choice dialog and wait for user input
            skaterDialog.showAndWait().ifPresent(skaterName -> {
                // Find the index of the selected skater
                int index = skaterDialog.getItems().indexOf(skaterName);

                // If the user selected a skater to delete
                if (index != skaterDialog.getItems().size() - 1) {
                    // Remove the selected skater
                    skaterList.removeSkater(index);
                    // Show a confirmation message
                    displayMessage("Skater Deleted", "The skater has been successfully deleted.");
                } else {
                    // If the user selected to return to the main menu, do nothing
                }
            });
        } else { // If the skater list is empty
            // Display a message indicating no skaters are currently in the list
            displayMessage("No Skaters", "No skaters currently in the list!");
        }
    }
}
