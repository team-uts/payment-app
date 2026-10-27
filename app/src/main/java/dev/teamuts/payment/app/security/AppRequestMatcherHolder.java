package dev.teamuts.payment.app.security;

import java.util.Arrays;
import java.util.List;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.security.autoconfigure.web.StaticResourceLocation;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class AppRequestMatcherHolder {
  private final AppRequestMatchers actuatorEndpoints;
  private final AppRequestMatchers swaggerUrls;

  public AppRequestMatcherHolder(
      WebEndpointProperties actuatorProperties,
      SwaggerUiConfigProperties swaggerUiProperties,
      SpringDocConfigProperties springDocProperties) {
    System.out.println("swaggerUiProperties.getPath(): " + swaggerUiProperties.getPath());
    this.actuatorEndpoints =
        AppRequestMatchers.of(
            AppRequestMatcher.noneRoleOf(
                "%s/**".formatted(actuatorProperties.getBasePath()), HttpMethod.GET));
    this.swaggerUrls =
        AppRequestMatchers.of(
            AppRequestMatcher.noneRoleOf(swaggerUiProperties.getPath(), HttpMethod.GET),
            AppRequestMatcher.noneRoleOf("/swagger-ui/**", HttpMethod.GET),
            AppRequestMatcher.noneRoleOf(
                "%s/**".formatted(springDocProperties.getApiDocs().getPath()), HttpMethod.GET));
  }

  private static final List<AppRequestMatcher> STATIC_RESOURCE_URL_LIST =
      Arrays.stream(StaticResourceLocation.values())
          .flatMap(StaticResourceLocation::getPatterns)
          .map(urlPattern -> AppRequestMatcher.noneRoleOf(urlPattern, HttpMethod.GET))
          .toList();

  public AppRequestMatchers getShouldNotLoggingFilterMatchers() {
    return actuatorEndpoints.merge(swaggerUrls);
  }
}
