package dev.teamuts.payment.shared.config;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum AppModule {
  DEFAULT(""),
  DOMAIN("domain"),
  INFRA("infra"),
  PERSISTENCE("persistence");

  private final String moduleName;

  AppModule(String moduleName) {
    this.moduleName = moduleName;
  }

  private static final String APPLICATION_MODULE_PREFIX = "application-";

  public static List<AppModule> getApplicationModules() {
    return List.of(DEFAULT, DOMAIN, INFRA, PERSISTENCE);
  }

  public static String getApplicationModuleNames() {
    return getApplicationModules().stream()
        .map(AppModule::getModuleName)
        .map(name -> APPLICATION_MODULE_PREFIX + name)
        .collect(Collectors.joining(","));
  }

}
