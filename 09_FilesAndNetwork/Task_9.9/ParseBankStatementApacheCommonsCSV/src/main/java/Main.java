import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String PATH = "src/main/resources/movementList.csv";
    private static Double totalIncome = 0.0;
    private static Double totalOutcome = 0.0;
    private static Function<String, String> replaceComma = s -> s.replaceAll(",", ".");
    private static Map<String, Double[]> operationMap = new HashMap<>();

    public static void main(String[] args) {
        CSVParser csvParser = null;
        Pattern pattern = Pattern.compile("^\\d{6}.{6}\\d{4}\\t?(.+)\\t+\\d{2}.\\d{2}.\\d{2}\\s*\\d{2}.\\d{2}.\\d{2}.+$");
        try {
            csvParser = new CSVParser(Files.newBufferedReader(Paths.get(PATH)), CSVFormat.DEFAULT.withFirstRecordAsHeader()
                    .withIgnoreHeaderCase().withTrim());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert csvParser != null;
        csvParser.forEach(e -> {
            String description = e.get("Описание операции").replaceAll("\\s{3,}", "\t").trim();
            Matcher matcher = pattern.matcher(description);
            if (matcher.find())
                description = matcher.group(1);

            Double income = Double.parseDouble(replaceComma.apply(e.get("Приход")));
            Double outcome = Double.parseDouble(replaceComma.apply(e.get("Расход")));
            if (!operationMap.containsKey(matcher.group(1))) {
                Double[] amounts = new Double[2];
                amounts[0] = income;
                amounts[1] = outcome;
                operationMap.put(matcher.group(1), amounts);
            } else {
                operationMap.get(description)[0] += income;
                operationMap.get(description)[1] += outcome;
            }
            totalIncome += income;
            totalOutcome += outcome;
        });
        System.out.println("***********************");
        operationMap.forEach((k, v) -> {
            System.out.printf("Operation: %s\nIncome: %.2f\nOutcome: %.2f\n", k, v[0], v[1]);
            System.out.println("***********************");
        });
        System.out.printf("Total income %.2f;\nTotal outcome %.2f;\n", totalIncome, totalOutcome);
    }
}