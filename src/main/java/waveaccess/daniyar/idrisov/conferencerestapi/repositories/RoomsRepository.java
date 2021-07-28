package waveaccess.daniyar.idrisov.conferencerestapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Room;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Schedule;

import java.util.Optional;

@Repository
public interface RoomsRepository extends JpaRepository<Room, Long> {

    Optional<Room> findRoomByScheduleContains(Schedule schedule);

}
