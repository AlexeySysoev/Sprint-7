package logincouriertest;
import createcourier.*;
import io.qameta.allure.Issue;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import io.qameta.allure.junit4.DisplayName; // импорт DisplayName
import io.qameta.allure.Description; // импорт Description

public class LoginCourierTest {
    private final String uri = "http://qa-scooter.praktikum-services.ru";
    @Before
    public void setUp(){
        RestAssured.baseURI=uri;
    }
    @Test
    @DisplayName("Корректный логин курьера")
    @Description("Проверяем тело ответа и статускод 200")
    public void checkLoginCourierWithRequiredFieldsPassed() {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierV1(randomData.generateLogin(),randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        courierRequest.createCourier(courier); //Отправляем Пост на создание курьера
        Response response = courierRequest.loginCourier(courier); //Логин курьера и получение тела ответа
        //Проверка тела и статускода
        response.then().assertThat().body("id", notNullValue())
                .and().statusCode(200);
        //удаляем курьера из базы данных
        courierRequest.deleteCourier(courierRequest.getCourierId(courier));
    }
    @Test
    @DisplayName("Логин курьера без поля login")
    @Description("Проверяем тело ответа и статускод 400")
    public void checkLoginCourierWithoutLoginFieldReturnBadRequest() {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierV3(randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        courierRequest.createCourier(courier); //Отправляем Пост на создание курьера
        Response response = courierRequest.loginCourier(courier); //Логин курьера и получение тела ответа
        //Проверка тела и статускода
        response.then().assertThat().body("message",equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
    }
    @Test
    @DisplayName("Логин курьера без поля password")
    @Description("Проверяем тело ответа и статускод 400")
    @Issue("BUG-001")
    public void checkLoginCourierWithoutPasswordFieldReturnBadRequest() {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        String login = randomData.generateLogin();
        Courier courier = //создаем курьера с рандомными данными
                new CourierV1(login,randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Отправляем Пост на создание курьера
        courierRequest.createCourier(courier);
        //Делаем курьера без поля password
        Courier courierOnlyLogin = new CourierV1(login);
        //Логин курьера и получение тела ответа
        Response response = courierRequest.loginCourier(courierOnlyLogin);
        //Проверка тела и статускода
        response.then().assertThat().body("message",equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
    }
    @Test
    @DisplayName("Логин курьера  неверным(несуществующим) login")
    @Description("Проверяем тело ответа и статус код 400")
    public void checkLoginCourierWithInvalidLoginFieldReturnNotFound() {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierV1(randomData.generateLogin(),randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Пропускаем шаг создания курьера в БД и отправляем запрос на логин
        Response response = courierRequest.loginCourier(courier);
        //Проверка тела ответа и статускода
        response.then().assertThat().body("message",equalTo("Учетная запись не найдена"))
                .and().statusCode(404);
    }
    @Test
    @DisplayName("Логин курьера  неверным(несуществующим) password")
    @Description("Проверяем тело ответа и статус код 404")
    public void checkLoginCourierWithInvalidPasswordFieldReturnNotFound() {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        String login = randomData.generateLogin();
        Courier courier = //создаем курьера с рандомными данными
                new CourierV1(login,randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Отправляем Пост на создание курьера
        courierRequest.createCourier(courier);
        //Замена пароля на несуществующий в системе
        Courier courierWithWrongPassword = new CourierV1(login,randomData.generatePassword());
        //Попытка логина курьера и получение тела ответа
        Response response = courierRequest.loginCourier(courierWithWrongPassword);
        response.then().assertThat().body("message",equalTo("Учетная запись не найдена"))
                .and().statusCode(404);
    }
}
