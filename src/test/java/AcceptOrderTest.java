import createcourier.Courier;
import createcourier.CourierRequest;
import createcourier.CourierV1;
import createorderdata.CreateOrderData;
import createorderdata.Creater;
import createorderdata.Order;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class AcceptOrderTest {
    private Creater creater = new Creater();
    private Order sendOrder = new Order();
    private CreateOrderData orderData = creater.createOrder();
    private CourierRequest courierRequest = new CourierRequest();
    @Test
    @DisplayName("Проверка успешного принятия заказа курьером")
    @Description("Ожидаем 200 и ок-true")
    public void checkCreateOrderColorField() throws InterruptedException {
        Response response = sendOrder.orderRequest(orderData); //Создание заказа
        int track  = sendOrder.getTrackNumberOfOrder(response); //Получаем трек номер заказа
        int orderId = sendOrder.getOrderNumberByTrack(track); //Получаем id заказа по треку
        CourierV1 courier = creater.createCourier();
        courierRequest.createCourier(courier);
        courier.setFirstName(null);
        Thread.sleep(500);
        int courierId = courierRequest.getCourierId(courier);
        sendOrder.acceptOrderByCourier(orderId, courierId);
        //System.out.println(courierId);
        //.then().statusCode(200).and().body("ok", equalTo(true));


        //sendOrder.cancelOrder(track); //Отмена заказа
    }

}
