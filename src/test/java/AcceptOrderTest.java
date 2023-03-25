import createorderdata.CreateOrderData;
import createorderdata.Creater;
import createorderdata.Order;
import createorderdata.RandomDataForOrder;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

public class AcceptOrderTest {
    private Creater creater = new Creater();
    private Order sendOrder = new Order();
    private CreateOrderData orderData = creater.createOrder();
    @Test
    @DisplayName("")
    @Description("")
    public void checkCreateOrderColorField() {
        //Создание заказа
        Response response = sendOrder.orderRequest(orderData);
        //Получаем трек номер заказа
        int track  = sendOrder.getTrackNumberOfOrder(response);
        //Получаем id заказа
        System.out.println(sendOrder.getOrderByTrack(track).statusCode());

        //Отмена заказа
        //sendOrder.cancelOrder(track);
    }
    @Test
    public void listViewerTest() {
        Creater creater = new Creater();
        creater.listViewer();
    }
}
