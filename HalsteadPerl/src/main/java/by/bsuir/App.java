package by.bsuir;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Window window;

    public static Window getWindow() {
        return window;
    }

    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("form.fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(loadFXML()));
        stage.getIcons().add(new Image(App.class.getResource("icon.png").toExternalForm()));
        window = stage.getOwner();
        stage.show();
    }

}