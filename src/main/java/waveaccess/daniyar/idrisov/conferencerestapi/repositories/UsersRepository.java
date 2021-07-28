package waveaccess.daniyar.idrisov.conferencerestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Talk;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllByRole(User.Role role);

    List<User> findAllByTalksContains(Talk talk);

    Optional<User> findByConfirmCode(String confirmCode);

}
