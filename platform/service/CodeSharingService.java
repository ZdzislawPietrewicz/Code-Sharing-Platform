package platform.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.model.CodeEntity;
import platform.persistance.CodeSharingPlatformRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
public class CodeSharingService {
    @Autowired
    CodeSharingPlatformRepository codeSharingPlatformRepository;
    @PersistenceContext
    EntityManager entityManager;

    public List<CodeEntity> getLatestTenCodes() {
        List<CodeEntity> codeEntityList = entityManager.createQuery("SELECT u FROM CodeEntity u WHERE u.time=0 AND u.views=0 ORDER BY u.date DESC", CodeEntity.class)
                .setMaxResults(10).getResultList();
        return codeEntityList;
    }

    public Optional<CodeEntity> getCodeEntityByUuid(UUID uuid) {
        Optional<CodeEntity> codeEntity = codeSharingPlatformRepository.findCodeEntityByUuid(uuid);

        codeEntity.ifPresent((code) -> {
            if (code.isViewsRestricted()) {
                int views = code.getViews() - 1;
                code.setViews(views);
                codeSharingPlatformRepository.save(code);
            }
            if (code.isTimeRestricted()) {
                System.out.println(code);
                if (LocalDateTime.now().isAfter(code.getEndDateTimeSecretCode())) {
                    codeSharingPlatformRepository.delete(code);
                    System.out.println(" Delete");
                } else {
                    code.setTime(ChronoUnit.SECONDS.between(LocalDateTime.now(), code.getEndDateTimeSecretCode()));
                    codeSharingPlatformRepository.save(code);
                }
            }
            if (code.getViews() < 0) codeSharingPlatformRepository.delete(code);
        });
        return codeSharingPlatformRepository.findCodeEntityByUuid(uuid);
    }

    public CodeEntity postNewCodeEntity(CodeEntity newCodeEntity) {
        if (newCodeEntity.getTime() > 0) {
            newCodeEntity.setTimeRestricted(true);
            newCodeEntity.setEndDateTimeSecretCode(newCodeEntity.getDate().plusSeconds(newCodeEntity.getTime()));
        }
        if (newCodeEntity.getViews() > 0) {
            newCodeEntity.setViewsRestricted(true);
        }

        System.out.println(newCodeEntity.getUuid());
        return codeSharingPlatformRepository.save(newCodeEntity);
    }
}
