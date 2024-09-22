package pl.lawit.data;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.data.entity.ApplicationUserEntity;
import pl.lawit.data.entity.ApplicationUserEntity_;
import pl.lawit.data.factory.PageResultFactory;
import pl.lawit.data.factory.PageableFactory;
import pl.lawit.data.jpa.ApplicationUserRepositoryJpa;
import pl.lawit.data.mapper.ApplicationUserMapper;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.domain.repository.ApplicationUserRepository;
import pl.lawit.kernel.exception.ObjectNotFoundException;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.kernel.util.TimeProvider;

import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@RequiredArgsConstructor
public class ApplicationUserRepositoryDb implements ApplicationUserRepository {

	private final ApplicationUserRepositoryJpa applicationUserRepositoryJpa;

	private final ApplicationUserMapper applicationUserMapper;

	private final PageResultFactory pageResultFactory;

	private final TimeProvider timeProvider;

	@Override
	@Transactional(propagation = MANDATORY)
	public ApplicationUser create(String idpUid, EmailAddress email) {
		ApplicationUserEntity entity = applicationUserMapper.mapToEntity(idpUid, email);
		applicationUserRepositoryJpa.save(entity);
		return applicationUserMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public List<ApplicationUser> findAll() {
		return List.ofAll(applicationUserRepositoryJpa.findAll())
			.map(applicationUserMapper::mapToDomain);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public PageResult<ApplicationUser> findAll(PageCommandQuery commandQuery) {
		Pageable pageable = getPageable(commandQuery);

		Page<ApplicationUser> page = applicationUserRepositoryJpa.findAll(pageable)
			.map(applicationUserMapper::mapToDomain);

		return pageResultFactory.create(page);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public ApplicationUser getByUuid(UUID uuid) {
		ApplicationUserEntity entity = applicationUserRepositoryJpa.getReferenceByUuid(uuid);
		return applicationUserMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<ApplicationUser> findByIdpUid(String uid) {
		return applicationUserRepositoryJpa.findByIdpUid(uid)
			.map(applicationUserMapper::mapToDomain);
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public ApplicationUser getByIdpUid(String uid) {
		return findByIdpUid(uid)
			.getOrElseThrow(() -> ObjectNotFoundException.byUid(uid, ApplicationUser.class));
	}

	@Override
	@Transactional(readOnly = true, propagation = MANDATORY)
	public Option<ApplicationUser> findByEmail(EmailAddress email) {
		return applicationUserRepositoryJpa.findByEmail(email.value())
			.map(applicationUserMapper::mapToDomain);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public ApplicationUser update(UUID uuid, EmailAddress email) {
		ApplicationUserEntity entity = applicationUserRepositoryJpa.getReferenceByUuid(uuid);
		entity.setEmail(email.value());
		entity.setLastSyncAt(timeProvider.getInstant());
		return applicationUserMapper.mapToDomain(entity);
	}

	@Override
	@Transactional(propagation = MANDATORY)
	public void disableUsersInBatch(List<String> collection) {
		applicationUserRepositoryJpa.disableUsers(collection);
	}

	private Pageable getPageable(PageCommandQuery pageQuery) {
		Sort sort = Sort.by(Sort.Direction.DESC, ApplicationUserEntity_.CREATED_AT);

		return PageableFactory.create(pageQuery, sort);
	}
}
