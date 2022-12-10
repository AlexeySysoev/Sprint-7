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
    public int getTrackNumberOfOrder(CreateOrderData order){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri("http://qa-scooter.praktikum-services.ru")
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .extract()
                .path("track");
    }
    public void cancelOrder(int track){
        given().log().all()
                .header("Content-type", "application/json")
                .baseUri("http://qa-scooter.praktikum-services.ru")
                .body(track)
                .when()
                .put("/api/v1/orders/cancel");
    }
    public Response getOrderWithId(int courierId){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri("http://qa-scooter.praktikum-services.ru")
                .when()
                .get("/api/v1/orders?courierId="+courierId);
    }
    public void acceptOrder(int track, int courierId){
        given().log().all()
                .header("Content-type", "application/json")
                .baseUri("http://qa-scooter.praktikum-services.ru")
                .when()
                .put("/api/v1/orders/accept/"+track+"?courierId="+courierId);
    }
}
