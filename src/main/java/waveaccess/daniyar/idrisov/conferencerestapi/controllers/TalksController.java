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
import waveaccess.daniyar.idrisov.conferencerestapi.services.ScheduleService;
import waveaccess.daniyar.idrisov.conferencerestapi.services.TalksService;
import waveaccess.daniyar.idrisov.conferencerestapi.services.UsersService;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

@RestController
public class TalksController {

    @Autowired
    private TalksService talksService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "Получение докладов текущего пользователя")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = TalkDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @GetMapping("/user/talks")
    public ResponseEntity<List<TalkDto>> getTalksOfCurrentUser(@RequestHeader("Authorization") String token) {
        UserDto userDto = usersService.getUserByToken(token);
        return ResponseEntity.ok(talksService.getTalksByUserId(userDto.getId()));
    }

    @ApiOperation(value = "Получение всех докладов")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = TalkDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @GetMapping("/talks")
    public ResponseEntity<List<TalkDto>> getAllTalks() {
        return ResponseEntity.ok(talksService.getAllTalks());
    }

    @ApiOperation(value = "Создание доклада и опционально добавление в него других спикеров")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно создано", response = TalkDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @PostMapping("/talks")
    public ResponseEntity<TalkDto> addTalk(@RequestBody @Valid AddTalkForm addTalkForm, @RequestHeader("Authorization") String token) {
        UserDto userDto = usersService.getUserByToken(token);
        return ResponseEntity.ok(talksService.addTalk(addTalkForm, userDto));
    }

    @ApiOperation(value = "Получение доклада по определенному id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = TalkDto.class)})
    @PermitAll
    @GetMapping("/talks/{talk-id}")
    public ResponseEntity<TalkDto> getTalkById(@PathVariable("talk-id") Long talkId) {
        return ResponseEntity.ok(talksService.getTalkById(talkId));
    }

    @ApiOperation(value = "Изменение доклада по определенному id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно изменено", response = TalkDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @PatchMapping("/talks/{talk-id}")
    public ResponseEntity<TalkDto> updateTalkById(@PathVariable("talk-id") Long talkId, @RequestBody TalkDto talkDto, @RequestHeader("Authorization") String token) {
        UserDto userDto = usersService.getUserByToken(token);
        return ResponseEntity.ok(talksService.updateTalkById(talkId, talkDto, userDto));
    }

    @ApiOperation(value = "Удаление доклада по определенному id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно удалено", response = TalkDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @DeleteMapping("/talks/{talk-id}")
    @ResponseBody
    public ResponseEntity<TalkDto> deleteTalkById(@PathVariable("talk-id") Long talkId, @RequestHeader("Authorization") String token) {
        UserDto userDto = usersService.getUserByToken(token);
        return ResponseEntity.ok(talksService.deleteTalkById(talkId, userDto));
    }

    @ApiOperation(value = "Получение спикеров по определенному id доклада")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = UserDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @GetMapping("/talks/{talk-id}/users")
    public ResponseEntity<List<UserDto>> getSpeakersByTalkId(@PathVariable("talk-id") Long talkId) {
        return ResponseEntity.ok(usersService.getUsersByTalkId(talkId));
    }

    @ApiOperation(value = "Получение расписания по определенному id доклада")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = ScheduleDto.class)})
    @PermitAll
    @GetMapping("/talks/{talk-id}/schedule")
    public ResponseEntity<ScheduleDto> getScheduleByTalkId(@PathVariable("talk-id") Long talkId) {
        return ResponseEntity.ok(scheduleService.getScheduleByTalkId(talkId));
    }

    @ExceptionHandler({AddEntityException.class, DeleteEntityException.class, UsageException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionDto> talksExceptions(Exception exception) {
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(exception.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.badRequest().body(exceptionDto);
    }

}
