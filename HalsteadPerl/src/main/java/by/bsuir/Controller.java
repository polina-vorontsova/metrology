package by.bsuir;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Controller {

    private static File codeFile;

    @FXML
    TextArea codeArea;

    @FXML
    private TableView<Pair> operandsTable;

    @FXML
    private TableView<Pair> operatorsTable;

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
        Map<String, Integer> resultMap =
                PerlHalstead.compute(Arrays.asList(codeArea.getText().split("\n")), codeArea);

        operatorsCardinality.setText(resultMap.get("operatorsCardinality").toString());
        operandsCardinality.setText(resultMap.get("operandsCardinality").toString());
        operatorsAmount.setText(resultMap.get("operatorsAmount").toString());
        operandsAmount.setText(resultMap.get("operandsAmount").toString());
        commonCardinality.setText(resultMap.get("commonCardinality").toString());
        commonAmount.setText(resultMap.get("commonAmount").toString());
        value.setText(resultMap.get("value").toString());

        operatorsTable.getItems().addAll(PerlHalstead.operators());
        operatorsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("key"));
        operatorsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("value"));

        operandsTable.getItems().addAll(PerlHalstead.operands());
        operandsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("key"));
        operandsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    @FXML
    void openFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("perl scripts (*.pl)", "*.pl"));
        File toOpen = fileChooser.showOpenDialog(App.getWindow());
        if (toOpen != null) {
            codeFile = toOpen;
            resetAll();
            extractCodeFromFile().forEach(s -> codeArea.appendText(s + "\n"));
        }
    }

    private List<String> extractCodeFromFile() throws IOException {
        return Files.readAllLines(codeFile.toPath());
    }

    private void resetAll() {
        codeArea.clear();
        operatorsTable.getItems().clear();
        operandsTable.getItems().clear();

        operatorsCardinality.clear();
        operandsCardinality.clear();
        operatorsAmount.clear();
        operandsAmount.clear();
        commonCardinality.clear();
        commonAmount.clear();
        value.clear();
    }
}
