import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        final List<Line> lines = new ArrayList<>();
        final List<Connection> connections = new ArrayList<>();
        final ObjectMapper mapper = new ObjectMapper();
        final JSONParser parser = new JSONParser();
        final JSONObject jsonFile = (JSONObject) parser.parse(Files.readString(Paths.get("src/main/JSON/moscow_underground_stations.json")));
        final JSONArray linesJsonArray = (JSONArray) jsonFile.get("lines");
        final JSONArray connectionsJsonArray = (JSONArray) jsonFile.get("connections");
        final Logger rootLogger = LogManager.getRootLogger();
        linesJsonArray.forEach(line -> {
            try {
                lines.add(mapper.readValue(line.toString(), Line.class));
            } catch (JsonProcessingException e) {
                rootLogger.error(e);
            }
        });

        connectionsJsonArray.forEach(connection -> {
            try {
                connections.add(mapper.readValue(connection.toString(), Connection.class));
            } catch (JsonProcessingException e) {
                rootLogger.error(e);
            }
        });

        rootLogger.info("Lines:");
        lines.forEach(rootLogger::info);
        rootLogger.info("Connections:");
        connections.forEach(rootLogger::info);
        long numberOfStations = lines.stream().mapToInt(line -> line.getStations().size()).sum();
        long numberOfConnections = connections.size();
        rootLogger.info(String.format("Количество станций: %d", numberOfStations));
        rootLogger.info(String.format("Количество пересадочных станций: %d", numberOfConnections));
    }
}