package createcourier;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierRequest {

    public Response createCourier(Courier courier){
        Response response;
        return response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
    }
}
