package au.com.api.wallet.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@JsonDeserialize(builder = PaymentNotificationRequest.PaymentNotificationRequestBuilder.class)
public class PaymentNotificationRequest {

    @ApiModelProperty("The transaction of the payment notification")
    protected final TransactionRequest transactions;

    @Builder(toBuilder = true)
    protected PaymentNotificationRequest(final TransactionRequest transactions) {
        this.transactions = transactions;
    }

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    public static class PaymentNotificationRequestBuilder {
    }
}
