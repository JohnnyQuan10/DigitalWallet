package au.com.api.wallet.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public abstract class AbstractNotification {
	@ApiModelProperty("The id of the transaction")
	@NotNull
	protected final UUID id;

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

	protected AbstractNotification(final UUID id, final LocalDateTime createdAt, final LocalDateTime updatedAt,
								   final String description, final String type, final String typeMethod,
								   final String state) {
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.description = description;
		this.type = type;
		this.typeMethod = typeMethod;
		this.state= state;
	}
}
