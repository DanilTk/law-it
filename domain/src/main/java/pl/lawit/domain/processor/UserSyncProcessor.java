package pl.lawit.domain.processor;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.lawit.domain.model.ApplicationUser;
import pl.lawit.domain.provider.IdpProvider;
import pl.lawit.domain.repository.ApplicationUserRepository;
import pl.lawit.kernel.model.TokenizedPageResult;

import static pl.lawit.kernel.logger.ApplicationLoggerFactory.userLogger;

@Component
@RequiredArgsConstructor
public class UserSyncProcessor {

	private static final int PAGE_SIZE = 500;

	private static final int BATCH_SIZE = 100;

	private final IdpProvider idpProvider;

	private final ApplicationUserRepository applicationUserRepository;

	@Transactional
	public void synchronizeUsers() {
		userLogger().info("Synchronizing users");
		List<ApplicationUser> idpUsers = List.empty();
		String pageToken = null;

		do {
			TokenizedPageResult<ApplicationUser> page = idpProvider.findPage(pageToken, PAGE_SIZE);
			List<ApplicationUser> content = page.content();
			processUsers(content);
			idpUsers = idpUsers.appendAll(content);
			pageToken = page.nextPageToken().getOrNull();
		} while (pageToken != null);

		disableNonIdpUsers(idpUsers);
		userLogger().info("Synchronization completed");
	}

	private void disableNonIdpUsers(List<ApplicationUser> idpUsers) {
		List<String> idpUserUids = idpUsers.map(ApplicationUser::idpUid);
		List<String> nonIdpUserUids = applicationUserRepository.findAll()
			.filter(user -> !idpUserUids.contains(user.idpUid()))
			.map(ApplicationUser::idpUid);

		nonIdpUserUids.grouped(BATCH_SIZE).forEach(batch -> applicationUserRepository.disableUsersInBatch(batch));
		userLogger().info("Disabled {} non-IDP users", nonIdpUserUids.size());
	}

	private void processUsers(List<ApplicationUser> users) {

		for (ApplicationUser user : users) {
			Option<ApplicationUser> existingApplicationUser = applicationUserRepository.findByIdpUid(user.idpUid());
			if (existingApplicationUser.isDefined()) {
				applicationUserRepository.update(existingApplicationUser.get().uuid().get(), user.email());
			} else {
				applicationUserRepository.create(user.idpUid(), user.email());
			}
		}
	}

}
