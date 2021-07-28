package waveaccess.daniyar.idrisov.conferencerestapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.ExceptionDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.RoomDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.ScheduleDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.services.RoomsService;
import waveaccess.daniyar.idrisov.conferencerestapi.services.ScheduleService;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
public class RoomsController {

    @Autowired
    private RoomsService roomsService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "Получение всех аудиторий")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = RoomDto.class)})
    @PermitAll
    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        return ResponseEntity.ok(roomsService.getAllRooms());
    }

    @ApiOperation(value = "Получение аудитории по определенному id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = RoomDto.class)})
    @PermitAll
    @GetMapping("/rooms/{room-id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable("room-id") Long roomId) {
        return ResponseEntity.ok(roomsService.getRoomById(roomId));
    }

    @ApiOperation(value = "Получение расписания по определенному id аудитории")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = ScheduleDto.class)})
    @PermitAll
    @GetMapping("/rooms/{room-id}/schedule")
    public ResponseEntity<List<ScheduleDto>> getScheduleByRoomId(@PathVariable("room-id") Long roomId) {
        return ResponseEntity.ok(scheduleService.getScheduleByRoomId(roomId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> usersExceptions(Exception exception) {
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(exception.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.badRequest().body(exceptionDto);
    }

}
