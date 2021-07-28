package waveaccess.daniyar.idrisov.conferencerestapi.dto;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginForm {

    private String email;

    private String password;

}
