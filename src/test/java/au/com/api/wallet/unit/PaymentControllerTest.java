package au.com.api.wallet.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import au.com.api.wallet.controller.PaymentController;
import au.com.api.wallet.dto.request.PaymentNotificationRequest;
import au.com.api.wallet.dto.request.TransactionRequest;
import au.com.api.wallet.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    public static final TransactionRequest transactionRequest = TransactionRequest.builder()
            .id(UUID.randomUUID())
            .userId(UUID.fromString("449416d8-ec3c-4c0b-a326-e2cfaadaa3a6"))
            .userName("John Smith")
            .amount("100.00")
            .build();

    public static final PaymentNotificationRequest paymentNotificationRequest = PaymentNotificationRequest.builder()
            .transactions(transactionRequest).build();

    public static final UUID USER_ID = UUID.fromString("449416d8-ec3c-4c0b-a326-e2cfaadaa3a6");


    @Test
    public void getAccount_givenValidRequest_succeeds() {
        // When
        paymentController.getAccount(USER_ID);

        // Then
        verify(paymentService, times(1)).getAccount(USER_ID);
    }

    @Test
    public void getTransactions_givenValidRequest_succeeds() {
        // When
        paymentController.getTransactions(USER_ID);

        // Then
        verify(paymentService, times(1)).getTransactions(USER_ID);
    }

    @Test
    public void savePaymentTransaction_givenInvalidRequest_shouldThrowNullPointerException(){
        // Given - When
        assertThrows(NullPointerException.class,
                () -> paymentController.save(null, null));
    }

}