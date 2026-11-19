package dev.teamuts.payment.domain.common.lock;

import dev.teamuts.shared.data.constant.KtLockGroup;
import dev.teamuts.shared.data.lock.BaseDistributedLockAspect;
import dev.teamuts.shared.data.lock.CacheLockPort;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DistributedTryLockAspect extends BaseDistributedLockAspect {
  protected DistributedTryLockAspect(
      @Qualifier("lettuceCacheLockAdapter") CacheLockPort cacheLockPort) {
    super(cacheLockPort);
  }

  @Around("@annotation(dev.teamuts.shared.data.annotation.TryLock)")
  public Object acquireLock(ProceedingJoinPoint joinPoint) throws Throwable {
    return super.acquireLock(joinPoint);
  }

  @Override
  protected KtLockGroup getLockGroup(String groupName) {
    return PaymentLockGroup.of(groupName);
  }
}
