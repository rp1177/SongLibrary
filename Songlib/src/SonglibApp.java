import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

    public class SonglibApp extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
// set up FXML loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("songlib.fxml"));
// load the fxml
            AnchorPane root = (AnchorPane)loader.load();
// get the controller (Do NOT create a new Controller()!!)
// instead, get it through the loader
            SonglibController listController = loader.getController();
            listController.start(primaryStage);
            Scene scene = new Scene(root, 729.2, 659);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        public static void main(String[] args) {
// TODO Auto-generated method stub
            launch(args);
        }
    }

