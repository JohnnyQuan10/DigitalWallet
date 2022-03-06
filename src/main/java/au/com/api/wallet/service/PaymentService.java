package au.com.api.wallet.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import au.com.api.wallet.dao.entity.AccountEntity;
import au.com.api.wallet.dao.entity.PaymentTransactionEntity;
import au.com.api.wallet.dao.repository.AccountRepository;
import au.com.api.wallet.dao.repository.PaymentTransactionRepository;
import au.com.api.wallet.dto.converter.TransactionResponseConverter;
import au.com.api.wallet.dto.request.PaymentNotificationRequest;
import au.com.api.wallet.dto.request.TransactionRequest;
import au.com.api.wallet.dto.response.AccountResponse;
import au.com.api.wallet.dto.response.TransactionResponse;
import au.com.api.wallet.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {

    private final AccountRepository accountRepository;

    private final PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    public PaymentService(final AccountRepository accountRepository,
                          final PaymentTransactionRepository paymentTransactionRepository) {
        this.accountRepository = accountRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    public void save(final PaymentNotificationRequest paymentNotificationRequest) throws BadRequestException{
        final TransactionRequest transaction = paymentNotificationRequest.getTransactions();
        if(Objects.isNull(transaction)) {
            throw new BadRequestException("The transaction is null.");
        }

        final PaymentTransactionEntity paymentTransactionEntities =
                paymentTransactionRepository.findPaymentTransactionEntityByTransactionId(transaction.getId());
        if (Objects.nonNull(paymentTransactionEntities)) {
            throw new BadRequestException("The transaction " + transaction.getId() + " has been recorded before.");
        }

        final AccountEntity accountEntity = createOrUpdateAccount(transaction);
        recordPaymentTransaction(transaction,accountEntity);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(final UUID userId){
        AccountEntity accountEntity = accountRepository.findAccountEntityByUserId(userId);
        return Optional.ofNullable(accountEntity)
                .map(model -> AccountResponse.builder()
                        .userId(model.getUserId())
                        .userName(model.getUserName())
                        .currency(model.getCurrency())
                        .balance(((double)model.getBalance())/100)
                        .build())
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions(final UUID userId){
        List<PaymentTransactionEntity> paymentTransactionEntities =
                accountRepository.findAccountEntityByUserId(userId).getPaymentTransactions();
        return paymentTransactionEntities.stream()
                .map(TransactionResponseConverter::fromEntity)
                .collect(Collectors.toList());
    }

    private AccountEntity createOrUpdateAccount(final TransactionRequest transaction) {
        final AccountEntity accountEntities =
                accountRepository.findAccountEntityByUserId(transaction.getUserId());
        final AccountEntity accountEntity = AccountEntity.builder()
                .id(Objects.isNull(accountEntities) ? null : accountEntities.getId())
                .userId(transaction.getUserId())
                .userName(transaction.getUserName())
                .balance(calculateBalance(transaction.getAmount(),accountEntities))
                .currency(transaction.getCurrency())
                .build();

        accountRepository.save(accountEntity);
        return accountEntity;
    }

    private long calculateBalance(final String amount, final AccountEntity accountEntities) {
        final long addedAmount = (long) (Double.parseDouble(amount)*100);
        return accountEntities == null ? addedAmount : accountEntities.getBalance() + addedAmount;
    }

    private void recordPaymentTransaction(final TransactionRequest transaction, AccountEntity accountEntity) {

        final PaymentTransactionEntity paymentTransactionEntity = PaymentTransactionEntity.builder()
                .transactionId(transaction.getId())
                .account(accountEntity)
                .userName(transaction.getUserName())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .amount((long) (Double.parseDouble(transaction.getAmount())*100))
                .currency(transaction.getCurrency())
                .type(transaction.getType())
                .typeMethod(transaction.getTypeMethod())
                .debitCredit(transaction.getDebitCredit())
                .state(transaction.getState())
                .description(transaction.getDescription())
                .build();

        paymentTransactionRepository.save(paymentTransactionEntity);
    }

}
