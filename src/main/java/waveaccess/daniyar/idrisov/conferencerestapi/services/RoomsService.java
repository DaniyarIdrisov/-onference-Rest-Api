package waveaccess.daniyar.idrisov.conferencerestapi.services;

import waveaccess.daniyar.idrisov.conferencerestapi.dto.RoomDto;

import java.util.List;

public interface RoomsService {

    List<RoomDto> getAllRooms();

    RoomDto getRoomById(Long roomId);

    RoomDto getRoomByScheduleId(Long scheduleId);

}
