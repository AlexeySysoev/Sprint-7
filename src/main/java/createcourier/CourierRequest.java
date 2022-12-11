package createcourier;
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
}
