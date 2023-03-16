package deletecouriertest;

import createcourier.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class DeleteCourierTest {
    RandomDataForCourier randomData = new RandomDataForCourier();
    CourierRequest courierRequest = new CourierRequest();
    Courier courier = new CourierV1(randomData.generateLogin(),randomData.generatePassword(),randomData.generateFirstName());
    @Test
    @DisplayName("Успешное удаление курьера возвращает ok: true")
    @Description("Проверяем наличие 200, ok = true")
    public void deleteCourierReturnOkTrueInResponse() {
        courierRequest.createCourier(courier);
        Response response = courierRequest.loginCourier(courier);
        Response deleteResponse = courierRequest.deleteCourier(response);
        deleteResponse.then().assertThat().statusCode(200)
                .and().body("ok", equalTo(true));
    }
}
