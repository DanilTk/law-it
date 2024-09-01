package pl.lawit.data;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.jpa.ApplicationUserRepositoryJpa;
import pl.lawit.data.mapper.ApplicationUserMapper;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.domain.repository.ApplicationUserRepository;
import pl.lawit.kernel.exception.ObjectNotFoundException;
import pl.lawit.kernel.model.EmailAddress;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@RequiredArgsConstructor
public class ApplicationUserRepositoryDb implements ApplicationUserRepository {

	private final ApplicationUserRepositoryJpa applicationUserRepositoryJpa;

	private final ApplicationUserMapper applicationUserMapper;

	@Override
	@Transactional(propagation = MANDATORY)
	public ApplicationUser create(String idpSub, EmailAddress email) {
		ApplicationUserEntity entity = applicationUserMapper.mapToEntity(idpSub, email);
		applicationUserRepositoryJpa.save(entity);
		return applicationUserMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public ApplicationUser getByUuid(UUID uuid) {
		ApplicationUserEntity entity = applicationUserRepositoryJpa.getReferenceByUuid(uuid);
		return applicationUserMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<ApplicationUser> findBySub(String sub) {
		return applicationUserRepositoryJpa.findByIdpSub(sub)
			.map(applicationUserMapper::mapToDomain);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public ApplicationUser getBySub(String sub) {
		return findBySub(sub)
			.getOrElseThrow(() -> ObjectNotFoundException.bySub(sub, ApplicationUser.class));
	}

	@Override
	public Option<ApplicationUser> findByEmail(EmailAddress email) {
		return applicationUserRepositoryJpa.findByEmail(email.value())
			.map(applicationUserMapper::mapToDomain);
	}
}
