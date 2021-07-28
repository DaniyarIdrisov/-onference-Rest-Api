package waveaccess.daniyar.idrisov.conferencerestapi.security.details;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Token;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.TokensRepository;

import java.util.function.Supplier;

@Component("userDetailsServiceToken")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TokensRepository tokensRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String token) {
        Token resultToken = tokensRepository.findByToken(token).orElseThrow((Supplier<Throwable>) () -> new UsernameNotFoundException("Token not found"));
        return new UserDetailsImpl(resultToken.getUser());
    }

}
