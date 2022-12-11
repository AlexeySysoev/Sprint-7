package createorderdata;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class Order {
    private final String uri = "http://qa-scooter.praktikum-services.ru";
    public Response orderRequest(CreateOrderData order){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .body(order)
                .when()
                .post("/api/v1/orders");
    }
    public int getTrackNumberOfOrder(CreateOrderData order){
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
}
