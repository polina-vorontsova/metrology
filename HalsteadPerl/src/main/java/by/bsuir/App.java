package by.bsuir;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    String operandRegex = "([$@%]\\w+)|((['\"`](\\w|\\s)*['\"`])|([+-]?\\d+\\.?\\d*))";
    String operatorRegex = "(<STDIN>)|(((\\w+)(::|(\\(\\))?\\->)?)+((\\()|\\w))|->|=>|<=>|((\\/|\\+\\+?|\\*|--?|>|<|=)=?)|;|!=|\\.|([%@] *{)|(&{1,2})|(\\|{1,2})|(!)";

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("form.fxml"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}