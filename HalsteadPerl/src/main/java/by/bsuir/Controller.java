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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    static File codeFile;

    @FXML
    private TextArea codeArea;

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
    void compute() throws IOException {
        List<String> code = Arrays.asList(codeArea.getText().split("\n"));

        removeComments(code);
    }

    @FXML
    void openFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("perl scripts (*.pl)", "*.pl"));
        codeFile = fileChooser.showOpenDialog(App.getWindow());
        extractCodeFromFile().forEach(s -> {
            codeArea.appendText(s + "\n");
        });
    }

    private List<String> extractCodeFromFile() throws IOException {
        return Files.readAllLines(codeFile.toPath());
    }

    private void removeComments(List<String> code) {
        for (int i = 0; i < code.size(); i++) {
            StringBuffer copy = new StringBuffer(code.get(i));
            Matcher matcher = Pattern.compile("(?<=[\"'`]).+(?=[\"'`])").matcher(copy);
            while (matcher.find()) {
                int begin = matcher.start();
                int end = matcher.end();
                copy.replace(begin,end, "*".repeat(end - begin));
            }
            int pos = copy.indexOf("#");
            if (pos >= 0) {
                code.set(i, code.get(i).substring(0, pos));
            }
        }

        // development future
        codeArea.clear();
        code.forEach(s -> codeArea.appendText(s + "\n"));
    }
}
