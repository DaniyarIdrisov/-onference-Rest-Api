package waveaccess.daniyar.idrisov.conferencerestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserRegistrationForm;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.EmailExistsException;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.UsersRepository;
import waveaccess.daniyar.idrisov.conferencerestapi.utils.MailSender;

import java.util.UUID;

import static waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto.from;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    @Value("${send.mail.subject}")
    private String subject;

    @Value("${send.mail.text1}")
    private String text1;

    @Value("${send.mail.text2}")
    private String text2;

    @Value("${send.mail.link}")
    private String link;

    @Value("${send.mail.text3}")
    private String text3;

    @Override
    public UserDto registerUser(UserRegistrationForm userRegistrationForm) {
        if (isUserWithThisEmailExists(userRegistrationForm.getEmail())) throw new EmailExistsException("User with this email already exists!");
        User user = User.builder()
                .email(userRegistrationForm.getEmail())
                .hashPassword(passwordEncoder.encode(userRegistrationForm.getPassword()))
                .firstName(userRegistrationForm.getFirstName())
                .lastName(userRegistrationForm.getLastName())
                .organization(userRegistrationForm.getOrganization())
                .role(User.Role.LISTENER)
                .state(User.State.ACTIVE)
                .confirmation(User.Confirmation.NOT_CONFIRMED)
                .confirmCode(UUID.randomUUID().toString())
                .build();
        usersRepository.save(user);
        String text = text1 + user.getFirstName() + " " + user.getLastName() + text2 + "\n" + link + user.getConfirmCode() + "\n" + text3;
        mailSender.sendMail(user.getEmail(), subject, text);
        return from(user);
    }

    @Override
    public UserDto activateAccount(String confirmCode) {
        User user = usersRepository.findByConfirmCode(confirmCode).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        user.setConfirmation(User.Confirmation.CONFIRMED);
        usersRepository.save(user);
        return from(user);
    }

    private boolean isUserWithThisEmailExists(String email) {
        User user = usersRepository.findByEmail(email).orElse(null);
        return user != null;
    }

}
