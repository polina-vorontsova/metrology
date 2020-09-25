package by.bsuir;

import javafx.scene.control.TextArea;

import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerlHalstead {

    private static final String OPERAND_REGEX = "([$@%]\\w+)|((['\"`](\\w|\\s)*['\"`])|([+-]?\\d+\\.?\\d*))";
    private static final String OPERATOR_REGEX =
            "(<\\w*>)|((\\w+)(::|(\\(\\))?->)((\\w+)(::|(\\(\\))?->))*((\\(\\))|\\w+))|" +
                    //unknown functionality
                    "->|=>|<=>|(=[!~])|([+-]{1,2}|~)|(([<>]{2}|[*&|]{1,2}|[/%+\\-x^.=<>!])=?)|;|(\\$#)";
    private static TreeMap<String, Integer> operands;
    private static TreeMap<String, Integer> operators;

    public static TreeMap<String, Integer> operands() {
        return operands;
    }

    public static TreeMap<String, Integer> operators() {
        return operators;
    }

    public static void compute(List<String> code, TextArea codeArea) {
        operands = new TreeMap<>();
        operators = new TreeMap<>();
        removeComments(code, codeArea);
        recognizeOperators(code, codeArea);
        recognizeOperands(code, codeArea);
    }

    private static void removeComments(List<String> code, TextArea codeArea) {
        for (int i = 0; i < code.size(); i++) {
            StringBuilder copy = new StringBuilder(code.get(i));
            Matcher matcher = Pattern.compile("(?<=[\"'`]).+(?=[\"'`])").matcher(copy);
            while (matcher.find()) {
                int begin = matcher.start();
                int end = matcher.end();
                copy.replace(begin, end, "*".repeat(end - begin));
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

    private static void recognizeOperators(List<String> code, TextArea codeArea) {
        for (int i = 0; i < code.size(); i++) {
            Matcher matcher = Pattern.compile(OPERATOR_REGEX).matcher(code.get(i));
            while (matcher.find()) {
                operators.merge(matcher.group(), 1, (oldValue, newValue) -> oldValue + 1);
                code.set(i, code.get(i).replaceFirst(Pattern.quote(matcher.group()), ""));
            }
        }

        // development future
        codeArea.clear();
        code.forEach(s -> codeArea.appendText(s + "\n"));
    }

    private static void recognizeOperands(List<String> code, TextArea codeArea) {
        for (int i = 0; i < code.size(); i++) {
            Matcher matcher = Pattern.compile(OPERAND_REGEX).matcher(code.get(i));
            while (matcher.find()) {
                operands.merge(matcher.group(), 1, (oldValue, newValue) -> oldValue + 1);
                code.set(i, code.get(i).replaceFirst(matcher.group(), ""));
            }
        }

        // development future
        codeArea.clear();
        code.forEach(s -> codeArea.appendText(s + "\n"));
    }
}
