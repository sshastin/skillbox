package core;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LineTest {
    private Line line1;
    private Line line2;
    private String lineNameExpected;
    private String lineNameActual;
    private Integer lineNumberExpected;
    private Integer lineNumberActual;
    private Integer sizeOfStationListExpected;
    private Integer sizeOfStationListActual;
    private Integer compareToExpectedGT;
    private Integer compareToExpectedLT;
    private Integer compareToExpectedEQ;
    private List<Station> stationListExpected;
    private Boolean equalsGTExpected;
    private Boolean equalsLTExpected;
    private Boolean equalsEQExpected;

    @BeforeAll
    @DisplayName("Initialize variables and fields")
    void setUp() {
        stationListExpected = new ArrayList<>();
        lineNameExpected = "LineNumberOne";
        lineNumberExpected = 1;
        compareToExpectedGT = 1;
        compareToExpectedLT = -1;
        compareToExpectedEQ = 0;
        line1 = new Line(lineNumberExpected, lineNameExpected);
        line2 = new Line(2, "LineThatIsNotNumberOne");
        lineNumberActual = line1.getNumber();
        lineNameActual = line1.getName();
        equalsGTExpected = false;
        equalsLTExpected = false;
        equalsEQExpected = true;


        for (int i = 0; i < 5; i++) {
            stationListExpected.add(new Station(("Station" + i), line1));
        }

        stationListExpected.forEach(station -> line1.addStation(station));

        sizeOfStationListExpected = stationListExpected.size();
        sizeOfStationListActual = line1.getStations().size();
    }

    @AfterAll
    void tearDown() {
        System.out.println("Congrats! All tests are passed!");
    }

    @Test
    @DisplayName("Testing getNumber method")
    void testGetNumber() {
        assertEquals(lineNumberExpected, lineNumberActual);
    }

    @Test
    @DisplayName("Testing getName method")
    void testGetName() {
        assertEquals(lineNameExpected, lineNameActual);
    }

    @Test
    @DisplayName("Testing addStation method")
    void testAddStation() {
        assertEquals(sizeOfStationListExpected, sizeOfStationListActual);
    }

    @Test
    @DisplayName("Testing getStations")
    void testGetStations() {
        assertIterableEquals(stationListExpected, line1.getStations());
    }

    @Nested
    @DisplayName("Testing compareTo method cases LT, LT, EQ")
    class CompareToTest {
        @Test
        @DisplayName("Testing compare line1 to line2 case LT")
        void testCompareToLine1ToLine2LT_ShouldReturnPositive() {
            assertEquals(compareToExpectedLT, line1.compareTo(line2));
        }

        @Test
        @DisplayName("Testing compare line2 to line1 case GT")
        void testCompareToLine2ToLine1GT_ShouldReturnNegative() {
            assertEquals(compareToExpectedGT, line2.compareTo(line1));
        }

        @Test
        @DisplayName("Testing compare line1 to line1 case EQ")
        void testCompareToLine1ToLine1EQ_ShouldReturnZero() {
            assertEquals(compareToExpectedEQ, line1.compareTo(line1));
        }
    }

    @Nested
    @DisplayName("Testing equals method cases LT, GT, EQ")
    class EqualsTest {
        @Test
        @DisplayName("Testing equals line1 to line2 case LT")
        void testEqualsLine1ToLine2LT_ShouldReturnFalse() {
            assertEquals(equalsLTExpected, line1.equals(line2));
        }

        @Test
        @DisplayName("Testing equals line2 to line1 case GT")
        void testEqualsLine2ToLine1GT_ShouldReturnFalse() {
            assertEquals(equalsGTExpected, line2.equals(line1));
        }

        @Test
        @DisplayName("Testing equals line1 to line1 case EQ")
        void testEqualsLine1ToLine1_ShouldReturnZero() {
            assertEquals(equalsEQExpected, line1.equals(line1));
        }
    }
}