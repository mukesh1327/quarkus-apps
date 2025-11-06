package in.cloudxplorer13;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class HelloWorldTest {

    @Test
    public void testRootEndpoint() {
        given()
          .when().get("/")
          .then()
             .statusCode(200)
             .header("Content-Type", containsString("text/html"))
             .body(containsString("<h1>"))
             .body(containsString("<h2>App modernization</h2>"))
             .body(containsString("<img"))
             .body(containsString("alt='Image'"));
    }
}
