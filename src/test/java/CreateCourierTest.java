import createcourier.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
public class CreateCourierTest {
    @Test
    @DisplayName("Корректное создание курьера")
    @Description("Проверяем тело ответа и статускод 201")
    public void checkCreateCourierWithWrightData() throws InterruptedException {
        RandomDataForCourier randomData = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierV1(randomData.generateLogin(), randomData.generatePassword(), randomData.generateFirstName());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Отправляем Пост на создание курьера
        Response response = courierRequest.createCourier(courier);
        //Проверяем тело ответа и статускод
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);
        //удаляем курьера из базы данных
        courierRequest.deleteCourier(courierRequest.getCourierId(courier));
    }
    @Test
    @DisplayName("Попытка создания уже существующего курьера")
    @Description("Проверяем тело ответа на наличие \"Этот логин уже используется. Попробуйте другой.\" и статус код 409")
    public void checkDoubleCreateCourierReturnConflict() throws InterruptedException {
        RandomDataForCourier randomData = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierV1(randomData.generateLogin(), randomData.generatePassword(), randomData.generateFirstName());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Отправляем Пост на создание курьера в первый раз
        courierRequest.createCourier(courier);
        //Делаем попытку создать курьера повторно с теми же данными  сохраняем ответ
        Response response = courierRequest.createCourier(courier);
        //Проверяем тело и статускод ответа
        response.then()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
        //Удаление курьера в случае его создания
        courierRequest.deleteWrongCourier(response,courier);
    }
    @Test
    //Попытка создать курьера без поля login
    //Проверяем тело и код 400
    @DisplayName("Попытка создать курьера без поля login")
    @Description("Проверяем тело ответа на наличие \"Недостаточно данных для создания учетной записи\" и статус код 400")
    public void checkCreateCourierWithoutLoginFailed() throws InterruptedException {
        RandomDataForCourier randomData = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierV3(randomData.generatePassword(), randomData.generateFirstName());
        CourierRequest courierRequest = new CourierRequest();//экземпляр класс для работы с АПИ курьера
        Response response = courierRequest.createCourier(courier);//отправляем запрос
        //проверяем тело ответа и статускод
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
        //Удаление курьера в случае его создания
        courierRequest.deleteWrongCourier(response,courier);
    }
    @Test
    @DisplayName("Попытка создать курьера без поля password")
    @Description("Проверяем тело ответа на наличие \"Недостаточно данных для создания учетной записи\" и статус код 400")
    public void checkCreateCourierWithoutPasswordFailed() throws InterruptedException {
        RandomDataForCourier randomData = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierV2(randomData.generateLogin(), randomData.generateFirstName());
        CourierRequest courierRequest = new CourierRequest();//экземпляр класс для работы с АПИ курьера
        Response response = courierRequest.createCourier(courier);//отправляем запрос
        //проверяем тело ответа и статускод
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
        //Удаление курьера в случае его создания
        courierRequest.deleteWrongCourier(response,courier);
    }
    @Test
    @DisplayName("Попытка создать курьера без полей login,password")
    @Description("Проверяем тело ответа на наличие \"Недостаточно данных для создания учетной записи\" и статус код 400")
    public void checkCreateCourierWithoutLoginAndPasswordFailed() throws InterruptedException {
        RandomDataForCourier randomData = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierV2(randomData.generateFirstName());
        CourierRequest courierRequest = new CourierRequest();//экземпляр класс для работы с АПИ курьера
        Response response = courierRequest.createCourier(courier);//отправляем запрос
        //проверяем тело ответа и статускод
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
        //Удаление курьера в случае его создания
        courierRequest.deleteWrongCourier(response,courier);
    }
}
