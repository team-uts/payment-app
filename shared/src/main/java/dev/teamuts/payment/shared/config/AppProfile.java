package dev.teamuts.payment.shared.config;

import java.util.Arrays;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProfile implements EnvironmentAware {
  private String[] profiles;

  public static final String LOCAL = "local";
  public static final String ALPHA = "alpha";

  @Override
  public void setEnvironment(Environment environment) {
    var activeProfiles = environment.getActiveProfiles();
    var defaultProfiles = environment.getDefaultProfiles();

    this.profiles = activeProfiles.length > 0 ? activeProfiles : defaultProfiles;
  }

  public boolean isLocal() {
    return hasProfile(LOCAL);
  }

  public boolean isAlpha() {
    return hasProfile(ALPHA);
  }

  private boolean hasProfile(String profile) {
    return Arrays.binarySearch(profiles, profile) >= 0;
  }
}
