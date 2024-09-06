package pl.lawit.web.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lawit.kernel.model.ApplicationUserRole;

@Getter
@Setter
public class ClaimDto {

    private String claimName;
    private ApplicationUserRole applicationUserRole;

}
