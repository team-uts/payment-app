package dev.teamuts.payment.persistence.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditableEntity extends BaseTimeEntity {
  @CreatedBy
  @Column(name = "created_by", nullable = false, updatable = false)
  protected String createdBy;

  @LastModifiedBy
  @Column(name = "updated_by")
  protected String updatedBy;
}
