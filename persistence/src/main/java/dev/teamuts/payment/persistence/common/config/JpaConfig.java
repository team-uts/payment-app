package dev.teamuts.payment.persistence.common.config;

import static dev.teamuts.payment.persistence.common.constant.DataSourceConstants.APPLICATION_DATA_SOURCE;
import static dev.teamuts.payment.persistence.common.constant.JpaConstants.APP_ENTITY_MANAGER;
import static dev.teamuts.payment.shared.data.TransactionConstants.APP_TRANSACTION_MANAGER;

import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.SchemaToolingSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.hibernate.autoconfigure.HibernateProperties;
import org.springframework.boot.jpa.autoconfigure.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = "dev.teamuts.payment.**.repository",
    entityManagerFactoryRef = APP_ENTITY_MANAGER,
    transactionManagerRef = APP_TRANSACTION_MANAGER)
@RequiredArgsConstructor
public class JpaConfig {

  private final JpaProperties jpaProperties;
  private final HibernateProperties hibernateProperties;

  @Primary
  @Bean(APP_ENTITY_MANAGER)
  public LocalContainerEntityManagerFactoryBean appEntityManager(
      @Qualifier(APPLICATION_DATA_SOURCE) DataSource applicationDataSource) {
    Properties mergedJpaProperties =
        new Properties() {
          {
            this.putAll(jpaProperties.getProperties());
            this.setProperty(SchemaToolingSettings.HBM2DDL_AUTO, hibernateProperties.getDdlAuto());
          }
        };

    return new LocalContainerEntityManagerFactoryBean() {
      {
        this.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        this.setJpaProperties(mergedJpaProperties);
        this.setDataSource(applicationDataSource);
        this.setPersistenceUnitName("paymentApp");
        this.setPackagesToScan("dev.teamuts.payment.persistence.db.*.entity");
      }
    };
  }
}
