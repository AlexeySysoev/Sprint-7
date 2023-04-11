package utils;

import java.util.List;

public class MetroStationDeserializator {
    private List<Stations> stationsList;

    public MetroStationDeserializator() {}

    public List<Stations> getStationsList() {
        return stationsList;
    }

    public void setStationsList(List<Stations> stationsList) {
        this.stationsList = stationsList;
    }
}
