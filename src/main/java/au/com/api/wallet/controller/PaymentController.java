package au.com.api.wallet.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import au.com.api.wallet.dto.request.PaymentNotificationRequest;
import au.com.api.wallet.dto.response.AccountResponse;
import au.com.api.wallet.dto.response.TransactionResponse;
import au.com.api.wallet.exception.BadRequestException;
import au.com.api.wallet.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payment")
public class PaymentController {
    private final PaymentService paymentService;

    protected final ObjectMapper objectMapper;

    @Value("${hmac.key}")
    protected String HMAC_KEY;

    @Value("${hmac.auth.enable}")
    protected Boolean enableHmacAuth;

    public PaymentController(final PaymentService paymentService,
                             final ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> save(@RequestBody @Valid @NotNull final String request,
                                       @RequestHeader("Authorization") @NotNull final String authorization)
            throws BadRequestException, JsonProcessingException {
        //Disable HMAC Auth for facilitating the test
        if(enableHmacAuth && !validateHMAC(authorization,request)) {
            throw new BadRequestException("Unauthorized request");
        }
        final PaymentNotificationRequest paymentNotificationRequest =
                objectMapper.readValue(request, PaymentNotificationRequest.class);
        paymentService.save(paymentNotificationRequest);
        return new ResponseEntity<>("The transaction has been saved.", HttpStatus.CREATED);
    }

    @GetMapping("/account/user/{userId}")
    public AccountResponse getAccount(@PathVariable @Valid @NotBlank final UUID userId) {
         return paymentService.getAccount(userId);
    }

    @GetMapping("/transactions/user/{userId}")
    public List<TransactionResponse> getTransactions(@PathVariable @Valid @NotBlank final UUID userId) {
        return paymentService.getTransactions(userId);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<String> BlogAlreadyExistsException(BadRequestException badRequestException) {
        return new ResponseEntity<>(badRequestException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private Boolean validateHMAC(final String authentication, final String notification){
        final String hmacValue = authentication.substring(authentication.lastIndexOf(" ") + 1);
        final String hmacGeneratedValue = new HmacUtils(authentication.split(" ")[0], HMAC_KEY).hmacHex(notification);
        return hmacValue.equals(hmacGeneratedValue);
    }
}