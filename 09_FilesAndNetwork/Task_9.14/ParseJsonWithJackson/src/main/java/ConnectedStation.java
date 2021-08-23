import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ConnectedStation {
    private String line;
    private Station station;

    @JsonCreator
    public ConnectedStation() {
        super();
    }

    public ConnectedStation(@JsonProperty("line") String line, @JsonProperty("station") Station station) {
        this.line = line;
        this.station = station;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}