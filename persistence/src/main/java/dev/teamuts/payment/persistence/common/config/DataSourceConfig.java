package dev.teamuts.payment.persistence.common.config;

import static dev.teamuts.payment.persistence.common.constant.DataSourceConstants.APPLICATION_DATA_SOURCE;
import static dev.teamuts.payment.persistence.common.constant.DataSourceConstants.REPLICA_DATA_SOURCE;
import static dev.teamuts.payment.persistence.common.constant.DataSourceConstants.ROUTING_DATA_SOURCE;
import static dev.teamuts.payment.persistence.common.constant.DataSourceConstants.SOURCE_DATA_SOURCE;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.teamuts.payment.persistence.common.config.properties.CustomDataSourceProperties;
import dev.teamuts.payment.persistence.common.exception.DatabaseException;
import java.util.Arrays;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CustomDataSourceProperties.class)
@RequiredArgsConstructor
public class DataSourceConfig {

  private final CustomDataSourceProperties dataSourceProperties;

  @Primary
  @Bean(APPLICATION_DATA_SOURCE)
  public DataSource applicationDataSource(
      @Qualifier(ROUTING_DATA_SOURCE) DataSource routingDataSource) {
    return new LazyConnectionDataSourceProxy(routingDataSource);
  }

  @Bean(ROUTING_DATA_SOURCE)
  public DataSource routingDataSource(
      @Qualifier(SOURCE_DATA_SOURCE) DataSource sourceDataSource,
      @Qualifier(REPLICA_DATA_SOURCE) DataSource replicaDataSource) {
    Map<Object, Object> dataSourceMap =
        Map.of(
            DbType.SOURCE, sourceDataSource,
            DbType.REPLICA, replicaDataSource);

    return new RoutingDataSource() {
      {
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(sourceDataSource);
      }
    };
  }

  @Bean(SOURCE_DATA_SOURCE)
  public DataSource sourceDataSource() {
    HikariConfig sourceConfig = new HikariConfig(dataSourceProperties.getSourceWithCommon());

    return new HikariDataSource(sourceConfig);
  }

  @Bean(REPLICA_DATA_SOURCE)
  public DataSource replicaDataSource() {
    HikariConfig replicaConfig = new HikariConfig(dataSourceProperties.getReplicaWithCommon());

    return new HikariDataSource(replicaConfig);
  }

  private static class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
      DbType dbType =
          DbType.valueOfReadOnly(TransactionSynchronizationManager.isCurrentTransactionReadOnly());

      log.info("Routing DataSource: {}", dbType);

      return dbType;
    }
  }

  private enum DbType {
    SOURCE(false),
    REPLICA(true);

    private final Boolean readOnly;

    DbType(Boolean readOnly) {
      this.readOnly = readOnly;
    }

    static DbType valueOfReadOnly(boolean readOnly) {
      return Arrays.stream(values())
          .filter(dbType -> dbType.readOnly == readOnly)
          .findFirst()
          .orElseThrow(
              () ->
                  new DatabaseException(
                      "Not Found DB(%s) type".formatted(Arrays.toString(values()))));
    }
  }
}
