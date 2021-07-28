package waveaccess.daniyar.idrisov.conferencerestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.AddUserForm;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.DeleteEntityException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.EmailExistsException;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Talk;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Token;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.TalksRepository;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.TokensRepository;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.UsersRepository;

import java.util.List;
import java.util.UUID;

import static waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto.from;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TalksRepository talksRepository;

    @Autowired
    private TokensRepository tokensRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isUserWithThisEmailExists(String email) {
        User user = usersRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    @Override
    public UserDto getUserByToken(String token) {
        Token resultToken = tokensRepository.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        return from(resultToken.getUser());
    }

    @Override
    public List<UserDto> getAllUsers() {
        return from(usersRepository.findAll());
    }

    @Override
    public UserDto addUser(AddUserForm addUserForm) {
        if (isUserWithThisEmailExists(addUserForm.getEmail())) throw new EmailExistsException("User with this email already exists!");
        User user = User.builder()
                .email(addUserForm.getEmail())
                .hashPassword(passwordEncoder.encode(addUserForm.getPassword()))
                .firstName(addUserForm.getFirstName())
                .lastName(addUserForm.getLastName())
                .organization(addUserForm.getOrganization())
                .state(addUserForm.getState())
                .role(addUserForm.getRole())
                .confirmation(User.Confirmation.NOT_CONFIRMED)
                .confirmCode(UUID.randomUUID().toString())
                .build();
        usersRepository.save(user);
        return from(user);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return from(usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!")));
    }

    @Override
    public UserDto updateUserById(Long userId, UserDto userDto) {
        User user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        if (isUserWithThisEmailExists(userDto.getEmail())) throw new EmailExistsException("User with this email already exists!");
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getOrganization() != null) {
            user.setOrganization(userDto.getOrganization());
        }
        if (userDto.getState() != null) {
            user.setState(userDto.getState());
        }
        if (userDto.getRole() != null) {
            user.setRole(userDto.getRole());
        }
        if (userDto.getConfirmation() != null) {
            user.setConfirmation(userDto.getConfirmation());
        }
        if (userDto.getHashPassword() != null) {
            user.setHashPassword(passwordEncoder.encode(userDto.getHashPassword()));
        }
        usersRepository.save(user);
        return from(user);
    }

    private boolean hasChildren(Long userId) {
        User user = usersRepository.findById(userId).orElse(null);
        assert user != null;
        return !user.getTalks().isEmpty();
    }

    @Override
    public UserDto deleteUserById(Long userId, UserDto userDto) {
        if (userId.equals(userDto.getId())) throw new DeleteEntityException("You cannot delete yourself!");
        if (hasChildren(userId)) throw new DeleteEntityException("You cannot delete an entity as it has children!");
        User user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        usersRepository.deleteById(userId);
        return from(user);
    }

    @Override
    public List<UserDto> getUsersByRole(User.Role role) {
        return from(usersRepository.findAllByRole(role));
    }

    @Override
    public List<UserDto> getUsersByTalkId(Long talkId) {
        Talk talk = talksRepository.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        return from(usersRepository.findAllByTalksContains(talk));
    }

}
