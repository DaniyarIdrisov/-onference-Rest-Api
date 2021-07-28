package waveaccess.daniyar.idrisov.conferencerestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Talk;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalksRepository extends JpaRepository<Talk, Long> {

    List<Talk> getAllBySpeakersContains(User user);

    Optional<Talk> getTalkBySchedule_Id(Long scheduleId);

}
