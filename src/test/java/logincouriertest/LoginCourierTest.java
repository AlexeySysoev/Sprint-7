package logincouriertest;
import createcourier.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
public class LoginCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    //Курьер может авторизоваться /api/v1/courier/login
    //получаем id при успешной авторизации
    public void checkLoginCourierWithRequiredFieldsPassed() {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierWithoutFirstName(randomData.generateLogin(),randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Отправляем Пост на создание курьера
        courierRequest.createCourier(courier);
        //Логин курьера и получение тела ответа
        Response response = courierRequest.loginCourier(courier);
        //Проверка тела и статускода
        response.then().assertThat().body("id", notNullValue())
                .and().statusCode(200);
        //удаляем курьера из базы данных
        courierRequest.deleteCourier(courierRequest.getCourierId(courier));
    }
    @Test
    //Попытка логина без поля login
    //Проверяем тело ответа и статускод 400
    public void checkLoginCourierWithoutLoginFieldReturnBadRequest() {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierWithOnlyPassword(randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Отправляем Пост на создание курьера
        courierRequest.createCourier(courier);
        //Логин курьера и получение тела ответа
        Response response = courierRequest.loginCourier(courier);
        //Проверка тела и статускода
        response.then().assertThat().body("message",equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
    }
    @Test
    //Попытка логина без поля password
    //Проверяем тело ответа и статускод 400
    public void checkLoginCourierWithoutPasswordFieldReturnBadRequest() {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        String login = randomData.generateLogin();
        Courier courier = //создаем курьера с рандомными данными
                new CourierWithoutFirstName(login,randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Отправляем Пост на создание курьера
        courierRequest.createCourier(courier);
        //Делаем курьера без поля password
        Courier courierOnlyLogin = new CourierWithOnlyLogin(login);
        //Логин курьера и получение тела ответа
        Response response = courierRequest.loginCourier(courierOnlyLogin);
        //Проверка тела и статускода
        response.then().assertThat().body("message",equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
        //Сервер падает на 504, составить репорт
    }
    @Test
    //Попытка авторизации с неверным(несуществующим) login
    //Проверяем тело ответа и статускод 404
    public void checkLoginCourierWithInvalidLoginFieldReturnNotFound() {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierWithoutFirstName(randomData.generateLogin(),randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Пропускаем шаг создания курьера в БД и отправляем запрос на логин
        Response response = courierRequest.loginCourier(courier);
        //Проверка тела ответа и статускода
        response.then().assertThat().body("message",equalTo("Учетная запись не найдена"))
                .and().statusCode(404);
    }
    @Test
    //Попытка авторизации с неверным(несуществующим) password
    //Проверяем тело ответа и статускод 404
    public void checkLoginCourierWithInvalidPasswordFieldReturnNotFound() {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        String login = randomData.generateLogin();
        Courier courier = //создаем курьера с рандомными данными
                new CourierWithoutFirstName(login,randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Отправляем Пост на создание курьера
        courierRequest.createCourier(courier);
        //Замена пароля на несуществующий в системе
        Courier courierWithWrongPassword = new CourierWithoutFirstName(login,randomData.generatePassword());
        //Попытка логина курьера и получение тела ответа
        Response response = courierRequest.loginCourier(courierWithWrongPassword);
        response.then().assertThat().body("message",equalTo("Учетная запись не найдена"))
                .and().statusCode(404);
    }
}
