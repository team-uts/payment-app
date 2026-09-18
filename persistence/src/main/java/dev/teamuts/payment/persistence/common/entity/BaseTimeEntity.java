package dev.teamuts.payment.persistence.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  protected ZonedDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  protected ZonedDateTime updatedAt;
}
