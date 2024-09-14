package pl.lawit.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lawit.domain.command.LawyerCommand.CreateLawyer;
import pl.lawit.domain.model.Pesel;
import pl.lawit.kernel.authentication.AuthenticatedUser;
import pl.lawit.kernel.authentication.AuthenticatedUserResolver;
import pl.lawit.kernel.model.EmailAddress;
import pl.lawit.kernel.model.MoneyAmount;
import pl.lawit.web.dto.LawyerDto.CreateLawyerRequestDto;

@Component
@RequiredArgsConstructor
public class LawyerCommandMapper {

	private final AuthenticatedUserResolver authenticatedUserResolver;

	public CreateLawyer mapToCreateLawyerCommand(CreateLawyerRequestDto dto) {
		AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser();

		return CreateLawyer.builder()
			.authenticatedUser(authenticatedUser)
			.pesel(Pesel.of(dto.pesel()))
			.fileUuid(dto.certificateId())
			.hourlyRate(MoneyAmount.of(dto.hourlyRate()))
			.userEmail(EmailAddress.of(dto.userEmail()))
			.build();
	}
}
