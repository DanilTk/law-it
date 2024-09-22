package pl.lawit.domain.service;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.command.PageCommandQuery;
import pl.lawit.domain.mapper.UserDetailsMapper;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.domain.provider.IdpProvider;
import pl.lawit.domain.repository.ApplicationUserRepository;
import pl.lawit.kernel.authentication.ApplicationUserDetails;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.PageResult;
import pl.lawit.kernel.provider.UserDetailsProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsProvider {

	private static final String SYSTEM_UID = "WrRrGPlMRfgKFVgDBlAExTePYKq2";

	private final IdpProvider idpProvider;

	private final ApplicationUserRepository applicationUserRepository;

	@Transactional
	public ApplicationUser findByUuid(UUID uuid) {
		return applicationUserRepository.getByUuid(uuid);
	}

	@Override
	@Transactional
	public Option<ApplicationUserDetails> findByUid(String uid) {
		return applicationUserRepository.findByIdpUid(uid)
			.map(UserDetailsMapper::map);
	}

	@Override
	@Transactional
	public ApplicationUserDetails syncUserDetailsWithIdpByUid(String uid) {
		ApplicationUser idpUser = idpProvider.getByUid(uid);
		ApplicationUser applicationUser = applicationUserRepository.create(idpUser.idpUid(), idpUser.email());
		return UserDetailsMapper.map(applicationUser);
	}

	@Override
	@Transactional
	public ApplicationUserDetails getSystemUser() {
		ApplicationUser applicationUser = applicationUserRepository.getByIdpUid(SYSTEM_UID);
		return UserDetailsMapper.map(applicationUser);
	}

	@Transactional
	public PageResult<ApplicationUser> findAll(PageCommandQuery commandQuery) {
		return applicationUserRepository.findAll(commandQuery);
	}

	@Transactional
	public Option<ApplicationUser> findByEmail(EmailAddress emailAddress) {
		Option<ApplicationUser> existingUser = applicationUserRepository.findByEmail(emailAddress);
		if (existingUser.isDefined()) {
			return existingUser;
		}

		Option<ApplicationUser> idpUser = idpProvider.findByEmail(emailAddress);
		if (idpUser.isDefined()) {
			ApplicationUser migratedUser = applicationUserRepository.create(idpUser.get().idpUid(),
				idpUser.get().email());
			idpUser = Option.of(migratedUser);
		}

		return idpUser;
	}

	@Transactional
	public ApplicationUser migrateApplicationUser(EmailAddress emailAddress) {
		ApplicationUser applicationUser = idpProvider.createUser(emailAddress);
		return applicationUserRepository.create(applicationUser.idpUid(), applicationUser.email());
	}

}
