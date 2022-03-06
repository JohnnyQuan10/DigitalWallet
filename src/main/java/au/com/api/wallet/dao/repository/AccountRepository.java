package au.com.api.wallet.dao.repository;

import java.util.UUID;

import au.com.api.wallet.dao.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    AccountEntity findAccountEntityByUserId(UUID userId);
}
