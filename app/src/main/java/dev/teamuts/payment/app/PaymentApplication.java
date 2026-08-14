package dev.teamuts.payment.app;

import dev.teamuts.payment.shared.config.AppModule;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PaymentApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(PaymentApplication.class)
        .properties(
            "spring.config.name=%s".formatted(AppModule.getApplicationModuleNames())
        )
        .run(args);
  }

}
