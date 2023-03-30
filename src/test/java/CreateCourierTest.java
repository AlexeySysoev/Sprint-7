import createcourier.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
public class CreateCourierTest {
    private Creater creater = new Creater();
    private CourierRequest courierRequest = new CourierRequest();
    @Test
    @DisplayName("Корректное создание курьера")
    @Description("Проверяем тело ответа и статускод 201")
    public void checkCreateCourierWithWrightData() throws InterruptedException {
        CourierV1 courier = creater.createCourier();
        Response response = courierRequest.createCourier(courier);
        Response loginResponse = courierRequest.loginCourier(courier);
        courierRequest.setId(courierRequest.getCourierId(loginResponse));
        response.then().assertThat().statusCode(201).and().body("ok", equalTo(true));
    }
    @Test
    @DisplayName("Попытка создания уже существующего курьера")
    @Description("Проверяем тело ответа на наличие \"Этот логин уже используется. Попробуйте другой.\" и статус код 409")
    public void checkDoubleCreateCourierReturnConflict() throws InterruptedException {
        CourierV1 courier = creater.createCourier();
        //Отправляем Пост на создание курьера в первый раз
        courierRequest.createCourier(courier);
        //Делаем попытку создать курьера повторно с теми же данными  сохраняем ответ
        Response response = courierRequest.createCourier(courier);
        Response loginResponse = courierRequest.loginCourier(courier);
        courierRequest.setId(courierRequest.getCourierId(loginResponse));
        //Проверяем тело и статускод ответа
        response.then()
                .statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    @Test
    //Попытка создать курьера без поля login
    //Проверяем тело и код 400
    @DisplayName("Попытка создать курьера без поля login")
    @Description("Проверяем тело ответа на наличие \"Недостаточно данных для создания учетной записи\" и статус код 400")
    public void checkCreateCourierWithoutLoginFailed() throws InterruptedException {
        CourierV1 courier = creater.createCourier();
        courier.setLogin(null);
        Response response = courierRequest.createCourier(courier);
        response.then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Попытка создать курьера без поля password")
    @Description("Проверяем тело ответа на наличие \"Недостаточно данных для создания учетной записи\" и статус код 400")
    public void checkCreateCourierWithoutPasswordFailed() throws InterruptedException {
        CourierV1 courier = creater.createCourier();
        courier.setPassword(null);
        Response response = courierRequest.createCourier(courier);
        response.then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Попытка создать курьера без полей login,password")
    @Description("Проверяем тело ответа на наличие \"Недостаточно данных для создания учетной записи\" и статус код 400")
    public void checkCreateCourierWithoutLoginAndPasswordFailed() throws InterruptedException {
        CourierV1 courier = creater.createCourier();
        courier.setLogin(null);
        courier.setPassword(null);
        Response response = courierRequest.createCourier(courier);
        response.then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @After
    public void deleteCourier() {
        courierRequest.deleteCourier(courierRequest.getId());
    }
}
