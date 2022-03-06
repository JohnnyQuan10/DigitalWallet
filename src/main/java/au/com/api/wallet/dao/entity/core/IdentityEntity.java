package au.com.api.wallet.dao.entity.core;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;

@FieldNameConstants
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdentityEntity implements IdentifiableEntity {

	private static final String KEY_GENERATOR_NAME = "surrogateKeyGenerator";
	private static final String KEY_GENERATOR_TYPE = "uuid2";

	/**
	 * The unique immutable id generate by the server to uniquely identify the resource. It is to be used as the
	 * database primary key and never exposed to clients.
	 */
	@Id
	@GeneratedValue(generator = KEY_GENERATOR_NAME)
	@GenericGenerator(name = KEY_GENERATOR_NAME, strategy = KEY_GENERATOR_TYPE)
	@Column(name = "id", columnDefinition = "UUID", unique = true, nullable = false)
	protected UUID id;

	@Builder(toBuilder = true)
	protected IdentityEntity(final UUID id) {
		this.id = id;
	}

	public static class IdentityEntityBuilder {

		protected IdentityEntityBuilder() {
		}
	}
}
