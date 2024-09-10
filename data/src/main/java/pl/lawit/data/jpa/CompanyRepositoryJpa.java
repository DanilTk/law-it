package pl.lawit.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lawit.data.entity.CompanyEntity;
import pl.lawit.domain.model.Company;
import pl.lawit.kernel.exception.ObjectNotFoundException;

import java.util.UUID;

public interface CompanyRepositoryJpa extends JpaRepository<CompanyEntity, UUID> {

	default CompanyEntity getReferenceByUuid(UUID uuid) {
		return findById(uuid).orElseThrow(() -> ObjectNotFoundException.byUUID(uuid, Company.class));
	}

	boolean existsByCompanyEmail(String companyEmail);

	boolean existsByNip(String nip);

	boolean existsByCompanyName(String companyName);

}
