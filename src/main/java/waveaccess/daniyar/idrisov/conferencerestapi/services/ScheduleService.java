package waveaccess.daniyar.idrisov.conferencerestapi.services;

import waveaccess.daniyar.idrisov.conferencerestapi.dto.*;

import java.util.List;

public interface ScheduleService {

    ScheduleDto getScheduleByTalkId(Long talkId);

    List<ScheduleDto> getScheduleByRoomId(Long roomId);

    List<ScheduleDto> getAllSchedule();

    ScheduleDto getScheduleById(Long scheduleId);

    ScheduleDto addTalkToSchedule(Long roomId, Long talkId, AddScheduleForm addScheduleForm, UserDto userDto);

    ScheduleDto updateSchedule(Long scheduleId, Long roomId, UpdateScheduleForm scheduleDto, UserDto userDto);

    ScheduleDto deleteScheduleById(Long scheduleId, UserDto userDto);

    List<ScheduleRoomsDto> getSeparateScheduleForRooms();

}
