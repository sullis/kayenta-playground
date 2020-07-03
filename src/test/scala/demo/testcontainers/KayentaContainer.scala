package demo.testcontainers

import org.testcontainers.containers.DockerComposeContainer
import java.io.{File => JFile}

import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.lifecycle.Startable

class KayentaContainer()
  extends Startable {

  private val dockerComposeFile = new JFile("src/test/resources/kayenta-docker-compose.yaml")
  private val container: DockerComposeContainer[_] = new DockerComposeContainer(dockerComposeFile)
  private val kayentaServiceName = "kayenta"
  private val kayentaServicePort = 8090

  container.withPull(true)
  container.withExposedService(
    kayentaServiceName,
    kayentaServicePort,
    Wait.forHttp("/metricsServices").forStatusCode(200))

  override def start: Unit = {
    container.start
  }

  override def stop: Unit = {
    container.stop
  }

  override def close: Unit = {
    container.close
  }

  def connectionUrl: String = {
    "http://" +
      container.getServiceHost(kayentaServiceName, kayentaServicePort) +
      ":" +
      container.getServicePort(kayentaServiceName, kayentaServicePort)
  }
  
  def swaggerSpecificationUrl: String = {
    connectionUrl + "/v2/api-docs"
  }

  def swaggerUiUrl: String = {
    connectionUrl + "/swagger-ui.html"
  }
}
