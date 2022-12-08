package createorderdata;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Order {
    public Response orderRequest(CreateOrderData order){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri("http://qa-scooter.praktikum-services.ru")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }
}
