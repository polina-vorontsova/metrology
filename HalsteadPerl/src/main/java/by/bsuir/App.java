package by.bsuir;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Window window;

    String operandRegex = "([$@%]\\w+)|((['\"`](\\w|\\s)*['\"`])|([+-]?\\d+\\.?\\d*))";
    String operatorRegex =
            "(<STDIN>)|(((\\w+)(::|(\\(\\))?\\->)?)+((\\()|\\w))|->|=>|<=>|((\\/|\\+\\+?|\\*|--?|>|<|=)=?)|;|!=|\\.|([%@] *{)|(&{1,2})|(\\|{1,2})|(!)";

    public static Window getWindow() {
        return window;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(loadFXML("form.fxml")));
        window = stage.getOwner();
        stage.show();
    }

}