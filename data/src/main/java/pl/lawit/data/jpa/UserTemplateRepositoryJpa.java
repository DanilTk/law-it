package pl.lawit.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.lawit.data.entity.UserTemplateEntity;

import java.util.UUID;

public interface UserTemplateRepositoryJpa extends JpaRepository<UserTemplateEntity, UUID>,
	JpaSpecificationExecutor<UserTemplateEntity> {

}
