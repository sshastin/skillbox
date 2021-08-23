import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@ToString
@EqualsAndHashCode
public class Line implements Serializable {
    private String number;

    private List<Station> stations;

    public Line(@JsonProperty("number") String number, @JsonProperty("stations") List<Station> stations) {
        this.number = number;
        this.stations = stations;
    }

    @JsonCreator
    public Line() {
        super();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }
}