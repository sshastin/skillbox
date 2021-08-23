import core.Line;
import core.Station;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RouteCalculatorTest {

    private static StationIndex stationIndex = new StationIndex();
    private static RouteCalculator calculator;
    private static List<Station> route;
    private static List<Station> sameStationListExpected;
    private static List<Station> sameStationListActual;
    private static List<Station> noInterchangeListExpected;
    private static List<Station> noInterchangeListActual;
    private static List<Station> oneInterchangeListExpected;
    private static List<Station> oneInterchangeListActual;
    private static List<Station> oneInterchangeViaConnection3linesListExpected;
    private static List<Station> oneInterchangeViaConnection3linesListActual;
    private static List<Station> noInterchangeReversedListExpected;
    private static List<Station> noInterchangeReversedListActual;
    private static List<Station> oneInterchangeReversedListExpected;
    private static List<Station> oneInterchangeReversedListActual;
    private static List<Station> oneInterchangeViaConnection3linesReversedListExpected;
    private static List<Station> oneInterchangeViaConnection3linesReversedListActual;
    private static List<Station> twoInterchangeForTwoConnectionsListExpected;
    private static List<Station> twoInterchangeForTwoConnectionsListActual;

    private static List<Station> connectionLine1Line2;
    private static List<Station> connectionLine1Line3;
    private static List<Station> connectionLine1Line4Line5;

    private static List<Station> connectionLine2Line3;
    private static List<Station> connectionLine2Line4;
    private static List<Station> connectionLine2Line5;

    private static Line line1;
    private static Line line2;
    private static Line line3;
    private static Line line4;
    private static Line line5;

    public static <T> Collector<T, ?, List<T>> reversed() {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            Collections.reverse(list);
            return list;
        });
    }

    private static List<Station> stationFactory(Integer numberOfStations, Line line) {
        List<Station> stationList = new ArrayList<>();
        for (int i = 0; i < numberOfStations; i++) {
            stationList.add(new Station(("Station_" + i + "_Line" + line.getName()), line));
        }

        return stationList;
    }

    @BeforeAll
    @DisplayName("Initialize variables and fields")
    public static void setUp() {
        route = new ArrayList<>();
        sameStationListExpected = new ArrayList<>();
        sameStationListActual = new ArrayList<>();
        noInterchangeListExpected = new ArrayList<>();
        noInterchangeListActual = new ArrayList<>();
        noInterchangeReversedListExpected = new ArrayList<>();
        noInterchangeReversedListActual = new ArrayList<>();
        oneInterchangeListExpected = new ArrayList<>();
        oneInterchangeListActual = new ArrayList<>();
        oneInterchangeReversedListExpected = new ArrayList<>();
        oneInterchangeReversedListActual = new ArrayList<>();
        oneInterchangeViaConnection3linesListExpected = new ArrayList<>();
        oneInterchangeViaConnection3linesListActual = new ArrayList<>();
        oneInterchangeViaConnection3linesReversedListExpected = new ArrayList<>();
        oneInterchangeViaConnection3linesReversedListActual = new ArrayList<>();
        twoInterchangeForTwoConnectionsListExpected = new ArrayList<>();
        twoInterchangeForTwoConnectionsListActual = new ArrayList<>();

        connectionLine1Line2 = new ArrayList<>();
        connectionLine1Line3 = new ArrayList<>();
        connectionLine1Line4Line5 = new ArrayList<>();
        connectionLine2Line3 = new ArrayList<>();
        connectionLine2Line4 = new ArrayList<>();
        connectionLine2Line5 = new ArrayList<>();

        line1 = new Line(1, "One");
        line2 = new Line(2, "Two");
        line3 = new Line(3, "Three");
        line4 = new Line(4, "Four");
        line5 = new Line(5, "Five");

        stationFactory(6, line1).forEach(station -> line1.addStation(station));
        route.addAll(line1.getStations());

        stationFactory(8, line2).forEach(station -> line2.addStation(station));
        route.addAll(line2.getStations());

        stationFactory(2, line3).forEach(station -> line3.addStation(station));
        route.addAll(line3.getStations());

        stationFactory(6, line4).forEach(station -> line4.addStation(station));
        route.addAll(line4.getStations());

        stationFactory(4, line5).forEach(station -> line5.addStation(station));
        route.addAll(line5.getStations());

        connectionLine1Line2.addAll(Stream.of(line1.getStations().get(4),
                line2.getStations().get(6)).collect(Collectors.toList()));
        connectionLine1Line3.addAll(Stream.of(line1.getStations().get(2),
                line3.getStations().get(0)).collect(Collectors.toList()));
        connectionLine1Line4Line5.addAll(Stream.of(line1.getStations().get(3),
                line4.getStations().get(3), line5.getStations().get(3)).collect(Collectors.toList()));
        connectionLine2Line3.addAll(Stream.of(line2.getStations().get(3),
                line3.getStations().get(1)).collect(Collectors.toList()));
        connectionLine2Line4.addAll(Stream.of(line2.getStations().get(5),
                line4.getStations().get(4)).collect(Collectors.toList()));
        connectionLine2Line5.addAll(Stream.of(line2.getStations().get(4),
                line5.getStations().get(2)).collect(Collectors.toList()));

        stationIndex.addConnection(connectionLine1Line2);
        stationIndex.addConnection(connectionLine1Line3);
        stationIndex.addConnection(connectionLine1Line4Line5);
        stationIndex.addConnection(connectionLine2Line3);
        stationIndex.addConnection(connectionLine2Line4);
        stationIndex.addConnection(connectionLine2Line5);

        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);
        stationIndex.addLine(line4);
        stationIndex.addLine(line5);

        route.forEach(station -> stationIndex.addStation(station));

        calculator = new RouteCalculator(stationIndex);

        sameStationListExpected.add(line1.getStations().get(0));
        sameStationListActual = calculator.getShortestRoute(line1.getStations().get(0), line1.getStations().get(0));

        noInterchangeListExpected.addAll(line1.getStations());
        noInterchangeListActual = calculator.getShortestRoute(line1.getStations().get(0),
                line1.getStations().get(line1.getStations().size() - 1));

        noInterchangeReversedListExpected.addAll(noInterchangeListExpected.stream().collect(reversed()));
        noInterchangeReversedListActual = calculator.getShortestRoute(line1.getStations()
                .get(line1.getStations().size() - 1), line1.getStations().get(0));

        oneInterchangeListExpected.addAll(Stream.of(line1.getStations().get(0), line1.getStations().get(1),
                line1.getStations().get(2), line1.getStations().get(3), line1.getStations().get(4),
                line2.getStations().get(6), line2.getStations().get(7)).collect(Collectors.toList()));
        oneInterchangeListActual = calculator.getShortestRoute(line1.getStations().get(0), line2.getStations().get(7));

        oneInterchangeReversedListExpected.addAll(oneInterchangeListExpected.stream().collect(reversed()));
        oneInterchangeReversedListActual = calculator.getShortestRoute(line2.getStations().get(7), line1.getStations()
                .get(0));

        oneInterchangeViaConnection3linesListExpected.addAll(line5.getStations());
        oneInterchangeViaConnection3linesListExpected.addAll(Stream.of(line4.getStations().get(3),
                line4.getStations().get(4), line4.getStations().get(5)).collect(Collectors.toList()));
        oneInterchangeViaConnection3linesListActual = calculator.getShortestRoute(line5.getStations().get(0),
                line4.getStations().get(5));

        twoInterchangeForTwoConnectionsListExpected.addAll(Stream.of(line1.getStations().get(0),
                line1.getStations().get(1), line1.getStations().get(2),
                line3.getStations().get(0), line3.getStations().get(1),
                line2.getStations().get(3), line2.getStations().get(2), line2.getStations().get(1),
                line2.getStations().get(0)).collect(Collectors.toList()));
        twoInterchangeForTwoConnectionsListActual = calculator.getShortestRoute(line1.getStations().get(0),
                line2.getStations().get(0));

        route = calculator.getShortestRoute(line1.getStations().get(0), line5.getStations().get(0));
    }

    @Test
    @DisplayName("Testing method CalculateDuration")
    public void testCalculateDuration() {
        double actual = RouteCalculator.calculateDuration(route);
        double expected = (route.size() - 2) * 2.5 + 3.5;
        assertEquals(expected, actual, () -> expected > actual ? "Меньше, чем должно быть" : "Больше, чем должно быть");
    }

    @Nested
    @DisplayName("Testing method getShortestRoute")
    class testGetShortestRoute {

        @Test
        @DisplayName("Testing method getShortestRoute with same station from and to")
        public void testGetShortestRouteSameStation() {
            assertEquals(sameStationListExpected, sameStationListActual);
        }

        @Test
        @DisplayName("Testing method getShortestRoute 2 stations on same line with no interchange")
        public void testGetShortestRouteNoInterchange() {
            assertEquals(noInterchangeListExpected, noInterchangeListActual);
        }

        @Test
        @DisplayName("Testing method getShortestRoute with 2 stations on same line in reversed direction with no interchange")
        public void testGetShortestRouteTwoStationsSameLineReversed() {
            assertEquals(noInterchangeReversedListExpected, noInterchangeReversedListActual);
        }

        @Test
        @DisplayName("Testing method getShortestRoute 2 stations on 2 lines with 1 interchange")
        public void testGetShortestRouteOneInterchange() {
            assertEquals(oneInterchangeListExpected, oneInterchangeListActual);
        }

        @Test
        @DisplayName("Testing method getShortestRoute with 2 stations on 2 lines in reversed direction with one interchange")
        public void testGetShortestRouteTwoStationsOneInterchange() {
            assertEquals(oneInterchangeReversedListExpected, oneInterchangeReversedListActual);
        }

        @Test
        @DisplayName("Testing method getShortestRoute with 2 stations via connection of 3 lines with 1 interchange")
        public void testGetShortestRouteTwoStationsOneInterchangesViaConnections3lines() {
            assertEquals(oneInterchangeViaConnection3linesListExpected, oneInterchangeViaConnection3linesListActual);
        }

        @Test
        @DisplayName("Testing method getShortestRoute with 2 stations via connection of 3 lines with 1 interchange in reversed direction")
        public void testGetShortestRouteTwoStationsOneInterchangesViaConnections3linesReversed() {
            assertEquals(oneInterchangeViaConnection3linesReversedListExpected, oneInterchangeViaConnection3linesReversedListActual);
        }

        @Test
        @DisplayName("Testing method getShortestRoute with 2 stations and 2 interchanges")
        public void testGetShortestRouteTwoInterchangesForTwoConnections() {
            assertEquals(twoInterchangeForTwoConnectionsListExpected, twoInterchangeForTwoConnectionsListActual);
        }

        @Test
        @DisplayName("Testing method getShortestRoute with 2 stations and 2 interchanges reversed")
        public void testGetShortestRouteTwoInterchangesForTwoConnectionsReversed() {
            assertEquals(twoInterchangeForTwoConnectionsListExpected, twoInterchangeForTwoConnectionsListActual);
        }
    }
}