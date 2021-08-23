import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static Double incomeTotal = 0.0;
    private static Double outcomeTotal = 0.0;
    private static Map<String, Double> incomeOperationsMap = new HashMap<>();
    private static Map<String, Double> outcomeOperationsMap = new HashMap<>();


    public static void main(String[] args) {

        //0. Тип счёта	1. Номер счета	2. Валюта	3. Дата операции	4. Референс проводки	5. Описание операции	6. Приход	7. Расход

        String pathToCSVFile = "src/main/resources/movementList.csv";
        Path fileCSVForParsing = Paths.get(pathToCSVFile);
        List<String> lines = new ArrayList<>();
        List<String[]> allLines = new ArrayList<>();

        try {
            lines = Files.readAllLines(fileCSVForParsing);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).indexOf('\"') != -1) {
                String word = lines.get(i);
                lines.set(i, replaceComma(word).replaceAll("\"", ""));
            }
            allLines.add(lines.get(i).split(","));
        }


        for (int i = 1; i < allLines.size(); i++) {
            String operation = getFormattedSubstring(allLines.get(i)[5]);

            if (notEqualToZero(allLines.get(i)[6])) {
                incomeOperationsMap.put(operation, 0.0);
                incomeTotal += Double.parseDouble(allLines.get(i)[6]);
            } else {
                outcomeOperationsMap.put(operation, 0.0);
                outcomeTotal += Double.parseDouble(allLines.get(i)[7]);
            }
        }

        calculate(allLines);
        printOperations(incomeOperationsMap, OperationType.INCOME);
        printOperations(outcomeOperationsMap, OperationType.OUTCOME);
    }

    private static String replaceComma(String string) {
        int first = string.indexOf('\"') + 1;
        int next = string.indexOf('\"', first + 1);
        StringBuilder newStringBuilder = new StringBuilder();

        newStringBuilder.append(string, 0, first);
        newStringBuilder.append(string.substring(first, next).replaceAll(",", "."));

        return newStringBuilder.toString();
    }

    private static <K, V> void printOperations(Map<K, V> map, OperationType operationType) {
        for (Map.Entry<K, V> element : map.entrySet()) {
            System.out.printf("Operation %s %s: %s\n", element.getKey(), operationType.type(), element.getValue());
        }

        String op = operationType.type().equals(OperationType.INCOME.type()) ? OperationType.INCOME.type() : OperationType.OUTCOME.type();
        String sum = String.valueOf(operationType.type().equals(OperationType.INCOME.type()) ? incomeTotal : outcomeTotal);
        System.out.printf("Total %s is %s\n", op, sum);
    }

    private static void calculate(List<String[]> list) {
        String operation;
        for (int i = 1; i < list.size(); i++) {

            operation = getFormattedSubstring(list.get(i)[5]);

            if (notEqualToZero(list.get(i)[6])) {
                Double value = incomeOperationsMap.get(operation) + Double.parseDouble(list.get(i)[6]);
                incomeOperationsMap.replace(operation, value);
            } else {
                Double value = outcomeOperationsMap.get(operation) + Double.parseDouble(list.get(i)[7]);
                outcomeOperationsMap.replace(operation, value);
            }
        }
    }

    private static Boolean notEqualToZero(String s) {
        Boolean notEQ = false;
        try {
            notEQ = Double.parseDouble(s) != 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return notEQ;
    }

    private static String getFormattedSubstring(String string) {
        string = string.trim().replaceAll("[\\s]{3,}", "\t");
        int firstTab = string.indexOf("\t");
        int secondTab = string.indexOf("\t", firstTab + 1);
        return string.substring(firstTab + 1, secondTab);
    }
}
