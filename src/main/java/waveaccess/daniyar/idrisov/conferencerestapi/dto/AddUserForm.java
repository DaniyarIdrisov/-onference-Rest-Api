package waveaccess.daniyar.idrisov.conferencerestapi.dto;


import lombok.*;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddUserForm {

    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String organization;

    @NotNull
    private User.Role role;

    @NotNull
    private User.State state;

}
