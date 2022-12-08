package createcouriertest;
import createcourier.Courier;
import createcourier.CourierRequest;
import createcourier.RandomDataForCourier;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
public class CreateCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    //Проверяем, что курьер успешно создается
    //Проверка тела ответа и статускода 201
    public void checkCreateCourierFirstTimeWithWrightData(){
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new Courier(randomData.generateLogin(),randomData.generatePassword(),randomData.generateFirstName());
        //Отправляем Пост на создание курьера
        CourierRequest courierRequest = new CourierRequest();
        Response response = courierRequest.createCourier(courier);
        //ПРоверяем тело ответа и статускод
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);
        //login
        int id = given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .path("id");
        //delete courier from DB
        given().log().all()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/"+ id);

    }
    @Test
    //При попытке создать одинаковых курьеров
    // Проверка статускода 409
    //Проверка тела ответа
    public void checkDoubleCreateCourierReturnConflict(){
        //String json = "{\"login\": \"igor\", \"password\": \"1234\", \"name\": \"igorek\"}";
        Courier courier = new Courier("bebebe","3333","ship");
        Response firstResponse =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        Response doubleResponse = given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
        doubleResponse.then()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }
    @Test
    //Попытка создать курьера без поля login
    //Проверяем тело и код 400
    public void checkCreateCourierWithoutLoginFailed(){

        File json = new File("src/test/resources/createcourierwithoutlogindata.json");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }
    @Test
    //Попытка создать курьера без поля password
    //Проверяем тело и код 400
    public void checkCreateCourierWithoutPasswordFailed(){

        File json = new File("src/test/resources/createcourierwithoutpassworddata.json");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }
    @Test
    //Попытка создать курьера без поля login, password
    //Проверяем тело и код 400
    public void checkCreateCourierWithoutLoginAndPasswordFailed(){

        File json = new File("src/test/resources/createcourierwithoutloginandpassworddata.json");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }
   /* @After
    public void cleanDataBase(){
        String json = "{\"login\": \"igor\", \"password\": \"1234\"}";
        given().header("Content-type", "application/json")
                .body()
    }*/
}
