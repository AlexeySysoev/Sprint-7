import createorderdata.CreateOrderData;
import createorderdata.Order;
import createorderdata.RandomDataForOrder;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
@Parameterized.Parameter()
public List<String> color;
@Parameterized.Parameters
public static Object[][] params(){
    return new Object[][]{
            {Arrays.asList()},
            {Arrays.asList("BLACK")},
            {Arrays.asList("GREY")},
            {Arrays.asList("BLACK", "GREY")}
    };
}
    @Test
    @DisplayName("Создание заказа с различными вариациями цвета самоката")
    @Description("Проверяем наличие в ответе не пустого track и статус код 201")
    public void checkCreateOrderColorField() {
        RandomDataForOrder data = new RandomDataForOrder(); //экземпляр класса для создания данных запроса
        CreateOrderData orderData = new CreateOrderData(data.generateName(), data.generateName(), data.generateAddress(),
                data.generateMetroStation(), data.generatePhoneNumber(), data.generateRentTime(),
                data.getDeliveryDate(), data.generateComment(),
                                                    color);
        Order sendOrder = new Order(); //экземпляр класса для работы с АПИ
        //Отправляем заказ и проверяем наличие значения в "track" и статус код
        sendOrder.orderRequest(orderData).then().assertThat().body("track",notNullValue()).and().statusCode(201);
        //Получаем трек номер заказа
        int track  = sendOrder.getTrackNumberOfOrder(orderData);
        //Отмена заказа
        sendOrder.cancelOrder(track);
    }
}
