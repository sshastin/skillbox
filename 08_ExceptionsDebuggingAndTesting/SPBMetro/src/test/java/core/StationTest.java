package core;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StationTest {
    private Station station1;
    private Station station2;
    private Station station3OnLine2;
    private Line line1;
    private Line line2;
    private String station1Name;
    private String station2Name;
    private String station3OnLine2Name;
    private String line1Name;
    private String line2Name;
    private Integer line1Number;
    private Integer line2Number;
    private Integer compareToExpectedGT;
    private Integer compareToExpectedLT;
    private Integer compareToExpectedEQ;
    private Boolean equalsGTExpected;
    private Boolean equalsLTExpected;
    private Boolean equalsEQExpected;

    @BeforeAll
    @DisplayName("Initialize variables and fields")
    void setUp() {
        station1Name = "Station1";
        station2Name = "Station2";
        station3OnLine2Name = "Station3OnLine2";
        line1Name = "Line1";
        line2Name = "Line2";
        line1Number = 1;
        line2Number = 2;
        compareToExpectedGT = 1;
        compareToExpectedLT = -1;
        compareToExpectedEQ = 0;
        line1 = new Line(line1Number, line1Name);
        line2 = new Line(line2Number, line2Name);
        station1 = new Station(station1Name, line1);
        station2 = new Station(station2Name, line1);
        station3OnLine2 = new Station(station2Name, line2);
        equalsGTExpected = false;
        equalsLTExpected = false;
        equalsEQExpected = true;
    }

    @AfterAll
    void tearDown() {
        System.out.println("Congrats! All tests are passed!");
    }

    @Test
    @DisplayName("Testing getLine")
    void getLine() {
        assertEquals(line1, station1.getLine());
    }

    @Test
    @DisplayName("Testing getName")
    void getName() {
        assertEquals(station1Name, station1.getName());
    }

    @Nested
    @DisplayName("Testing compareTo method cases LT, LT, EQ")
    class CompareToTest {
        @Test
        @DisplayName("Testing compareTo station1 to station2 case LT")
        void testCompareToStation1ToStation2LT_ShouldReturnPositive() {
            assertEquals(compareToExpectedLT, station1.compareTo(station2));
        }

        @Test
        @DisplayName("Testing compareTo station2 to station1 case GT")
        void testCompareToStation2ToStation1GT_ShouldReturnNegative() {
            assertEquals(compareToExpectedGT, station2.compareTo(station1));
        }

        @Test
        @DisplayName("Testing compareTo station1 to station1 case EQ")
        void testCompareToStation1ToStation1EQ_ShouldReturnZero() {
            assertEquals(compareToExpectedEQ, station1.compareTo(station1));
        }

        @Test
        @DisplayName("Testing compareTo station1 to station3")
        void testCompareToStation1ToStation3_ShouldReturnNotZero() {
            assertNotEquals(compareToExpectedEQ, station1.compareTo(station3OnLine2));
        }
    }

    @Nested
    @DisplayName("Testing equals method cases LT, GT, EQ")
    class TestEquals {

        @Test
        @DisplayName("Testing equals station1 to station2 case LT")
        void testEqualsStation1ToStation2LT_ShouldReturnFalse() {
            assertEquals(equalsLTExpected, station1.equals(station2));
        }

        @Test
        @DisplayName("Testing equals station2 to station1 case GT")
        void testEqualsStation2ToStation1GT_ShouldReturnFalse() {
            assertEquals(equalsGTExpected, station2.equals(station1));
        }

        @Test
        @DisplayName("Testing equals station1 to station1 case EQ")
        void testEqualsStation1ToStation1_ShouldReturnZero() {
            assertEquals(equalsEQExpected, station1.equals(station1));
        }
    }

    @Test
    @DisplayName("Testing toString method")
    void testToString() {
        assertEquals(station2Name, station2.toString());
    }
}