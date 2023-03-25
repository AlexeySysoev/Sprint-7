package specs;

import endpoints.EndPoints;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Specs extends EndPoints{
    private final int DELAY = 500;
    public RequestSpecification baseCourierSpec() throws InterruptedException {
        Thread.sleep(DELAY);
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(URI);
    }
}
