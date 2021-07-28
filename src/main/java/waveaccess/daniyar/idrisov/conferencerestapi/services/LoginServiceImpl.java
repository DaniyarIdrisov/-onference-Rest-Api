package waveaccess.daniyar.idrisov.conferencerestapi.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.TokenDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserLoginForm;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Token;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.TokensRepository;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.UsersRepository;

import java.util.UUID;
import java.util.function.Supplier;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokensRepository tokensRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public TokenDto login(UserLoginForm userLoginForm) {
        User user = usersRepository.findByEmail(userLoginForm.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        if (passwordEncoder.matches(userLoginForm.getPassword(), user.getHashPassword())) {
            String tokenValue = UUID.randomUUID().toString();
            Token token = Token.builder()
                    .token(tokenValue)
                    .user(user)
                    .build();
            tokensRepository.save(token);
            return TokenDto.builder()
                    .token(tokenValue)
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

}
