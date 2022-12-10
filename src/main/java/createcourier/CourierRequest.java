package createcourier;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class CourierRequest {
    //создание курьера в БД
    public Response createCourier(Courier courier){
        Response response;
        return response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
    }
    //получить id курьера
    public int getCourierId(Courier courier){
        int id;
        return id = given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .path("id");
    }
    //логин курьера в системе
    public Response loginCourier(Courier courier){
        return   given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }
    //удаление курьера из БД
    public void deleteCourier(int id){
        given().log().all()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/"+ id);
    }
}
