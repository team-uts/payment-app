package dev.teamuts.payment.domain.common.lock;

import dev.teamuts.shared.data.constant.LockTtl;
import java.util.List;
import lombok.Getter;

@Getter
public enum PaymentLockGroup {
  AUTH_TOKEN,
  PRAYER_LIKE,
  PRAYER_COMMENT,
  MEMBER_UPDATE,
  FOLLOW,
  FOLLOW_REQUEST,
  MEMBER_IMAGE_UPLOAD,
  ;

  private final LockTtl ttl;

  private static final String DELIMITER = ":";
  private static final String LOCK_PREFIX = "LOCK";

  PaymentLockGroup() {
    this.ttl = LockTtl.FIVE_SECONDS;
  }

  PaymentLockGroup(LockTtl ttl) {
    this.ttl = ttl;
  }

  public String generateLockKey(List<String> params) {
    String paramStr = String.join(DELIMITER, params);
    return String.join(DELIMITER, List.of(LOCK_PREFIX, this.name(), paramStr));
  }

  /**
   * 인자로 받은 ttl이 우선권을 가지게 된다.
   *
   * @param ttl 해당 ttl 값이 [LockTtl.ZERO]인 경우 type에 설정된 ttl로 반환
   */
  public long getValidTtlOrTypeDefaultMillis(LockTtl ttl) {
    if (ttl == LockTtl.ZERO) {
      // type의 ttl 값 반환
      return this.ttl.millis();
    }

    // 인자로 받은 ttl 반환
    return ttl.millis();
  }
}
