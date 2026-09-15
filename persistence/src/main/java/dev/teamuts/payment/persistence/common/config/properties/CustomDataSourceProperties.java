package dev.teamuts.payment.persistence.common.config.properties;

import dev.teamuts.payment.persistence.common.constant.DataSourceConstants;
import dev.teamuts.payment.shared.utils.ConfigUtils;
import java.util.Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = DataSourceConstants.SPRING_DATA_SOURCE_PREFIX)
public record CustomDataSourceProperties(
    HikariProperties common, HikariProperties source, HikariProperties replica) {

  public Properties getSourceWithCommon() {
    return ConfigUtils.mergeProperties(common.hikari(), source.hikari());
  }

  public Properties getReplicaWithCommon() {
    return ConfigUtils.mergeProperties(common.hikari(), replica.hikari());
  }
}
