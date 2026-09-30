package dev.teamuts.payment.persistence.payment.entity;

import dev.teamuts.payment.domain.payment.constant.CardStatus;
import dev.teamuts.payment.domain.payment.model.Card;
import dev.teamuts.payment.persistence.common.entity.BaseAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(catalog = "payment", name = "cards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class CardJpaEntity extends BaseAuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "payment_method_id", nullable = false)
  private Long payMethodId;

  @Column(name = "brand")
  private String brand;

  @Column(name = "last_four", comment = "Last 4 digits of the card number")
  private String lastFour;

  @Column(name = "expiry_month")
  private Integer expiryMonth;

  @Column(name = "expiry_year")
  private Integer expiryYear;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private CardStatus status;

  public static CardJpaEntity fromDomain(Card card) {
    return CardJpaEntity.builder()
        .id(card.getId())
        .memberId(card.getMemberId())
        .payMethodId(card.getPayMethodId())
        .brand(card.getBrand())
        .lastFour(card.getLast4())
        .expiryMonth(card.getExpMonth())
        .expiryYear(card.getExpYear())
        .status(card.getStatus())
        .build();
  }
}
