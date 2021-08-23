package app.shastin.skillbox;

import app.shastin.skillbox.entities.ConnectedStation;
import app.shastin.skillbox.entities.Connection;
import app.shastin.skillbox.entities.Line;
import app.shastin.skillbox.entities.Station;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String URL = "https://ru.wikipedia.org/wiki/";
    private static final String LOCAL_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36";
    private static final String LOCAL_PATH = "src/main/sourceFiles/";
    private static final String QUERY = URLEncoder.
            encode("Список_станций_Московского_метрополитена", StandardCharsets.UTF_8);
    private static final String JSON_FILE_OUT = "moscow_underground_stations.json";
    private static final String TABLE_SELECTOR = "standard sortable";
    private static final Map<String, List<Station>> LINES_MAP_TEMP = new HashMap<>();
    private static final List<Connection> CONNECTIONS = new ArrayList<>();
    private static final Logger rootLogger = LogManager.getRootLogger();
    private static final Logger mainLogger = LogManager.getLogger(Main.class);

    @FunctionalInterface
    private interface BiFunctionThrowable<T, U, R> {
        R apply(T t, U u) throws IOException;
    }

    @SneakyThrows(IOException.class)
    public static void main(String[] args) {
        final Document page = getJsoupDocument.apply((URL + QUERY), LOCAL_USER_AGENT);
        page.getElementsByClass(TABLE_SELECTOR).forEach(parseTableToJson);
        List<Line> linesFinal = createLines.apply(LINES_MAP_TEMP);
        ObjectMapper objectMapper = new ObjectMapper().setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        JSONWrappedObject objectConnections = new JSONWrappedObject("\"connections\":", "}", CONNECTIONS);
        JSONWrappedObject objectLines = new JSONWrappedObject("{ \"lines\":", ", " +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectConnections), linesFinal);
        objectWriter.writeValue(Paths.get(LOCAL_PATH + JSON_FILE_OUT).toFile(), objectLines);
        rootLogger.info(String.format("Json file %s has been written to folder %s", JSON_FILE_OUT, LOCAL_PATH));
    }

    private static final BiFunctionThrowable<String, String, Document> getJsoupDocument = (urlWithQuery, userAgent) -> {
        rootLogger.info(String.format("Parsing URL %s, UserAgent %s", urlWithQuery, userAgent));
        return Jsoup.connect(urlWithQuery).userAgent(userAgent).get();
    };

    private static final BiConsumer<String, Station> addStationAndLineToList = (line, station) -> {
        List<Station> stationList;
        if (LINES_MAP_TEMP.containsKey(line)) {
            stationList = LINES_MAP_TEMP.get(line);
        } else {
            stationList = new ArrayList<>();
        }
        stationList.add(station);
        LINES_MAP_TEMP.put(line, stationList);
        rootLogger.info(String.format("Added line \"%s\", station \"%s\"", line, station.getName()));
    };

    private static final UnaryOperator<String> removeLeadingZero = element -> element.charAt(0) == '0'
            ? element.replaceFirst("0", "") : element;

    private static final Function<Element, Connection> parseLineConnections = lineConnection -> {
        Connection connection = new Connection();
        final String[] numbersOfConnectedLines = lineConnection.getElementsByClass("sortkey").text().split("\\s+");
        final AtomicInteger i = new AtomicInteger();

        for (Element link : lineConnection.getElementsByTag("a")) {
            ConnectedStation connectedStation;
            String lineNumber = removeLeadingZero.apply(numbersOfConnectedLines[i.getAndIncrement()]);
            try {
                Document page = getJsoupDocument.apply(link.attr("abs:href"), LOCAL_USER_AGENT);
                String station = page.getElementById("firstHeading").text();
                if (station.contains("("))
                    station = station.substring(0, station.indexOf('(')).trim();
                connectedStation = new ConnectedStation(lineNumber, new Station(station));
            } catch (IOException e) {
                String string = "^.+станцию\\s(.+)\\sМоск.+$";
                Pattern pattern = Pattern.compile(string);
                Matcher matcher = pattern.matcher(link.attr("title"));
                if (matcher.find()) {
                    connectedStation = new ConnectedStation(lineNumber, new Station(matcher.group(1)));
                } else {
                    connectedStation = new ConnectedStation(lineNumber, new Station("Станция не определена"));
                    rootLogger.warn(String.format("%s - Станция не определена", link));
                }
                rootLogger.warn("Невозможно установить соединение со страницей станции." +
                        " Название станции вязто из таблицы начальной страницы%n%s", e);
            }
            rootLogger.info(String.format("Created new connected station %s", connectedStation));
            connection.addConnectedStation(connectedStation);
        }
        return connection;
    };

    private static final BiPredicate<Object[], Integer> checkLength = (array, length) -> array.length == length;

    private static final Consumer<Element> parseTableToJson = table -> {
        final String lineNumberSelector = "td:nth-of-type(1)";
        final String stationNameSelector = "td:nth-of-type(2)";
        final String lineConnectionsSelector = "td:nth-of-type(4)";
        table.getElementsByTag("tr").forEach(tableRow -> tableRow.select(lineConnectionsSelector)
                .forEach(lineConnection -> {
                    final String stationName;
                    final String[] numbers;
                    final String line1Number;
                    final String line2Number;
                    Connection connection = null;
                    final ConnectedStation connectedStation1;
                    final ConnectedStation connectedStation2;

                    if (!tableRow.select(lineNumberSelector).select("td > span").text().equals("")
                            && !tableRow.select(stationNameSelector).select("a[href]").text().equals("")) {

                        numbers = tableRow.select(lineNumberSelector).select("td > span").text().split("\\s+");
                        stationName = tableRow.select(stationNameSelector).select("a[href]").first().text();
                        line1Number = removeLeadingZero.apply(numbers[0]);
                        Station station = new Station(stationName);
                        addStationAndLineToList.accept(line1Number, station);

                        connection = parseLineConnections.apply(lineConnection);

                        if (!connection.getConnection().isEmpty()) {
                            connectedStation1 = new ConnectedStation(line1Number, station);
                            rootLogger.info(String.format("Created new connected station %s", connectedStation1));
                            connection.addConnectedStation(connectedStation1);

                            if (checkLength.test(numbers, 3)) {
                                line2Number = removeLeadingZero.apply(numbers[1]);
                                addStationAndLineToList.accept(line2Number, station);
                                connectedStation2 = new ConnectedStation(line2Number, station);
                                rootLogger.info(String.format("Created new connected station %s", connectedStation2));
                                connection.addConnectedStation(connectedStation2);

                            }
                        }
                    }
                    if ((!CONNECTIONS.contains(connection)) &&
                            (connection != null) &&
                            (!connection.getConnection().isEmpty())) {
                        rootLogger.info(String.format("Added new connection %s", connection));
                        CONNECTIONS.add(connection);
                    }
                }));
    };

    private static final Function<Map<String, List<Station>>, List<Line>> createLines = map -> {
        List<Line> lineList = new ArrayList<>();
        map.forEach((k, v) -> {
            Line line = new Line(k, v);
            rootLogger.info(String.format("New line created %s", line));
            lineList.add(line);
        });
        return lineList;
    };
}