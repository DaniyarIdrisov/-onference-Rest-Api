package waveaccess.daniyar.idrisov.conferencerestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Room;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Schedule;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> getScheduleByTalk_Id(Long id);

    List<Schedule> getAllByRoom_Id(Long roomId);

}
