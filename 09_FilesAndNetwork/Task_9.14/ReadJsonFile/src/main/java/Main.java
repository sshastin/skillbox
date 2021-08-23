import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.Consumer;

public class Main {
    private static Integer numberOfStations = 0;
    private static Integer totalNumberOfStations = 0;
    private static final Consumer<Integer> counter = number -> {
        if (number == 0) {
            numberOfStations = number;
        } else {
            numberOfStations += number;
            totalNumberOfStations += number;
        }
    };

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        String pathToJsonFile = "D:\\Oracle\\Java\\SkillBox\\java_basics\\09_FilesAndNetwork\\Task_9.14\\ReadJsonFile\\src\\main\\resources\\moscow_underground_stations.json";
        try (Reader reader = new FileReader(pathToJsonFile)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject stationsJsonObject = (JSONObject) jsonObject.get("stations");
            JSONArray connectionsJsonObject = (JSONArray) jsonObject.get("connections");

            stationsJsonObject.forEach((line, stations) -> {
                System.out.printf("Линия: %s\n", line);
                JSONArray newStations = (JSONArray) stations;
                counter.accept(0);
                newStations.forEach(station -> {
                    counter.accept(1);
                    System.out.printf("\t%s\n", station);
                });
                System.out.printf("Количество станций: %s\n", numberOfStations);
            });
            System.out.printf("=========================\nОбщее количество станций Московского метрополитена: " +
                    "%s\n=========================\n", totalNumberOfStations);

            connectionsJsonObject.forEach(connection -> {
                System.out.println("Пересадка:");
                ((JSONArray) connection).forEach(element -> {
                    String connectedLine = (String) ((JSONObject) element).get("line");
                    String connectedStation = (String) ((JSONObject) element).get("station");
                    System.out.printf("\tЛиния: %s, Станция: %s\n", connectedLine, connectedStation);
                });
            });
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}