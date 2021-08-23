import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ToString
@EqualsAndHashCode
public class Connection {
    private List<ConnectedStation> connectionList = new ArrayList<>();

    public void addConnectedStation(ConnectedStation connectedStation) {
        connectionList.add(connectedStation);
        connectionList.sort(Comparator.comparing(ConnectedStation::getLine));
    }

    public Connection(@JsonProperty("connection") List<ConnectedStation> connectionList) {
        this.connectionList = connectionList;
    }

    @JsonCreator
    public Connection() {
        super();
    }
}