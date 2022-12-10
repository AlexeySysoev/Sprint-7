package getordertest;

import createcourier.*;
import createorderdata.CreateOrderData;
import createorderdata.Order;
import createorderdata.RandomDataForOrder;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderTest {
    // /api/v1/orders
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    //Все активные заказы курьера - /v1/orders?courierId=1
    //создать курьера
    //залогиниться - взять id
    //создать заказ/заказы
    //принять заказы
    //получить список заказов, проверить статус код 200 и тело
    public void checkAllActiveCourierOrders(){
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierWithoutFirstName(randomData.generateLogin(),randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Отправляем Пост на создание курьера
        courierRequest.createCourier(courier);
        //Логин курьера и получение тела ответа
        int courierId = courierRequest.getCourierId(courier);
        System.out.println(courierId);
        //создаем объект для заказа
        RandomDataForOrder data = new RandomDataForOrder();
        CreateOrderData orderData = new CreateOrderData(data.generateName(),
                                                    data.generateName(),
                                                    data.generateAddress(),
                                                    data.generateMetroStation(),
                                                    data.generatePhoneNumber(),
                                                    data.generateRentTime(),
                                                    data.getDeliveryDate(),
                                                    data.generateComment(),
                                                    Arrays.asList("BLACK"));
        Order order = new Order();
        //создание заказа в бд и получение трек номера
        int track = order.getTrackNumberOfOrder(orderData);

        //принять заказы
        order.acceptOrder(track,courierId);
        //проверить тело и статускод
        order.getOrderWithId(courierId).then().body("orders", notNullValue()).and().statusCode(200);


    }
    @Test
    //Все активные заказы курьера на станциях - /v1/orders?courierId=1&nearestStation=["1", "2"]
    //создать курьера
    //залогиниться - взять id
    //создать заказ/заказы
    //получить список заказов с фильтром станций, проверить статус код и тело
    public void checkAllActiveCouriersOrdersNearestStation() {

    }
    @Test
    //Заказы, доступные для взятия курьером - /v1/orders?limit=10&page=0
    //создать заказы
    //получить список заказов и проверить статус код и тело
    public void checkAvailableForCourierOrders(){

    }
    @Test
    //Заказы, доступные для взятия курьером возле станции метро - /v1/orders?limit=10&page=0&nearestStation=["110"]
    //создать заказы
    //получить список заказов с фильтром станций и проверить статус код и тело
    public void checkAvailableForCourierOrdersNearestStation(){

    }
    @Test
    //запрос с несуществующим id курьера 404
    //Отправить запрос без создания курьера http://qa-scooter.praktikum-services.ru/api/v1/orders?courierId=789
    public void checkAllActiveCourierOrdersWithWrongCourierIdReturnNotFound(){
        Order order = new Order();
        RandomDataForCourier randomCourierId = new RandomDataForCourier();
        int id = randomCourierId.generateCourierId();
        Response response = order.getOrderWithId(id);
        response.then().assertThat().body("message",equalTo("Курьер с идентификатором "+ id +" не найден") ).and().statusCode(404);
    }
}
