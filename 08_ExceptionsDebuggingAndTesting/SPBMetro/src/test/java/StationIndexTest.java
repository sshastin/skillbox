import core.Line;
import core.Station;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StationIndexTest {
    Line line;
    StationIndex stationIndex;
    Station station1;
    Station station2;
    Station station3;
    List<Station> connections;
    Set<Station> connectionsStation1Expected;

    @BeforeAll
    @DisplayName("Initialize variables and fields")
    void setUp() {
        line = new Line(1, "Line");
        stationIndex = new StationIndex();
        station1 = new Station("Station1", line);
        station2 = new Station("Station2", line);
        station3 = new Station("Station3", line);
        connections = new ArrayList<>();
        connectionsStation1Expected = new TreeSet<>();


        connections.addAll(Stream.of(station1, station2).collect(Collectors.toList()));
        stationIndex.addConnection(connections);
        stationIndex.addLine(line);
        stationIndex.addStation(station1);
        connectionsStation1Expected.add(station2);
    }

    @Test
    @DisplayName("Testing method addStation")
    void addStation() {
        assertEquals(station1, stationIndex.getStation("Station1"));
    }

    @Test
    @DisplayName("Testing method addLine")
    void addLine() {
        assertEquals(line, stationIndex.getLine(1));
    }

    @Test
    @DisplayName("Testing method addConnection")
    void addConnection() {
        assertEquals(connectionsStation1Expected, stationIndex.getConnectedStations(station1));
    }

    @Test
    @DisplayName("Testing method getLine")
    void getLine() {
        assertEquals(line, stationIndex.getLine(1));
    }

    @Test
    @DisplayName("Testing method getStation with 1 String arg should return null")
    void testGetStationWithIOneArg_ShouldReturnNull() {
        assertEquals(null, stationIndex.getStation("StationNotExist"));
    }

    @Test
    @DisplayName("Testing method getStation with 2 arg")
    void testGetStationWithTwoArgs() {
        assertEquals(station1, stationIndex.getStation("Station1", line.getNumber()));
    }

    @Test
    @DisplayName("Testing method getConnectedStations should return null")
    void getConnectedStations() {
        assertEquals(new TreeSet<>(), stationIndex.getConnectedStations(station3));
    }
}