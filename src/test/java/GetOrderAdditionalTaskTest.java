import createorderdata.OrderRequest;
import createorderdata.OrderData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import orderbytrackdeserializator.Order;
import orderbytrackdeserializator.OrderByTrack;
import org.junit.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderAdditionalTaskTest {
    private Creater creater = new Creater();
    private OrderRequest orderRequest = new OrderRequest();

    @Test
    @DisplayName("успешный запрос возвращает объект с заказом")
    @Description("ожидаем 200 и объект с заказом")
    public void SuccessGetOrderByTrackReturnOrderObj() throws InterruptedException {
        OrderData orderData = creater.createOrder();
        Response response = orderRequest.createOrder(orderData);
        String track = String.valueOf(orderRequest.getTrackNumberOfOrder(response));
        Response orderResponse = orderRequest.getOrderByTrack(track);
        //Проверка схемы ответа
        orderResponse.then().assertThat().statusCode(200).and()
                .body(matchesJsonSchemaInClasspath("order_by_track_schema.json"));
    }
}
