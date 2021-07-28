package waveaccess.daniyar.idrisov.conferencerestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Token;

import java.util.Optional;

@Repository
public interface TokensRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

}
