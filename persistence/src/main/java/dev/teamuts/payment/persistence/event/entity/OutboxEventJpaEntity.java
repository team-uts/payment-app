package dev.teamuts.payment.persistence.event.entity;

import static dev.teamuts.payment.domain.event.model.OutboxEvent.LIMITED_PAYLOAD_LENGTH;

import dev.teamuts.payment.domain.event.constant.EventType;
import dev.teamuts.payment.domain.event.constant.OutboxEventStatus;
import dev.teamuts.payment.domain.event.model.OutboxEvent;
import dev.teamuts.payment.persistence.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(catalog = "payment", name = "outbox_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class OutboxEventJpaEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_type", nullable = false, comment = "event type")
  private EventType eventType;

  @Column(
      name = "payload",
      nullable = false,
      length = LIMITED_PAYLOAD_LENGTH,
      comment = "event payload")
  private String payload;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, comment = "event status")
  private OutboxEventStatus status;

  public static OutboxEventJpaEntity newEntity(OutboxEvent outboxEvent) {
    return OutboxEventJpaEntity.builder()
        .eventType(outboxEvent.getEventType())
        .payload(outboxEvent.getPayload())
        .status(outboxEvent.getStatus())
        .build();
  }

  public void updateStatus(OutboxEventStatus status) {
    this.status = status;
  }
}
