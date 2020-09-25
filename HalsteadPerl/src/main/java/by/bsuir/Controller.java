package by.bsuir;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Controller {

    private static File codeFile;

    @FXML
    TextArea codeArea;

    @FXML
    private TableView<?> operandsTable;

    @FXML
    private TableView<?> operatorsTable;

    @FXML
    private TextField operatorsCardinality;

    @FXML
    private TextField operatorsAmount;

    @FXML
    private TextField operandsCardinality;

    @FXML
    private TextField operandsAmount;

    @FXML
    private TextField commonCardinality;

    @FXML
    private TextField commonAmount;

    @FXML
    private TextField value;

    @FXML
    void compute() {
        PerlHalstead.compute(Arrays.asList(codeArea.getText().split("\n")), codeArea);
    }

    @FXML
    void openFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("perl scripts (*.pl)", "*.pl"));
        codeFile = fileChooser.showOpenDialog(App.getWindow());
        extractCodeFromFile().forEach(s -> codeArea.appendText(s + "\n"));
    }

    private List<String> extractCodeFromFile() throws IOException {
        return Files.readAllLines(codeFile.toPath());
    }
}
