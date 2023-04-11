package utils;


import io.restassured.common.mapper.TypeRef;
import specs.Specs;
import java.util.*;

public class MetroStationMap extends Specs {
    public Map<Integer, String> getMetroStationObj() throws InterruptedException {
        //получаем актуальный список станций метро (номер - название станции)
        List<Map<String, Object>> listObg=
                baseSpec()
                        .get(SEARCH_METRO_STATIONS)
                        .as(new TypeRef<>() {});
        Map<Integer, String> actualStations= new TreeMap<>();
        for (Map<String, Object> stringObjectMap : listObg) {
            actualStations.put(Integer.parseInt(stringObjectMap.get("number").toString()), stringObjectMap.get("name").toString());
        }
        return actualStations;
    }
}
