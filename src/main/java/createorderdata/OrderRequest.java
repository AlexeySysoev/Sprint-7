package createorderdata;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import orderbytrackdeserializator.Order;
import orderbytrackdeserializator.OrderByTrack;
import specs.Specs;

import static io.restassured.RestAssured.given;
public class OrderRequest extends Specs {
    private Integer track = null;
    public Response createOrder(OrderData order) throws InterruptedException {
        return baseSpec()
                .body(order)
                .when()
                .post(ORDERS);
    }
    public Response getOrderWithoutCourier() throws InterruptedException {
        return baseSpec()
                .when()
                .get(ORDERS);
    }
    public Response getOrderByTrack(String track) throws InterruptedException {
        return baseSpecForQuery()
                .queryParam("t", track)
                .baseUri(URI)
                .get(ORDER_BY_TRACK);
    }
    public OrderByTrack getOrderObjByTrack(String track) throws InterruptedException {
        return baseSpecForQuery()
                .queryParam("t", track)
                .baseUri(URI)
                .get(ORDER_BY_TRACK)
                .as(OrderByTrack.class);
    }
    public int getTrackNumberOfOrder(OrderData order) throws InterruptedException {
        return baseSpec()
                .body(order)
                .when()
                .post(ORDERS)
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
    public void cancelOrder(int track) throws InterruptedException {
                baseSpec()
                .body(track)
                .when()
                .put(CANCEL_ORDER);
    }
    public Response getOrderWithId(int courierId) throws InterruptedException {
        return baseSpecForQuery()
                .queryParam("courierId", courierId)
                .baseUri(URI)
                .when()
                .get(ORDERS);
    }
    public int getOrderNumberByTrack(int track) throws InterruptedException {
        return baseSpecForQuery()
                .queryParam("t", String.valueOf(track))
                .baseUri(URI)
                .when()
                .get(ORDER_BY_TRACK)
                .then()
                .extract()
                .path("order.id");
    }
    public Response getOrderAccessibleForCourier(){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(URI)
                .when()
                .get("/api/v1/orders?limit=10&page=0");
    }
    public Response getOrderAccessibleForCourierNearestStation(){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(URI)
                .when()
                .get("/api/v1/orders?limit=10&page=0&nearestStation=[\"2\"]");
    }
    public Response getOrderWithIdIncludeStations(int courierId){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(URI)
                .when()
                .get("/api/v1/orders?courierId="+courierId+"&nearestStation=[\"2\"]");
    }
    public void acceptOrder(int track, int courierId){
        given().log().all()
                .header("Content-type", "application/json")
                .baseUri(URI)
                .when()
                .put("/api/v1/orders/accept/"+track+"?courierId="+courierId);
    }
    public Response acceptOrderByCourier (String orderId, String courierId) throws InterruptedException {
        return baseSpec()
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER + orderId);
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }
}
