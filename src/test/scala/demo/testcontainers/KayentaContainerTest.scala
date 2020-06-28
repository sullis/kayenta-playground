package demo.testcontainers

import org.scalatest.{BeforeAndAfterAll, EitherValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import demo.clients.definitions._
import demo.clients.AkkaHttpImplicits._
import demo.clients.Implicits._
import cats.implicits._
import org.scalatest.time.SpanSugar._
import scala.concurrent.Future
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext.Implicits.global
import demo.clients.`canary-controller`.CanaryControllerClient
import org.scalatest.concurrent.ScalaFutures

class KayentaContainerSpec
  extends AnyFlatSpec
  with BeforeAndAfterAll
  with Matchers
  with ScalaFutures
  with EitherValues {

  override implicit val patienceConfig = PatienceConfig(10 seconds, 1 second)

  private var kayenta: KayentaContainer = _

  implicit val actorMat = ActorMaterializer
  implicit val actorSys = ActorSystem()

  implicit val singleRequestHttpClient: HttpRequest => Future[HttpResponse] = {
    (req: HttpRequest) => Http()(actorSys).singleRequest(req)
  }

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
    given()
      .request()
      .get(kayenta.swaggerUiUrl)
    .then()
      .assertThat()
      .statusCode(200)
      .contentType("text/html")
      .body(containsString("<title>Swagger UI</title>"))
  }

  "swagger 2 specification" should "exist" in {
    given()
      .request()
      .get(kayenta.swaggerSpecificationUrl)
    .then()
      .assertThat()
      .statusCode(200)
      .contentType("application/json")
      .body(containsString("Kayenta API"))
  }

  "CanaryControllerClient" should "connect to server" in {
    val client = new CanaryControllerClient(kayenta.connectionUrl)

    val futureValue = client.initiateCanaryWithConfigUsingPost(application = Some("test-application"))
      .value
      .futureValue

    System.out.println(futureValue)

    /*
    response.fold(
      handleOk,
      handleCreated,
      handleUnauthorized,
      handleForbidden,
      handleNotFound)
      */
  }

  def handleOk(canaryExecutionResponse: CanaryExecutionResponse): Unit = {
    canaryExecutionResponse.canaryExecutionId shouldBe empty
  }

  def handleCreated(): Unit = { fail("whatever") }
  def handleUnauthorized(): Unit = { fail("whatever") }
  def handleForbidden(): Unit = { fail("whatever") }
  def handleNotFound(): Unit = { fail("whatever") }

}
