package dev.teamuts.payment.domain.point.service;

import dev.teamuts.payment.domain.common.model.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
  public void checkPointStatus(Long memberId, Money pointAmount) {
    // TODO: if the order has any point usage, validate the point status of the member.
  }
}
