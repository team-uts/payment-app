package dev.teamuts.payment.domain.payment.port;

import dev.teamuts.payment.domain.payment.model.PaymentTransaction;
import java.util.List;

public interface PaymentTransactionStorePort {
  List<PaymentTransaction> storeAll(List<PaymentTransaction> transactions);
}
