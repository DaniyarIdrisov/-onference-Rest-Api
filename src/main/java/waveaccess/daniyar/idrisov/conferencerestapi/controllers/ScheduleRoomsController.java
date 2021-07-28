package waveaccess.daniyar.idrisov.conferencerestapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.ExceptionDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.ScheduleDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.ScheduleRoomsDto;
import waveaccess.daniyar.idrisov.conferencerestapi.services.ScheduleService;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
public class ScheduleRoomsController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "Получение расписания разбитого на аудитории")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = ScheduleRoomsDto.class)})
    @PermitAll
    @GetMapping("/rooms/schedule")
    public ResponseEntity<List<ScheduleRoomsDto>> getSeparateScheduleForRooms() {
        return ResponseEntity.ok(scheduleService.getSeparateScheduleForRooms());
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
