package platform.persistance;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import platform.model.CodeEntity;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface CodeSharingPlatformRepository extends PagingAndSortingRepository<CodeEntity, Long> {

    Optional<CodeEntity> findCodeEntityByUuid(UUID uuid);
}
