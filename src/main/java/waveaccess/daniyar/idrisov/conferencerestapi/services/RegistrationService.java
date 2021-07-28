package waveaccess.daniyar.idrisov.conferencerestapi.services;

import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserRegistrationForm;

public interface RegistrationService {

    UserDto registerUser(UserRegistrationForm userRegistrationForm);

    UserDto activateAccount(String confirmCode);

}
