package by.bsuir;

import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerlHalstead {

    private static final String OPERAND_REGEX =
            "(([$@%]\\w+)|((['\"`].*?(?<!\\\\)['\"`])|([+-]?\\d+\\.?\\d*)))|" +
                    "((?<=\\W|^)(__(LINE|FILE|END|DATA)__)(?=\\W|$))";
    private static final String OPERATOR_REGEX =
            "(<\\w*>)|((\\w+)(::|(\\(\\))?->)((\\w+)(::|(\\(\\))?->))*((\\(\\))|\\w+))|" +
                    //unknown functionality
                    "->|=>|<=>|(=[!~])|([+-]{1,2}|~)|(([<>]{2}|[*&|]{1,2}|[/%+\\-^x.=<>!])=?)|;|(\\$#)";
    private static File functionsFile;
    private static Set<Pair> operands;
    private static Set<Pair> operators;
    private static Set<String> functions;

    private static int operatorsAmount;
    private static int operandsAmount;

    public static Set<Pair> operands() {
        return operands;
    }

    public static Set<Pair> operators() {
        return operators;
    }

    public static Map<String, Integer> compute(List<String> code, TextArea codeArea) {
        operatorsAmount = operandsAmount = -1;
        functionsFile = null;

        operands = new TreeSet<>();
        operators = new TreeSet<>();
        functions = new HashSet<>();
        functionsFile = new File("default_functions.txt");
        if (functionsFile.exists()) {
            try {
                addFunctionsFromFile(functions, functionsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        addUserFunctions(functions, code);

        removeComments(code, codeArea);
        recognizeOperands(code, codeArea);
        recognizeFunctions(functions, code, codeArea);
        recognizeOperators(code, codeArea);

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

//        // development future
        codeArea.clear();
        code.forEach(s -> codeArea.appendText(s + "\n"));
    }

    private static void recognizeOperands(List<String> code, TextArea codeArea) {
        Map<String, Integer> mapOfOperands = new TreeMap<>();
        for (int i = 0; i < code.size(); i++) {
            Matcher matcher = Pattern.compile(OPERAND_REGEX).matcher(code.get(i));
            while (matcher.find()) {
                mapOfOperands.merge(matcher.group(), 1, (oldValue, newValue) -> oldValue + 1);
                code.set(i, code.get(i).replaceFirst(Pattern.quote(matcher.group()), "\"#\""));
            }
        }
        mapOfOperands.forEach((key, value) -> operands.add(new Pair(key, value)));

        // development future
        codeArea.clear();
        code.forEach(s -> codeArea.appendText(s + "\n"));
    }

    private static void recognizeOperators(List<String> code, TextArea codeArea) {
        Map<String, Integer> mapOfOperators = new TreeMap<>();
        for (int i = 0; i < code.size(); i++) {
            Matcher matcher = Pattern.compile(OPERATOR_REGEX).matcher(code.get(i));
            while (matcher.find()) {
                mapOfOperators.merge(matcher.group(), 1, (oldValue, newValue) -> oldValue + 1);
                code.set(i, code.get(i).replaceFirst(Pattern.quote(matcher.group()), "\"#\""));
            }
        }
        mapOfOperators.forEach((key, value) -> operators.add(new Pair(key, value)));

        // development future
        codeArea.clear();
        code.forEach(s -> codeArea.appendText(s + "\n"));
    }

    private static void addFunctionsFromFile(Set<String> functions, File functionsFile) throws IOException {
        List<String> functionsList = Files.readAllLines(Path.of(functionsFile.getPath()));
        for (int i = 0; i < functionsList.size(); i++) {
            functionsList.set(i, functionsList.get(i).trim());
        }
        functions.addAll(functionsList);
    }

    private static void addUserFunctions(Set<String> functions, List<String> code) {
        String compressedCode = compressCode(code);
        Matcher matcher = Pattern.compile("(?<=(\\W|^)sub)\\s+\\w+(?=\\W)").matcher(compressedCode);
        while (matcher.find()) {
            functions.add(matcher.group().stripLeading());
        }
    }

    private static void recognizeFunctions(Set<String> functions, List<String> code, TextArea codeArea) {
        Map<String, Integer> mapOfOFunctions = new HashMap<>();
        String compressedCode = compressCode(code).replaceAll("\\s+", " ").
                replaceAll("(?<=\\W|^)sub\\s", "sub");
        for (String oneFunction : functions) {
            Matcher matcher = Pattern.compile(String.format("(?<=\\s|^)%s(?=\\W|$)",
                    Pattern.quote(oneFunction))).matcher(compressedCode);
            while (matcher.find()) {
                mapOfOFunctions.merge(matcher.group().stripLeading(),
                        1, (oldValue, newValue) -> oldValue + 1);
                compressedCode = compressedCode.replaceFirst(Pattern.quote(matcher.group().stripLeading()),
                        "\"#\"");
            }
        }
        mapOfOFunctions.forEach((key, value) -> operators.add(new Pair(key, value)));

        // development future
        codeArea.clear();
        code.forEach(s -> codeArea.appendText(s + "\n"));
    }

    private static String compressCode(List<String> code) {
        StringBuilder builder = new StringBuilder();
        for (String line : code) {
            builder.append(line);
        }
        return builder.toString();
    }

    private static int calculateOperatorsAmount() {
        int amount = 0;
        for (Pair onePair : operators) {
            amount += onePair.getValue();
        }
        operatorsAmount = amount;
        return operatorsAmount;
    }

    private static int calculateOperandsAmount() {
        int amount = 0;
        for (Pair onePair : operands) {
            amount += onePair.getValue();
        }
        operandsAmount = amount;
        return operandsAmount;
    }
}
