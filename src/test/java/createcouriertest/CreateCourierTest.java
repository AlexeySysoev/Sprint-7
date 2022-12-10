package createcouriertest;
import createcourier.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
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
                new CourierAllFields(randomData.generateLogin(),randomData.generatePassword(),randomData.generateFirstName());
        CourierRequest courierRequest = new CourierRequest(); //экземпляр класс для работы с АПИ курьера
        //Отправляем Пост на создание курьера
        Response response = courierRequest.createCourier(courier);
        //Проверяем тело ответа и статускод
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);
        //получаем id курьера
        //удаляем курьера из базы данных
        courierRequest.deleteCourier(courierRequest.getCourierId(courier));
    }
    @Test
    //Проверка невозможности создать одинаковых курьеров
    //Проверка статускода 409
    //Проверка тела ответа
    public void checkDoubleCreateCourierReturnConflict(){
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierAllFields(randomData.generateLogin(),randomData.generatePassword(),randomData.generateFirstName());
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
    }
    @Test
    //Попытка создать курьера без поля login
    //Проверяем тело и код 400
    public void checkCreateCourierWithoutLoginFailed(){
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierWithoutLogin(randomData.generatePassword(),randomData.generateFirstName());
        CourierRequest courierRequest = new CourierRequest();//экземпляр класс для работы с АПИ курьера
        Response response = courierRequest.createCourier(courier);//отправляем запрос
        //проверяем тело ответа и статускод
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }
    @Test
    //Попытка создать курьера без поля password
    //Проверяем тело и код 400
    public void checkCreateCourierWithoutPasswordFailed(){
            RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
            Courier courier = //создаем курьера с рандомными данными
                    new CourierWithoutPassword(randomData.generateLogin(),randomData.generateFirstName());
            CourierRequest courierRequest = new CourierRequest();//экземпляр класс для работы с АПИ курьера
            Response response = courierRequest.createCourier(courier);//отправляем запрос
            //проверяем тело ответа и статускод
            response.then().assertThat()
                    .body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
        }

    @Test
    //Попытка создать курьера без поля login, password
    //Проверяем тело и код 400
    public void checkCreateCourierWithoutLoginAndPasswordFailed(){
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierWithOnlyFirstName(randomData.generateFirstName());
        CourierRequest courierRequest = new CourierRequest();//экземпляр класс для работы с АПИ курьера
        Response response = courierRequest.createCourier(courier);//отправляем запрос
        //проверяем тело ответа и статускод
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    //Попытка создать курьера без поля firstName
    //Проверяем тело и код 400
    public void checkCreateCourierWithoutFirstNameFailed(){
        RandomDataForCourier randomData  = new RandomDataForCourier(); //экземпляр класса для создания данных
        Courier courier = //создаем курьера с рандомными данными
                new CourierWithoutFirstName(randomData.generateLogin(),randomData.generatePassword());
        CourierRequest courierRequest = new CourierRequest();//экземпляр класс для работы с АПИ курьера
        Response response = courierRequest.createCourier(courier);//отправляем запрос
        //проверяем тело ответа и статускод
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }
}
