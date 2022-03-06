package au.com.api.wallet.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import au.com.api.wallet.dto.AbstractNotification;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@JsonDeserialize(builder = TransactionRequest.TransactionRequestBuilder.class)
public class TransactionRequest extends AbstractNotification {

    @ApiModelProperty("The user ID of the payment")
    @JsonProperty("user_id")
    @NotNull
    protected final UUID userId;

    @ApiModelProperty("The user name of the payment")
    @JsonProperty("user_name")
    protected final String userName;

    @ApiModelProperty("The amount of the payment")
    protected final String amount;

    @ApiModelProperty("The currency of the payment")
    protected final String currency;

    @ApiModelProperty("The debit or credit payment method")
    @JsonProperty("debit_credit")
    protected final String debitCredit;

    @Builder(toBuilder = true)
    protected TransactionRequest(final UUID id, final LocalDateTime createdAt, final LocalDateTime updatedAt,
                                 final String description, final String type, final String typeMethod,
                                 final String state, final UUID userId, final String userName, final String amount,
                                 final String currency, final String debitCredit) {
        super(id, createdAt, updatedAt, description, type, typeMethod,state);

        this.userId = userId;
        this.userName = userName;
        this.amount = amount;
        this.currency = currency;
        this.debitCredit = debitCredit;
    }

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    public static class TransactionRequestBuilder {
    }
}
