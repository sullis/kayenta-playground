package demo.testcontainers

import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.containsString

class KayentaContainerSpec
  extends AnyFlatSpec
  with BeforeAndAfterAll
  with Matchers {

  var kayenta: KayentaContainer = _

  override def beforeAll() {
    kayenta = new KayentaContainer()
    kayenta.start()
  }

  override def afterAll() {
    kayenta.stop()
  }

  "connectionUrl" should "be valid" in {
    kayenta.connectionUrl should startWith ("http://localhost")
  }

  "swagger UI" should "exist" in {
    RestAssured.given()
      .get(kayenta.swaggerUiUrl)
      .prettyPeek()
      .then()
      .assertThat()
      .statusCode(200)
      .contentType("text/html")
      .body(containsString("<title>Swagger UI</title>"))
  }

  "swagger 2 specification" should "exist" in {
    RestAssured.given()
      .get(kayenta.swaggerSpecificationUrl)
      .prettyPeek()
      .then()
      .assertThat()
      .statusCode(200)
      .contentType("application/json")
      .body(containsString("Kayenta API"))
    
  }
}
