package specs;

import endpoints.EndPoints;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Specs extends EndPoints{
    private final int DELAY = 500;
    public RequestSpecification baseSpec() throws InterruptedException {
        Thread.sleep(DELAY);
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(URI);
    }
    public RequestSpecification baseSpecForQuery() throws InterruptedException {
        Thread.sleep(DELAY);
        return given().log().all()
                .contentType(ContentType.JSON);
    }
}
