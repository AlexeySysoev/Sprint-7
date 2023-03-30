import createcourier.CourierRequest;
import createcourier.CourierV1;
import createorderdata.OrderData;
import createorderdata.OrderRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class AcceptOrderTest {
    private Creater creater = new Creater();
    private OrderRequest sendOrder = new OrderRequest();
    private OrderData orderData = creater.createOrder();
    private CourierRequest courierRequest = new CourierRequest();
    @Test
    @DisplayName("Проверка успешного принятия заказа курьером")
    @Description("Ожидаем 200 и ок-true")
    public void checkCreateOrderColorField() throws InterruptedException {
        Response response = sendOrder.createOrder(orderData); //Создание заказа
        int track  = sendOrder.getTrackNumberOfOrder(response); //Получаем трек номер заказа
        String orderId = String.valueOf(sendOrder.getOrderNumberByTrack(track)); //Получаем id заказа по треку
        CourierV1 courier = creater.createCourier();
        courierRequest.createCourier(courier);
        courier.setFirstName(null);
        Response loginResponse = courierRequest.loginCourier(courier);
        String courierId = String.valueOf(courierRequest.getCourierId(loginResponse));
        Response orderResponse = sendOrder.acceptOrderByCourier(orderId, courierId);
        orderResponse.then().statusCode(200).and().body("ok", equalTo(true));
        sendOrder.cancelOrder(track); //Отмена заказа
    }
    @Test
    @DisplayName("Запрос без id заказа выдает ошибку")
    @Description("Ожидаем - 400 Conflict, \"message\": \"Недостаточно данных для поиска\"")
    @Issue("Bug-00X 404 вместо 400")
    public void acceptOrderWithoutOrderIdReturnConflict() throws InterruptedException {
        Response response = sendOrder.createOrder(orderData); //Создание заказа
        int track  = sendOrder.getTrackNumberOfOrder(response); //Получаем трек номер заказа
        sendOrder.getOrderNumberByTrack(track); //Получаем id заказа по треку
        CourierV1 courier = creater.createCourier();
        courierRequest.createCourier(courier);
        courier.setFirstName(null);
        Response loginResponse = courierRequest.loginCourier(courier);
        String courierId = String.valueOf(courierRequest.getCourierId(loginResponse));
        Response orderResponse = sendOrder.acceptOrderByCourier("", courierId);
        orderResponse.then().statusCode(400).and().body("message", equalTo("Недостаточно данных для поиска"));
        sendOrder.cancelOrder(track); //Отмена заказа
    }
    @Test
    @DisplayName("Запрос без id курьера выдает ошибку")
    @Description("Ожидаем - 400 Conflict, \"message\": \"Недостаточно данных для поиска\"")
    public void acceptOrderWithoutCourierIdReturnConflict() throws InterruptedException {
        Response response = sendOrder.createOrder(orderData); //Создание заказа
        int track  = sendOrder.getTrackNumberOfOrder(response); //Получаем трек номер заказа
        String orderId = String.valueOf(sendOrder.getOrderNumberByTrack(track)); //Получаем id заказа по треку
        CourierV1 courier = creater.createCourier();
        courierRequest.createCourier(courier);
        courier.setFirstName(null);
        courierRequest.loginCourier(courier);
        Response orderResponse = sendOrder.acceptOrderByCourier(orderId, "");
        orderResponse.then().statusCode(400).and().body("message", equalTo("Недостаточно данных для поиска"));
        sendOrder.cancelOrder(track); //Отмена заказа
    }
    @Test
    @DisplayName("Запрос с неверным id заказа выдает ошибку")
    @Description("Ожидаем - 404 Not Found, \"message\": \"Заказа с таким id не существует\"")
    @Issue("Bug-00X - 409 вместо 404")
    public void acceptOrderWithWrongOrderIdReturnNotFound() throws InterruptedException {
        Response response = sendOrder.createOrder(orderData); //Создание заказа
        int track  = sendOrder.getTrackNumberOfOrder(response); //Получаем трек номер заказа
        String orderId = String.valueOf(sendOrder.getOrderNumberByTrack(track)); //Получаем id заказа по треку
        CourierV1 courier = creater.createCourier();
        courierRequest.createCourier(courier);
        courier.setFirstName(null);
        Response loginResponse = courierRequest.loginCourier(courier);
        String courierId = String.valueOf(courierRequest.getCourierId(loginResponse));
        Response orderResponse = sendOrder.acceptOrderByCourier(String.valueOf(10000 + new Random().nextInt(10000)), courierId);
        orderResponse.then().statusCode(404).and().body("message", equalTo("Заказа с таким id не существует"));
        sendOrder.cancelOrder(track); //Отмена заказа
    }
    @Test
    @DisplayName("Запрос с неверным id курьера выдает ошибку")
    @Description("Ожидаем - 404 Not Found, \"message\": \"Курьера с таким id не существует\"")
    public void acceptOrderWithWrongCourierIdReturnNotFound() throws InterruptedException {
        Response response = sendOrder.createOrder(orderData); //Создание заказа
        int track  = sendOrder.getTrackNumberOfOrder(response); //Получаем трек номер заказа
        String orderId = String.valueOf(sendOrder.getOrderNumberByTrack(track)); //Получаем id заказа по треку
        CourierV1 courier = creater.createCourier();
        courierRequest.createCourier(courier);
        courier.setFirstName(null);
        courierRequest.loginCourier(courier);
        Response orderResponse = sendOrder.acceptOrderByCourier(orderId, String.valueOf(1000 + new Random().nextInt(10000)));
        orderResponse.then().statusCode(404).and().body("message", equalTo("Курьера с таким id не существует"));
        sendOrder.cancelOrder(track); //Отмена заказа
    }
}
