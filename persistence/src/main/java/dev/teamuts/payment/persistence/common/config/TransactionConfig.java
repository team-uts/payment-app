package dev.teamuts.payment.persistence.common.config;

import static dev.teamuts.payment.persistence.common.constant.JpaConstants.APP_ENTITY_MANAGER;
import static dev.teamuts.payment.shared.data.TransactionConstants.APP_TRANSACTION_MANAGER;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
public class TransactionConfig {

  @Primary
  @Bean(APP_TRANSACTION_MANAGER)
  PlatformTransactionManager appTransactionManager(
      @Qualifier(APP_ENTITY_MANAGER) LocalContainerEntityManagerFactoryBean appEntityManager) {
    return new JpaTransactionManager() {
      {
        this.setEntityManagerFactory(appEntityManager.getObject());
      }
    };
  }
}
