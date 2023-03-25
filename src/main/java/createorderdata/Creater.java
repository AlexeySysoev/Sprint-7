package createorderdata;

import createcourier.Courier;
import createcourier.CourierV1;
import createcourier.RandomDataForCourier;

import java.util.List;
import java.util.Random;

public class Creater {
    private final List<String> noColor = List.of();
    private final List<String> black= List.of("BLACK");
    private final List<String> grey= List.of("GREY");
    private final List<String> blackAndGrey= List.of("BLACK", "GREY");
    private final List<List<String>> allColorsList = List.of(noColor, black, grey, blackAndGrey);
    private RandomDataForOrder data = new RandomDataForOrder();
    private RandomDataForCourier randomData = new RandomDataForCourier();
    public CreateOrderData createOrder() {
        List<String> randomColorList = allColorsList.get(new Random().nextInt(allColorsList.size()));
        return new CreateOrderData (data.generateName(), data.generateName(), data.generateAddress(),
                data.generateMetroStation(), data.generatePhoneNumber(), data.generateRentTime(),
                data.getDeliveryDate(), data.generateComment(),
                randomColorList);
    }
    public CourierV1 createCourier() {
        return new CourierV1(randomData.generateLogin(),
                             randomData.generatePassword(),
                             randomData.generateFirstName());
    }
}
