package createordertest;

import createorderdata.CreateOrderData;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    @Parameterized.Parameter()
    public String colorBlack;
    @Parameterized.Parameter(1)
    public String colorGrey;
    @Parameterized.Parameter(2)
    public int statusCode;
    @Parameterized.Parameters(name = "colorBlack: {0}, colorGrey: {1}, statusCode: {2}")
    public static  Object[][] params(){
        return new Object[][]{
                {"", "",          201},
                {"BLACK", "",     201},
                {"", "GREY",      201},
                {"BLACK", "GREY", 201}
        };
    }
    @Test
    //Проверяем создание заказа
    public void checkCreateOrderWithBlackColorSuccess() {
        String[] color = {colorBlack, colorGrey};
        CreateOrderData order = new CreateOrderData("Mike",
                                                    "Jonson",
                                                    "Sovetskya, 32, kv.34",
                                                    "baltiyskaya",
                                                    "89997776655",
                                                    7,
                                                    "2022-12-26",
                                                    "",
                                                    color);
        Response response = given().log().all()
                            .header("Content-type", "application/json")
                            .baseUri("http://qa-scooter.praktikum-services.ru")
                            .body(order)
                            .when()
                            .post("/api/v1/orders");
        response.then().assertThat().body("track",notNullValue()).and().statusCode(statusCode);

    }
}
