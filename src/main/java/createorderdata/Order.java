package createorderdata;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import specs.Specs;

import static io.restassured.RestAssured.given;
public class Order extends Specs {
    private final String uri = "http://qa-scooter.praktikum-services.ru";
    private final String orders = "/api/v1/orders";
    public Response orderRequest(OrderData order){
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(uri)
                .body(order)
                .when()
                .post(orders);
    }
    public Response getOrderWithoutCourier(){
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(uri)
                .when()
                .get(orders);
    }
    public int getTrackNumberOfOrder(OrderData order){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .extract()
                .path("track");
    }
    public int getTrackNumberOfOrder(Response response){
        return response
                .then()
                .extract()
                .path("track");
    }
    public void cancelOrder(int track){
        given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .body(track)
                .when()
                .put("/api/v1/orders/cancel");
    }
    public Response getOrderWithId(int courierId){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .when()
                .get("/api/v1/orders?courierId="+courierId);
    }
    public int getOrderNumberByTrack(int track) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("t", String.valueOf(track))
                .baseUri(uri)
                .when()
                .get("/api/v1/orders/track")
                .then()
                .extract()
                .path("order.id");
    }
    public Response getOrderAccessibleForCourier(){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .when()
                .get("/api/v1/orders?limit=10&page=0");
    }
    public Response getOrderAccessibleForCourierNearestStation(){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .when()
                .get("/api/v1/orders?limit=10&page=0&nearestStation=[\"2\"]");
    }
    public Response getOrderWithIdIncludeStations(int courierId){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .when()
                .get("/api/v1/orders?courierId="+courierId+"&nearestStation=[\"2\"]");
    }
    public void acceptOrder(int track, int courierId){
        given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .when()
                .put("/api/v1/orders/accept/"+track+"?courierId="+courierId);
    }
    public Response acceptOrderByCourier (String orderId, String courierId) throws InterruptedException {
        return baseCourierSpec()
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER+orderId);
    }
}
