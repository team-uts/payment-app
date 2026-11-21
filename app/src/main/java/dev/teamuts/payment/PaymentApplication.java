package dev.teamuts.payment;

import dev.teamuts.shared.config.AppModule;
import dev.teamuts.shared.utils.AppTimeUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PaymentApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(PaymentApplication.class)
        .properties("spring.config.name=%s".formatted(AppModule.getApplicationModuleNames()))
        .run(args);
  }

  @PostConstruct
  public void init() {
    AppTimeUtils.setDefaultTimezone();
  }
}
