package demo.testcontainers;

import java.net.URL;
import java.io.File;
import java.nio.file.Paths;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.lifecycle.Startable;

public class KayentaContainer implements Startable {

  private static File dockerComposeFile = resolveFile("kayenta-docker-compose.yaml");
  private DockerComposeContainer container = new DockerComposeContainer(dockerComposeFile);
  private final String kayentaServiceName = "kayenta";
  private final int kayentaServicePort = 8090;

  public KayentaContainer() {
    container.withPull(true);
      container.withExposedService(
      kayentaServiceName,
      kayentaServicePort,
         Wait.forHttp("/metricsServices").
         forStatusCode(200));
  }

  public void start() {
    container.start();
  }

  public void stop() {
    container.stop();
  }

  public void close() {
    container.close();
  }

  public String connectionUrl() {
    return "http://" +
      container.getServiceHost(kayentaServiceName, kayentaServicePort) +
      ":" +
      container.getServicePort(kayentaServiceName, kayentaServicePort);
  }
  
  public String swaggerSpecificationUrl() {
    return connectionUrl() + "/v2/api-docs";
  }

  public String swaggerUiUrl() {
    return connectionUrl() + "/swagger-ui.html";
  }

  static java.io.File resolveFile(String resourceName) {
    URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
    if (url == null) {
      throw new IllegalStateException("Resource not found. resourceName: " + resourceName);
    }
    try {
      return Paths.get(url.toURI()).toFile();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}