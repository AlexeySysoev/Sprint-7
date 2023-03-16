package createcourier;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class CourierRequest {
    private final String uri = "http://qa-scooter.praktikum-services.ru";
    private final String newCourierApi = "/api/v1/courier";
    private final String loginCourier = "/api/v1/courier/login";
    //создание курьера в БД
    public Response createCourier(Courier courier){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .body(courier)
                .when()
                .post(newCourierApi);
    }
    //получить id курьера
    public int getCourierId(Courier courier){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .body(courier)
                .when()
                .post(loginCourier)
                .then()
                .extract()
                .path("id");
    }
    //логин курьера в системе
    public Response loginCourier(Courier courier){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .body(courier)
                .when()
                .post(loginCourier);
    }
    //удаление курьера из БД
    public void deleteCourier(int id){
        given().log().all()
                .header("Content-type", "application/json")
                .baseUri(uri)
                .when()
                .delete(newCourierApi+ id);
    }
    public void deleteWrongCourier(Response response, Courier courier){
        CourierRequest courierRequest = new CourierRequest();
        if(response.then().extract().statusCode() ==200 || response.then().extract().statusCode()==201){
            courierRequest.deleteCourier(courierRequest.getCourierId(courier));
        }
    }
    public Response deleteCourier(Response response) {
            String id = "/" + response.then().extract().path("id").toString();
            return given().log().all()
                    .contentType(ContentType.JSON)
                    .baseUri(uri)
                    .when()
                    .delete(newCourierApi + id);
    }
}
