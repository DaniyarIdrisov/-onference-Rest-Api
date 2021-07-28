package waveaccess.daniyar.idrisov.conferencerestapi.dto;

import lombok.*;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private Long id;

    @Email
    private String email;

    private String hashPassword;

    private String firstName;

    private String lastName;

    private String organization;

    private String confirmCode;

    private User.Role role;

    private User.State state;

    private User.Confirmation confirmation;

    public static UserDto from(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .hashPassword(user.getHashPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .organization(user.getOrganization())
                .confirmCode(user.getConfirmCode())
                .role(user.getRole())
                .state(user.getState())
                .confirmation(user.getConfirmation())
                .build();
    }

    public static List<UserDto> from(List<User> users) {
        return users.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }


}
