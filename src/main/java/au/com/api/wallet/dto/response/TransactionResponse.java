package au.com.api.wallet.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Getter
@JsonDeserialize(builder = TransactionResponse.TransactionResponseBuilder.class)
@FieldNameConstants
@Builder(toBuilder = true)
public class TransactionResponse {
    @ApiModelProperty("The user ID of the transaction")
    @JsonProperty("user_id")
    @NotNull
    protected final UUID userId;

    @ApiModelProperty("The transaction ID of the transaction")
    @JsonProperty("transaction_id")
    @NotNull
    protected final UUID transactionId;

    @ApiModelProperty("The user name of the transaction")
    @JsonProperty("user_name")
    protected final String userName;

    @ApiModelProperty("The amount of the transaction")
    protected final double amount;

    @ApiModelProperty("The currency of the transaction")
    protected final String currency;

    @ApiModelProperty("The created time of the transaction")
    @JsonProperty("created_at")
    protected final LocalDateTime createdAt;

    @ApiModelProperty("The updated time of the transaction")
    @JsonProperty("updated_at")
    protected final LocalDateTime updatedAt;

    @ApiModelProperty("The description of the transaction")
    protected final String description;

    @ApiModelProperty("The type of the transaction")
    protected final String type;

    @ApiModelProperty("The type method of the transaction")
    @JsonProperty("type_method")
    protected final String typeMethod;

    @ApiModelProperty("The state of the transaction")
    protected final String state;

    @ApiModelProperty("The debit or credit payment method")
    @JsonProperty("debit_credit")
    protected final String debitCredit;
}
