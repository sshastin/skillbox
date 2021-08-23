import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.DoubleFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static Double totalIncome = 0.0;
    private static Double totalOutcome = 0.0;

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/movementList.csv");
        Pattern pattern = Pattern.compile("^\\d{6}.{6}\\d{4}\\t?(.+)\\t+\\d{2}.\\d{2}.\\d{2}\\s*\\d{2}.\\d{2}.\\d{2}.+$");
        List<String> list;
        List<String[]> splitRecords = new ArrayList<>();
        Map<String, Double[]> operationMap = new HashMap<>();
        DoubleFunction<Double> addIncome = a -> totalIncome + a;
        DoubleFunction<Double> addOutcome = a -> totalOutcome + a;

        list = Files.readAllLines(path);

        for (int i = 0; i < list.size(); i++) {
            String oldString = list.get(i).replaceAll("\\s{3,}", "\t").trim();
            StringBuilder newStringBuilder = new StringBuilder();

            while (oldString.contains("\"")) {
                oldString = newStringBuilder.append(oldString, 0, oldString.indexOf("\"")).append(oldString.substring(oldString.indexOf("\"") + 1,
                        oldString.indexOf("\"", oldString.indexOf("\"") + 1)).replaceAll(",", "\\.")).toString().trim();
            }
            splitRecords.add(oldString.split(","));
        }

        splitRecords.forEach(e -> {
            Matcher matcher = pattern.matcher(e[5]);
            while (matcher.find()) {
                if (!operationMap.containsKey(matcher.group(1))) {
                    Double[] amounts = new Double[2];
                    totalIncome = addIncome.apply(amounts[0] = Double.parseDouble(e[6]));
                    totalOutcome = addOutcome.apply(amounts[1] = Double.parseDouble(e[7]));
                    operationMap.put(matcher.group(1), amounts);
                } else {
                    operationMap.get(matcher.group(1))[0] += Double.parseDouble(e[6]);
                    totalIncome = addIncome.apply(Double.parseDouble(e[6]));
                    operationMap.get(matcher.group(1))[1] += Double.parseDouble(e[7]);
                    totalOutcome = addOutcome.apply(Double.parseDouble(e[7]));
                }
            }
        });

        System.out.println("***********************");
        operationMap.forEach((k, v) -> {
            System.out.printf("Operation: %s\nIncome: %.2f\nOutcome: %.2f\n", k, v[0], v[1]);
            System.out.println("***********************");
        });
        System.out.printf("Total income: %.2f\nTotal outcome %.2f\n", totalIncome, totalOutcome);
    }
}