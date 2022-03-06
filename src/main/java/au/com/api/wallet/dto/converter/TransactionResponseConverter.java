package au.com.api.wallet.dto.converter;

import java.util.Optional;

import au.com.api.wallet.dao.entity.PaymentTransactionEntity;
import au.com.api.wallet.dto.response.TransactionResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class TransactionResponseConverter {

	public static TransactionResponse fromEntity (PaymentTransactionEntity paymentTransactionEntity) {
		return Optional.ofNullable(paymentTransactionEntity)
				.map(model -> TransactionResponse.builder()
						.transactionId(model.getTransactionId())
						.userName(model.getUserName())
						.createdAt(model.getCreatedAt())
						.updatedAt(model.getUpdatedAt())
						.amount(((double)model.getAmount())/100)
						.currency(model.getCurrency())
						.type(model.getType())
						.typeMethod(model.getTypeMethod())
						.debitCredit(model.getDebitCredit())
						.state(model.getState())
						.description(model.getDescription()).build()
				).orElse(null);
	}
}
