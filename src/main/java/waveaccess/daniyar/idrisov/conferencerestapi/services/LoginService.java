package waveaccess.daniyar.idrisov.conferencerestapi.services;

import waveaccess.daniyar.idrisov.conferencerestapi.dto.TokenDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserLoginForm;

public interface LoginService {

    TokenDto login(UserLoginForm userLoginForm);

}
