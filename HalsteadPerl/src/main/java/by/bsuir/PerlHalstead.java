package by.bsuir;

import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Map;
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

    private static int operatorsAmount;
    private static int operandsAmount;

    public static Map<String, Integer> compute(List<String> code, TextArea codeArea) {
        operatorsAmount = operandsAmount = -1;
        operands = new TreeMap<>();
        operators = new TreeMap<>();
        removeComments(code, codeArea);
        recognizeOperators(code, codeArea);
        recognizeOperands(code, codeArea);

        int commonCardinality = operators.size() + operands.size();
        int commonAmount = calculateOperatorsAmount() + calculateOperandsAmount();

        return Map.of("operatorsCardinality", operators.size(),
                "operandsCardinality", operands.size(),
                "operatorsAmount", operatorsAmount,
                "operandsAmount", operandsAmount,
                "commonCardinality", commonCardinality,
                "commonAmount", commonAmount,
                "value", (int) Math.round(commonAmount * Math.log(commonCardinality) / Math.log(2)));
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

    private static int calculateOperatorsAmount() {
        int amount = 0;
        for (Integer one_amount : operators.values()) {
            amount += one_amount;
        }
        operatorsAmount = amount;
        return operatorsAmount;
    }

    private static int calculateOperandsAmount() {
        int amount = 0;
        for (Integer one_amount : operands.values()) {
            amount += one_amount;
        }
        operandsAmount = amount;
        return operandsAmount;
    }
}
