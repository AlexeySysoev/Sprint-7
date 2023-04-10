import createcourier.*;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class DeleteCourierTest {
    private CourierRequest courierRequest = new CourierRequest();
    private Creater creater = new Creater();
    private Courier courier = creater.createCourier();
    @Test
    @DisplayName("Успешное удаление курьера возвращает ok: true")
    @Description("Проверяем наличие 200, ok = true")
    public void deleteCourierReturnOkTrueInResponse() throws InterruptedException {
        courierRequest.createCourier(courier);
        Response response = courierRequest.loginCourier(courier);
        Response deleteResponse = courierRequest.deleteCourier(response);
        deleteResponse.then().assertThat().statusCode(200)
                .and().body("ok", equalTo(true));
    }
    @Test
    @DisplayName("удаление курьера без id возвращает 400: Недостаточно данных для удаления курьера")
    @Description("Проверяем код 400, message:  Недостаточно данных для удаления курьера")
    @Issue("Bug - получаем 404 вместо 400")
    public void deleteCourierWithoutIdReturnBadRequest() throws InterruptedException {
        courierRequest.createCourier(courier);
        courierRequest.loginCourier(courier);
        Response deleteResponse = courierRequest.deleteCourier("");
        deleteResponse.then().assertThat().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для удаления курьера"));
    }
    @Test
    @DisplayName("удаление курьера c несуществующим id возвращает 404: Курьера с таким id нет")
    @Description("Проверяем наличие 404, message: Курьера с таким id нет")
    public void deleteCourierWithWrongIdReturnNotFound() throws InterruptedException {
        courierRequest.createCourier(courier);
        courierRequest.loginCourier(courier);
        String id = RandomStringUtils.randomNumeric(6);
        Response deleteResponse = courierRequest.deleteCourier(id);
        deleteResponse.then().assertThat().statusCode(404)
                .and().body("message", equalTo("Курьера с таким id нет."));
    }
}
