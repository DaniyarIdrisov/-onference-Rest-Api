package waveaccess.daniyar.idrisov.conferencerestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.RoomDto;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Schedule;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.RoomsRepository;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.ScheduleRepository;

import java.util.List;
import java.util.function.Supplier;

import static waveaccess.daniyar.idrisov.conferencerestapi.dto.RoomDto.from;

@Service
public class RoomsServiceImpl implements RoomsService {

    @Autowired
    private RoomsRepository roomsRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public List<RoomDto> getAllRooms() {
        return from(roomsRepository.findAll());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        return from(roomsRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!")));
    }

    @Override
    public RoomDto getRoomByScheduleId(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        return from(roomsRepository.findRoomByScheduleContains(schedule).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!")));
    }

}
