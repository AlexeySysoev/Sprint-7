package createcourier;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import specs.Specs;

import static io.restassured.RestAssured.given;
public class CourierRequest extends Specs {
    private Integer id = null;
    public Integer getId() {
        return id;
    }

    public void setId(Integer courierId) {
        if (courierId != null) {
            this.id = courierId;
        }
    }


    //создание курьера в БД
    public Response createCourier(Courier courier) throws InterruptedException {
        return baseSpec()
                .body(courier)
                .when()
                .post(NEW_COURIER_API);
    }
    //получить id курьера
    public int getCourierId(Courier courier) throws InterruptedException {
        return baseSpec()
                .body(courier)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .extract()
                .path("id");
    }
    public Integer getCourierId(Response response) {
        Integer id = null;
        try { id = response.then().extract().path("id");}
        catch (Exception e){
            throw new NullPointerException();
        }
        return id;
    }
    //логин курьера в системе
    public Response loginCourier(Courier courier){
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(URI)
                .body(courier)
                .when()
                .post(LOGIN_COURIER);
    }
    //удаление курьера из БД
    public void deleteCourier(Integer id){
        if (id != null) {
            given().log().all()
                    .contentType(ContentType.JSON)
                    .baseUri(URI)
                    .when()
                    .delete(NEW_COURIER_API + "/" + id);
        }
    }
    public Response deleteCourier(String id){
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(URI)
                .when()
                .delete(NEW_COURIER_API + "/" + id);
    }
    public void deleteWrongCourier(Response response, Courier courier) throws InterruptedException {
        CourierRequest courierRequest = new CourierRequest();
        if(response.then().extract().statusCode() ==200 || response.then().extract().statusCode()==201){
            courierRequest.deleteCourier(courierRequest.getCourierId(courier));
        }
    }
    public int extractCourierId(Response response) {
           return response.then().extract().path("id");
    }
    public Response deleteCourier(Response response) {
            if(response.then().extract().statusCode() ==200 || response.then().extract().statusCode()==201){
            String id = "/" + response.then().extract().path("id").toString();
            return given().log().all()
                    .contentType(ContentType.JSON)
                    .baseUri(URI)
                    .when()
                    .delete(NEW_COURIER_API + id);
            }
            else return response;
    }
}
