package au.com.api.wallet.dao.repository;

import java.util.UUID;

import au.com.api.wallet.dao.entity.PaymentTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransactionEntity, String> {
    PaymentTransactionEntity findPaymentTransactionEntityByTransactionId(UUID transactionId);
}
