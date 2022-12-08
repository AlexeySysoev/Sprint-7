package logincouriertest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    //Курьер может авторизоваться /api/v1/courier/login
    //получаем id при успешной авторизации
    public void checkLoginCourierWithRequiredFieldsPassed() {
        String json = "{\"login\": \"igor\", \"password\": \"1234\"}";
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue())
                .and().statusCode(200);
    }
    @Test
    //Попытка логина без поля login
    //Проверяем тело ответа и статускод 400
    public void checkLoginCourierWithoutLoginFieldReturnBadRequest() {
        String json = "{\"password\": \"1234\"}";
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message",equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
    }
    @Test
    //Попытка логина без поля login
    //Проверяем тело ответа и статускод 400
    public void checkLoginCourierWithoutPasswordFieldReturnBadRequest() {
        String json = "{\"login\": \"igor\"}";
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message",equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
    }
    @Test
    //Попытка авторизации с неверным(несуществующим) login
    //Проверяем тело ответа и статускод 404
    public void checkLoginCourierWithInvalidLoginFieldReturnNotFound() {
        String json = "{\"login\": \"igortata\", \"password\": \"1234\"}";
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message",equalTo("Учетная запись не найдена"))
                .and().statusCode(404);
    }
    @Test
    //Попытка авторизации с неверным(несуществующим) password
    //Проверяем тело ответа и статускод 404
    public void checkLoginCourierWithInvalidPasswordFieldReturnNotFound() {
        String json = "{\"login\": \"igor\", \"password\": \"1212\"}";
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message",equalTo("Учетная запись не найдена"))
                .and().statusCode(404);
    }
}
