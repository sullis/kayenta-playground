package demo.testcontainers

import org.testcontainers.containers.DockerComposeContainer
import java.io.{File => JFile}

class KayentaContainer() {
  private val dockerComposeFile = new JFile("src/test/resources/kayenta-docker-compose.yaml")
  private val container: DockerComposeContainer[_] = new DockerComposeContainer(dockerComposeFile)
  private val kayentaServiceName = "kayenta"
  private val kayentaServicePort = 8090

  container.withPull(true)
  container.withExposedService(kayentaServiceName, kayentaServicePort)

  def start(): Unit = {
    container.start()
  }

  def stop(): Unit = {
    container.stop()
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
