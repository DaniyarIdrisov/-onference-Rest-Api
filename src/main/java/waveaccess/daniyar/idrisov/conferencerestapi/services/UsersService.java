package waveaccess.daniyar.idrisov.conferencerestapi.services;

import waveaccess.daniyar.idrisov.conferencerestapi.dto.AddUserForm;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;

import java.util.List;

public interface UsersService {

    UserDto getUserByToken(String token);

    List<UserDto> getAllUsers();

    UserDto addUser(AddUserForm addUserForm);

    UserDto getUserById(Long userId);

    UserDto updateUserById(Long userId, UserDto userDto);

    UserDto deleteUserById(Long userId, UserDto userDto);

    List<UserDto> getUsersByRole(User.Role role);

    List<UserDto> getUsersByTalkId(Long talkId);

}
