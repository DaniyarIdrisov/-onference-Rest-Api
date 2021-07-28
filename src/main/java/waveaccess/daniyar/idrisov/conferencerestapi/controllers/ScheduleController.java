package waveaccess.daniyar.idrisov.conferencerestapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.*;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.AddEntityException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.DeleteEntityException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.UsageException;
import waveaccess.daniyar.idrisov.conferencerestapi.services.RoomsService;
import waveaccess.daniyar.idrisov.conferencerestapi.services.ScheduleService;
import waveaccess.daniyar.idrisov.conferencerestapi.services.TalksService;
import waveaccess.daniyar.idrisov.conferencerestapi.services.UsersService;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private RoomsService roomsService;

    @Autowired
    private TalksService talksService;

    @ApiOperation(value = "Получение всего расписания")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = ScheduleDto.class)})
    @PermitAll
    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduleDto>> getAllSchedule() {
        return ResponseEntity.ok(scheduleService.getAllSchedule());
    }

    @ApiOperation(value = "Получение расписания по определенному id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = ScheduleDto.class)})
    @PermitAll
    @GetMapping("/schedule/{schedule-id}")
    public ResponseEntity<ScheduleDto> getScheduleById(@PathVariable("schedule-id") Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleById(scheduleId));
    }

    @ApiOperation(value = "Добавление доклада в расписание по id аудитории и id доклада, добавление времени доклад")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно добавлено", response = ScheduleDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @PostMapping("/schedule/rooms/{room-id}/talks/{talk-id}")
    public ResponseEntity<ScheduleDto> addTalkToSchedule(@PathVariable("room-id") Long roomId, @PathVariable("talk-id") Long talkId, @RequestBody @Valid AddScheduleForm addScheduleForm, @RequestHeader("Authorization") String token) {
        UserDto userDto = usersService.getUserByToken(token);
        return ResponseEntity.ok(scheduleService.addTalkToSchedule(roomId, talkId, addScheduleForm, userDto));
    }

    @ApiOperation(value = "Изменение расписания по по id аудитории, изменение времени доклада")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно изменено", response = ScheduleDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @PatchMapping("/schedule/{schedule-id}/rooms/{room-id}")
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable("schedule-id") Long scheduleId, @PathVariable("room-id") Long roomId, @RequestBody @Valid UpdateScheduleForm scheduleDto, @RequestHeader("Authorization") String token) {
        UserDto userDto = usersService.getUserByToken(token);
        return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, roomId, scheduleDto, userDto));
    }

    @ApiOperation(value = "Удаление расписания по определенному id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно удалено", response = ScheduleDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @DeleteMapping("/schedule/{schedule-id}")
    @ResponseBody
    public ResponseEntity<ScheduleDto> deleteScheduleById(@PathVariable("schedule-id") Long scheduleId, @RequestHeader("Authorization") String token) {
        UserDto userDto = usersService.getUserByToken(token);
        return ResponseEntity.ok(scheduleService.deleteScheduleById(scheduleId, userDto));
    }

    @ApiOperation(value = "Получение аудитории по определеному id расписания")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = RoomDto.class)})
    @PermitAll
    @GetMapping("/schedule/{schedule-id}/room")
    public ResponseEntity<RoomDto> getRoomByScheduleId(@PathVariable("schedule-id") Long scheduleId) {
        return ResponseEntity.ok(roomsService.getRoomByScheduleId(scheduleId));
    }

    @ApiOperation(value = "Получение доклада по определенному id расписания")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = TalkDto.class)})
    @PermitAll
    @GetMapping("/schedule/{schedule-id}/talk")
    public ResponseEntity<TalkDto> getTalkByScheduleId(@PathVariable("schedule-id") Long scheduleId) {
        return ResponseEntity.ok(talksService.getTalkByScheduleId(scheduleId));
    }

    @ExceptionHandler({AddEntityException.class, DeleteEntityException.class, UsageException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionDto> scheduleExceptions(Exception exception) {
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(exception.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.badRequest().body(exceptionDto);
    }

}
