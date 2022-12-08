package createordertest;
import createorderdata.CreateOrderData;
import createorderdata.Order;
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
    //Проверяем создание заказа
    public void checkCreateOrderWithBlackColorSuccess() {
        CreateOrderData orderData = new CreateOrderData("Mike",
                                                    "Jonson",
                                                    "Sovetskya, 32, kv.34",
                                                    "baltiyskaya",
                                                    "89997776655",
                                                    7,
                                                    "2022-12-26",
                                                    "",
                                                    color);
        Order sendOrder = new Order(); //экземпляр класса для работы с АПИ
        //Отправляем заказ и проверяем наличие значения в "track" и статус код
        sendOrder.orderRequest(orderData).then().assertThat().body("track",notNullValue()).and().statusCode(201);
    }
}
