import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.*;

public class SonglibController {
    @FXML
    ListView<String> listView;

    @FXML
    Button add;

    @FXML
    TextField songtext;

    @FXML
    Button edit;

    @FXML
    Button delete;

    @FXML
    Label details;

    @FXML
    Button clear;

    @FXML
    TextField artisttext;

    @FXML
    TextField albumtext;

    @FXML
    TextField yeartext;

    private int selectedIndex=-1;
    private ObservableList<String> obsList;
    private HashMap<String, SongInfo> obsListToSong;

    public void start(Stage mainStage) {
// create an ObservableList from an ArrayList and Hashmap to map song details to list elements
        obsList = FXCollections.observableArrayList(
                "Jaguar God - Mastodon",
                "The Hills - The Weeknd"
        );
        listView.setItems(obsList);
        SongInfo jaguar = new SongInfo("Jaguar God", "Mastodon", "Emperor of Sand", "2017");
        SongInfo hills = new SongInfo("The Hills", "The Weeknd", "Beauty Behind The Madness", "2015");
        obsListToSong = new HashMap<>();
        obsListToSong.put("Jaguar God - Mastodon".toLowerCase(), jaguar);
        obsListToSong.put("The Hills - The Weeknd".toLowerCase(), hills);
        SortedList<String> sortedList = new SortedList<>(obsList);
        listView.setItems(sortedList);

        sortedList.setComparator(new Comparator<String>() {
            @Override
            public int compare(String song1, String song2) {
                SongInfo s1 = obsListToSong.get(song1.toLowerCase());
                SongInfo s2 = obsListToSong.get(song2.toLowerCase());

                int s1Tos2 = s1.name.compareToIgnoreCase(s2.name);
                if (s1Tos2 != 0) return s1Tos2;
                else return s1.artist.compareToIgnoreCase(s2.artist);
            }
        });

        listView.getSelectionModel().select(0);
        songtext.setPromptText("Song Name");
        artisttext.setPromptText("Artist Name");
        albumtext.setPromptText("Album Name");
        yeartext.setPromptText("Year");


        //viewing details when clicked on song in list on label & in text box
        listView.setOnMouseClicked(mouseEvent -> {
            if (obsList.size() == 0) return;
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            selectedIndex = listView.getSelectionModel().getSelectedIndex();
            SongInfo selectedSong = obsListToSong.get(selectedItem.toLowerCase());
            songtext.setText(selectedSong.name);
            artisttext.setText(selectedSong.artist);
            albumtext.setText(selectedSong.album);
            yeartext.setText(selectedSong.year);
            if (albumtext.getText().equals("N/A")) {
                albumtext.setText("");
            }
            if (yeartext.getText().equals("N/A")) {
                yeartext.setText("");
            }
            details.setText(selectedSong.name + " - " + selectedSong.artist + " - " + selectedSong.album + " - " + selectedSong.year);
        });

        //add button
        add.setOnAction((ActionEvent e) -> {
            String songName = songtext.getText().trim();
            String artistName = artisttext.getText().trim();
            String albumName = albumtext.getText().trim();
            String year = yeartext.getText().trim();

            //checking if the user inputted a song
            if (!validateInputs(songName, artistName,albumName, year)){
                return;
            }

            if (albumName.isEmpty()) albumName = "N/A";
            if (year.isEmpty()) year = "N/A";

            String songString = addDash(songName, artistName);
            if (makeConfirm("Are you sure you want to add " + songString + "?")) {
                if (obsListToSong.containsKey(songString.toLowerCase())) {
                    makeAlert("Song is already added, please try a new song.");
                    return;
                }
                else {
                    SongInfo selectedSong = new SongInfo(songName, artistName, albumName, year);
                    obsListToSong.put(songString.toLowerCase(), selectedSong);
                    obsList.add(songString);
                    details.setText(songName + " - " + artistName + " - " + albumName + " - " + year);
                    listView.getSelectionModel().select(songString);
                }
            }
            clearAll();
        });
        //edit button
        edit.setOnAction((ActionEvent event) -> {
            //if no song is selected
            if (listView.getSelectionModel().getSelectedItem() == null) {
                makeAlert("Please select a song to edit.");
                return;
            }

            String editSong = listView.getSelectionModel().getSelectedItem();
            String songName = songtext.getText().trim();
            String artistName = artisttext.getText().trim();
            String albumName = albumtext.getText().trim();
            String year = yeartext.getText().trim();

            if (!validateInputs(songName, artistName, albumName, year)) return;

            if (albumName.isEmpty()) albumName = "N/A";
            if (year.isEmpty()) year = "N/A";

            String songString = addDash(songName, artistName);
            String key = songString.toLowerCase();
            if (makeConfirm("Are you sure you want to edit " + editSong + " to " + songString + "?")) {
                if (obsListToSong.containsKey(key) && !editSong.toLowerCase().equals(key)) {
                    makeAlert("Song is already added. Please try a new song.");
                    return;
                }
                obsListToSong.remove(editSong.toLowerCase());
                obsList.remove(editSong);
                obsListToSong.put(key, new SongInfo(songName, artistName, albumName, year));
                obsList.add(songString);
                listView.getSelectionModel().select(songString);
                details.setText(songName + " - " + artistName + " - " + albumName + " - " + year);
            }
            clearAll();
        });
        //delete button
        delete.setOnAction((ActionEvent event) -> {
            if (listView.getSelectionModel().getSelectedItem()==null) {
                makeAlert("Please select a song to delete.");
                return;
            }

            String deleteSong = listView.getSelectionModel().getSelectedItem();

            if (makeConfirm("Are you sure you want to delete " + deleteSong + "?")) {
                selectedIndex = listView.getSelectionModel().getSelectedIndex();
                obsListToSong.remove(deleteSong.toLowerCase());
                obsList.remove(deleteSong);
                clearAll();
            }

            if (obsList.size()==0) return;

            if (selectedIndex < obsList.size() - 1) {
                listView.getSelectionModel().select(selectedIndex);
            }
            else if (selectedIndex > 0) {
                listView.getSelectionModel().select(selectedIndex - 1);
                selectedIndex--;
            }

            String selectedItem = listView.getSelectionModel().getSelectedItem();
            SongInfo selectedSong = obsListToSong.get(selectedItem.toLowerCase());
            songtext.setText(selectedSong.name);
            artisttext.setText(selectedSong.artist);
            albumtext.setText(selectedSong.album);
            yeartext.setText(selectedSong.year);

            if (albumtext.getText().equals("N/A")) {
                albumtext.setText("");
            }
            if (yeartext.getText().equals("N/A")) {
                yeartext.setText("");
            }
            details.setText(selectedSong.name + " - " + selectedSong.artist + " - " + selectedSong.album + " - " + selectedSong.year);
        });
        //clear button
        clear.setOnAction((ActionEvent event) -> {
            clearAll();
        });
    }

    private boolean validateInputs(String songName, String artistName, String albumName, String year) {

        if (songName.isEmpty() && artistName.isEmpty()) {
            makeAlert("Please provide a song and artist.");
            return false;
        }
        if (songName.isEmpty()) {
            makeAlert("Please provide a song.");
            return false;
        }
        if (artistName.isEmpty()) {
            makeAlert("Please provide an artist.");
            return false;
        }
        if (songName.contains("|") || artistName.contains("|") || year.contains("|") || albumName.contains("|")) {
            makeAlert("Invalid character '|'. Please provide letters, numbers or other symbols.");
            return false;
        }

        //if the user did not enter an album title or year, automatically setting them to N/A
        if (!year.isEmpty()) {
            if (year.length() != 4) {
                makeAlert("Year must have 4 characters");
                return false;
            }
            try {
                int yearNum = Integer.parseInt(year);
                if (yearNum < 0) {
                    makeAlert("Year must be positive");
                    return false;
                }
            }
            catch (Exception e1) {
                makeAlert("Year must be numerical.");
                return false;
            }
        }
        return true;
    }
    private void makeAlert(String contentText) {
        Alert.AlertType error = Alert.AlertType.ERROR;
        Alert alert = new Alert(error, "");
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.getDialogPane().setContentText(contentText);
        alert.getDialogPane().setHeaderText("SongLib Application");

        Optional<ButtonType> result = alert.showAndWait();
        return;
    }

    private boolean makeConfirm(String contentText) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("SongLib Application");
        confirm.setContentText(contentText);
        Optional<ButtonType> action = confirm.showAndWait();
        return action.get() == ButtonType.OK;
    }

    private String addDash(String s1, String s2) {
        return s1 + " - " + s2;
    }

    private void clearAll() {
        songtext.clear();
        artisttext.clear();
        albumtext.clear();
        yeartext.clear();
    }
}
