package pl.lawit.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

    private String email;
    private String password;
    private String displayName;

}