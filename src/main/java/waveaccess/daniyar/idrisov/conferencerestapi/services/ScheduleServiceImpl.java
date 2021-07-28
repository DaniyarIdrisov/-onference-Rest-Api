package waveaccess.daniyar.idrisov.conferencerestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.*;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.AddEntityException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.UsageException;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Room;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Schedule;
import waveaccess.daniyar.idrisov.conferencerestapi.models.Talk;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.RoomsRepository;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.ScheduleRepository;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.TalksRepository;
import waveaccess.daniyar.idrisov.conferencerestapi.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static waveaccess.daniyar.idrisov.conferencerestapi.dto.ScheduleDto.from;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RoomsRepository roomsRepository;

    @Autowired
    private TalksRepository talksRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public ScheduleDto getScheduleByTalkId(Long talkId) {
        return from(scheduleRepository.getScheduleByTalk_Id(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!")));
    }

    @Override
    public List<ScheduleDto> getScheduleByRoomId(Long roomId) {
        return from(scheduleRepository.getAllByRoom_Id(roomId));
    }

    @Override
    public List<ScheduleDto> getAllSchedule() {
        return from(scheduleRepository.findAll());
    }

    @Override
    public ScheduleDto getScheduleById(Long scheduleId) {
        return from(scheduleRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!")));
    }

    @Override
    public ScheduleDto addTalkToSchedule(Long roomId, Long talkId, AddScheduleForm addScheduleForm, UserDto userDto) {
        if (!Pattern.matches("^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", addScheduleForm.getTalkTime()))
            throw new IllegalArgumentException("Wrong time format, correct format XX:XX-XX:XX, without spaces!");
        if (!isYourTalk(talkId, userDto)) throw new UsageException("This is not your talk!");
        if (thisTalkIsAlreadyInSchedule(talkId)) throw new AddEntityException("This talk already in schedule!");
        if (thisTimeIsAlreadyTaken(roomId, addScheduleForm.getTalkTime()))
            throw new AddEntityException("This time is already taken!");
        Room room = roomsRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        Talk talk = talksRepository.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        Schedule schedule = Schedule.builder()
                .talkTime(addScheduleForm.getTalkTime())
                .room(room)
                .talk(talk)
                .build();
        scheduleRepository.save(schedule);
        return from(schedule);
    }

    @Override
    public ScheduleDto updateSchedule(Long scheduleId, Long roomId, UpdateScheduleForm scheduleDto, UserDto userDto) {
        if (!Pattern.matches("^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", scheduleDto.getTalkTime()))
            throw new IllegalArgumentException("Wrong time format, correct format XX:XX-XX:XX, without spaces!");
        if (!isYourSchedule(scheduleId, userDto)) throw new UsageException("This is not your schedule!");
        if (thisTimeIsAlreadyTaken(roomId, scheduleDto.getTalkTime()))
            throw new AddEntityException("This time is already taken!");
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        Room room = roomsRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        schedule.setTalkTime(scheduleDto.getTalkTime());
        schedule.setRoom(room);
        scheduleRepository.save(schedule);
        return from(schedule);
    }

    private boolean isYourSchedule(Long scheduleId, UserDto userDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        User user = usersRepository.findById(userDto.getId()).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        List<Talk> talks = user.getTalks();
        for (Talk talk : talks) {
            if (talk.equals(schedule.getTalk())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ScheduleDto deleteScheduleById(Long scheduleId, UserDto userDto) {
        if (!isYourSchedule(scheduleId, userDto)) throw new UsageException("This is not your schedule!");
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        scheduleRepository.deleteById(scheduleId);
        return from(schedule);
    }

    @Override
    public List<ScheduleRoomsDto> getSeparateScheduleForRooms() {
        List<ScheduleRoomsDto> scheduleRoomsDtoList = new ArrayList<>();
        List<Room> rooms = roomsRepository.findAll();
        for (Room room : rooms) {
            List<Schedule> schedules = scheduleRepository.getAllByRoom_Id(room.getId());
            ScheduleRoomsDto scheduleRoomsDto = ScheduleRoomsDto.builder()
                    .roomDto(RoomDto.from(room))
                    .scheduleDtoList(from(schedules))
                    .build();
            scheduleRoomsDtoList.add(scheduleRoomsDto);
        }
        return scheduleRoomsDtoList;
    }

    private boolean thisTimeIsAlreadyTaken(Long roomId, String talkTime) {
        Room room = roomsRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        List<Schedule> schedules = room.getSchedule();
        for (Schedule schedule : schedules) {
            if (isTimeTaken(talkTime, schedule.getTalkTime())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isTimeTaken(String comparableTime, String time) {
        String[] comparableStrings = comparableTime.split("-");
        int comparableLeft = convertToInteger(comparableStrings[0]);
        int comparableRight = convertToInteger(comparableStrings[1]);
        if (comparableLeft > comparableRight) {
            comparableRight = comparableLeft + (1440 - (comparableLeft - comparableRight));
        }


        String[] strings = time.split("-");
        int left = convertToInteger(strings[0]);
        int right = convertToInteger(strings[1]);
        if (left > right) {
            right = left + (1440 - (left - right));
        }

        return !check(comparableLeft, comparableRight, left, right);
    }

    private static int convertToInteger(String time) {
        StringBuilder stringBuilder = new StringBuilder(time);
        int first = 0;
        int second = 0;
        if (stringBuilder.charAt(0) == '0') {
            if (stringBuilder.charAt(1) != '0') {
                first = Character.getNumericValue(stringBuilder.charAt(1)) * 60;
            }
        } else {
            String substring = stringBuilder.substring(0, 2);
            first = Integer.parseInt(substring) * 60;
        }
        if (stringBuilder.charAt(3) == '0') {
            if (stringBuilder.charAt(4) != '0') {
                second = Character.getNumericValue(stringBuilder.charAt(1));
            }
        } else {
            String substring = stringBuilder.substring(3, 5);
            second = Integer.parseInt(substring);
        }
        return first + second;
    }

    private static boolean check(int comparableLeft, int comparableRight, int left, int right) {
        if (comparableLeft <= left) {
            return comparableRight <= left;
        } else {
            if (comparableLeft < right) {
                return false;
            } else {
                return comparableRight > left;
            }
        }
    }

    private boolean isYourTalk(Long talkId, UserDto userDto) {
        User user = usersRepository.findById(userDto.getId()).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        Talk talk = talksRepository.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Wrong argument, no entity exists for that argument!"));
        List<Talk> talks = user.getTalks();
        for (Talk talk1 : talks) {
            if (talk.getId().equals(talk1.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean thisTalkIsAlreadyInSchedule(Long talkId) {
        List<Schedule> schedules = scheduleRepository.findAll();
        Talk talk = talksRepository.findById(talkId).orElse(null);
        for (Schedule schedule : schedules) {
            if (schedule.getTalk().equals(talk)) {
                return true;
            }
        }
        return false;
    }

}
