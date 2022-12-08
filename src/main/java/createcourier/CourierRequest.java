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
    //делаем логин курьера и получаем id в ответе
    public int loginCourier(Courier courier){
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
    //удаление курьера из БД
    public void deleteCourier(int id){
        given().log().all()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/"+ id);
    }
}
