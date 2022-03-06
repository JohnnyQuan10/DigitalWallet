package au.com.api.wallet.dto.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Getter
@JsonDeserialize(builder = AccountResponse.AccountResponseBuilder.class)
@FieldNameConstants
@Builder(toBuilder = true)
public class AccountResponse {
    @ApiModelProperty("The user ID of the payment")
    @JsonProperty("user_id")
    @NotNull
    protected final UUID userId;

    @ApiModelProperty("The user name of the payment")
    @JsonProperty("user_name")
    protected final String userName;

    @ApiModelProperty("The balance of the account")
    protected final double balance;

    @ApiModelProperty("The currency of the payment")
    protected final String currency;
}
