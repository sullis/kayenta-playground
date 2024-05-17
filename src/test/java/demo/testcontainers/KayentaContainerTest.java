package demo.testcontainers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;


public class KayentaContainerTest {

  private static KayentaContainer kayenta;

  @BeforeAll
  static void beforeAll() {
    kayenta = new KayentaContainer();
    kayenta.start();
  }

  @AfterAll
  static void afterAll() {
    kayenta.stop();
  }

  @Test
  public void connectionUrlShouldBeValid() {
    assertThat(kayenta.connectionUrl()).startsWith("http://localhost");
  }

  @Test
  public void swaggerUiShouldExist() {
    given()
      .request()
      .get(kayenta.swaggerUiUrl())
    .then()
      .assertThat()
      .statusCode(200)
      .contentType("text/html")
      .body(containsString("<title>Swagger UI</title>"));
  }

 @Test
 public void swagger2SpecShouldExist() {
    given()
      .request()
      .get(kayenta.swaggerSpecificationUrl())
    .then()
      .assertThat()
      .statusCode(200)
      .contentType("application/json")
      .body(containsString("Kayenta API"));
  }

  @Test
  public void canaryControllerClientShouldConnectToServer() {
    /* todo
    CanaryControllerClient client = new CanaryControllerClient(kayenta.connectionUrl());

    val futureValue = client.initiateCanaryWithConfigUsingPost(application = Some("test-application"))
      .value
      .futureValue

    System.out.println(futureValue)
    */

    /*
    response.fold(
      handleOk,
      handleCreated,
      handleUnauthorized,
      handleForbidden,
      handleNotFound)
      */
  }

}
