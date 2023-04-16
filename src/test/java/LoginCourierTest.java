import createcourier.*;
import io.qameta.allure.Issue;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

public class LoginCourierTest {
    private Creater creater = new Creater();
    private CourierRequest courierRequest = new CourierRequest();
    @Test
    @DisplayName("Успешный логин курьера")
    @Description("Проверяем тело ответа и статускод 200")
    public void checkLoginCourierWithRequiredFieldsPassed() throws InterruptedException {
        Courier courier = creater.createCourier();
        courierRequest.createCourier(courier);
        Response response = courierRequest.loginCourier(courier);
        courierRequest.setId(courierRequest.getCourierId(response));
        response.then().assertThat().statusCode(200).and().body("id", notNullValue());
    }
    @Test
    @DisplayName("Логин курьера без поля login")
    @Description("Проверяем тело ответа и статускод 400")
    public void checkLoginCourierWithoutLoginFieldReturnBadRequest() throws InterruptedException {
        Courier courier = creater.createCourier();
        courierRequest.createCourier(courier);
        courier.setLogin(null);
        Response response = courierRequest.loginCourier(courier);
        courierRequest.setId(courierRequest.getCourierId(response));
        response.then().assertThat().statusCode(400)
                .and().body("message",equalTo("Недостаточно данных для входа"));

    }
    @Test
    @DisplayName("Логин курьера без поля password")
    @Description("Проверяем тело ответа и статускод 400")
    @Issue("BUG-001")
    public void checkLoginCourierWithoutPasswordFieldReturnBadRequest() throws InterruptedException {
        Courier courier = creater.createCourier();
        courierRequest.createCourier(courier);
        courier.setPassword(null);
        Response response = courierRequest.loginCourier(courier);
        courierRequest.setId(courierRequest.getCourierId(response));
        response.then().assertThat().statusCode(400)
                .and().body("message",equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Логин курьера  неверным(несуществующим) login")
    @Description("Проверяем тело ответа и статус код 400")
    public void checkLoginCourierWithInvalidLoginFieldReturnNotFound() throws InterruptedException {
        Courier courier = creater.createCourier();
        Response response = courierRequest.loginCourier(courier);
        courierRequest.setId(courierRequest.getCourierId(response));
        response.then().assertThat().statusCode(404)
                .and().body("message",equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Логин курьера  неверным(несуществующим) password")
    @Description("Проверяем тело ответа и статус код 404")
    public void checkLoginCourierWithInvalidPasswordFieldReturnNotFound() throws InterruptedException {
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = creater.createCourier();
        courier.setPassword(randomData.generatePassword());
        Response response = courierRequest.loginCourier(courier);
        courierRequest.setId(courierRequest.getCourierId(response));
        response.then().assertThat().statusCode(404)
                .and().body("message",equalTo("Учетная запись не найдена"));
    }
    @Test
    public void loginSchemaValidate() throws InterruptedException {
        Courier courier = creater.createCourier();
         courierRequest.createCourier(courier);
        Response response = courierRequest.loginCourier(courier);
        courierRequest.setId(courierRequest.getCourierId(response));
        response.then().assertThat()
                .body(matchesJsonSchemaInClasspath("login_response_schema.json"));
    }
    @After
    public void deleteCourier() {
        courierRequest.deleteCourier(courierRequest.getId());
    }
}
