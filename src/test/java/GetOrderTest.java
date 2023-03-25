import createcourier.*;
import createorderdata.CreateOrderData;
import createorderdata.Order;
import createorderdata.RandomDataForOrder;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
public class GetOrderTest {
     private Order order = new Order();
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    @DisplayName("Получение активных заказов курьера")
    @Description("Проверяем наличие в теле ответа объекта \"orders\" и статус код 200")
     public void checkAllActiveCourierOrders() throws InterruptedException {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных курьера
        RandomDataForOrder data = new RandomDataForOrder(); //экземпляр класса для создания данных запроса
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        Order order = new Order(); //экземпляр класса для работы с АПИ заказов
        Courier courier = //создаем объект курьера с рандомными данными
                new CourierV1(randomData.generateLogin(),randomData.generatePassword());
        courierRequest.createCourier(courier); //Отправляем Пост на создание курьера
        int courierId = courierRequest.getCourierId(courier); //Логин курьера и получение id
        //создаем объект для заказа
        CreateOrderData orderData = new CreateOrderData(data.generateName(), data.generateName(), data.generateAddress(),
                                                        data.generateMetroStation(), data.generatePhoneNumber(), data.generateRentTime(),
                                                        data.getDeliveryDate(), data.generateComment(), Arrays.asList("BLACK"));
        int track = order.getTrackNumberOfOrder(orderData); //создание заказа в бд и получение трек номера
        order.acceptOrder(track,courierId); //принять заказы
        //проверить тело и статускод
        order.getOrderWithId(courierId).then().body("orders", notNullValue()).and().statusCode(200);
        courierRequest.deleteCourier(courierId); //удаляем курьера из БД
    }
    @Test
    @DisplayName("Получение активных заказов курьера на станциях метро")
    @Description("Проверяем наличие в теле ответа объекта \"orders\" и статус код 200")
    public void checkAllActiveCouriersOrdersNearestStation() throws InterruptedException {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных курьера
        RandomDataForOrder data = new RandomDataForOrder();//экземпляр класса для создания данных запроса
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        Order order = new Order();////экземпляр класса для работы с АПИ заказов
        Courier courier = //создаем объект курьера с рандомными данными
                new CourierV1(randomData.generateLogin(),randomData.generatePassword());
        courierRequest.createCourier(courier); //Отправляем Пост на создание курьера
        int courierId = courierRequest.getCourierId(courier);//Логин курьера и получение id
        //создаем объект для заказа
        CreateOrderData orderData = new CreateOrderData(data.generateName(), data.generateName(), data.generateAddress(),
                data.generateMetroStation(), data.generatePhoneNumber(), data.generateRentTime(),
                data.getDeliveryDate(), data.generateComment(), Arrays.asList("BLACK"));
        int track = order.getTrackNumberOfOrder(orderData); //создание заказа в бд и получение трек номера
        order.acceptOrder(track,courierId); //принять заказ
        //проверка тела и статус кода
        order.getOrderWithIdIncludeStations(courierId).then().body("orders", notNullValue()).and().statusCode(200);
        courierRequest.deleteCourier(courierId); //удаление
    }
    @Test
    @DisplayName("Получение заказов, доступных для взятия курьером")
    @Description("Проверяем наличие в теле ответа объекта \"orders\" и статус код 200")
    public void checkAccessibleForCourierOrders(){
        RandomDataForOrder data = new RandomDataForOrder();//экземпляр класса для создания данных запроса
        Order order = new Order();////экземпляр класса для работы с АПИ заказов
        //создаем объект для заказа
        CreateOrderData orderData = new CreateOrderData(data.generateName(), data.generateName(), data.generateAddress(),
                data.generateMetroStation(), data.generatePhoneNumber(), data.generateRentTime(),
                data.getDeliveryDate(), data.generateComment(), Arrays.asList("BLACK"));
        order.orderRequest(orderData); //создание заказа в бд
        order.getOrderAccessibleForCourier().then().body("orders", notNullValue()).and().statusCode(200);
    }
    @Test
    @DisplayName("Получение заказов, доступных для взятия курьером возле станции метро")
    @Description("Проверяем наличие в теле ответа объекта \"orders\" и статус код 200")
    public void checkAccessibleForCourierOrdersNearestStation(){
        RandomDataForOrder data = new RandomDataForOrder();//экземпляр класса для создания данных запроса
        Order order = new Order(); //экземпляр класса для работы с АПИ заказов
        //создаем объект для заказа
        CreateOrderData orderData = new CreateOrderData(data.generateName(), data.generateName(), data.generateAddress(),
                data.generateMetroStation(), data.generatePhoneNumber(), data.generateRentTime(),
                data.getDeliveryDate(), data.generateComment(), Arrays.asList("BLACK"));
        order.orderRequest(orderData); //создание заказа в бд
        order.getOrderAccessibleForCourierNearestStation().then().body("orders", notNullValue()).and().statusCode(200);
    }
    @Test
    //запрос с несуществующим id курьера 404
    @DisplayName("Попытка получения заказов с несуществующим id курьера")
    @Description("Проверяем наличие в тело ответа и статус код 404")
    public void checkAllActiveCourierOrdersWithWrongCourierIdReturnNotFound(){
        Order order = new Order(); //экземпляр класса для работы с АПИ заказов
        RandomDataForCourier randomCourierId = new RandomDataForCourier(); //экземпляр класса для создания данных курьера
        int id = randomCourierId.generateCourierId()+1000000; //создание случайного id
        Response response = order.getOrderWithId(id); //отправляем запрос на список заказов курьера с несуществующим id
        response.then().assertThat().body("message",equalTo("Курьер с идентификатором "+ id +" не найден") ).and().statusCode(404);
    }
    @Test
    @DisplayName("Получение заказов без курьера")
    @Description("Осваиваем работу в вложенными объектами")
    public void checkResponseBobyToNestedIssuesGetOrdersWithoutCourier() {
        Response response = order.getOrderWithoutCourier();

        int track =  response.then().extract().path("pageInfo.total");
        System.out.println("track from nested obj ==>> " + track);
    }
}
