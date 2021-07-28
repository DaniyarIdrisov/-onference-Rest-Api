package waveaccess.daniyar.idrisov.conferencerestapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.ExceptionDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.TalkDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.AddEntityException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.DeleteEntityException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.UsageException;
import waveaccess.daniyar.idrisov.conferencerestapi.models.User;
import waveaccess.daniyar.idrisov.conferencerestapi.services.TalksService;
import waveaccess.daniyar.idrisov.conferencerestapi.services.UsersService;

import java.util.List;

@RestController
public class TalkSpeakersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private TalksService talksService;

    @ApiOperation(value = "Получение всех спикеров")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = UserDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @GetMapping("/speakers")
    public ResponseEntity<List<UserDto>> getAllSpeakers() {
        return ResponseEntity.ok(usersService.getUsersByRole(User.Role.SPEAKER));
    }

    @ApiOperation(value = "Добавление спикера в доклад")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно добавлено", response = TalkDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @PostMapping("/talks/{talk-id}/users/{user-id}")
    public ResponseEntity<TalkDto> addSpeakerToTalk(@PathVariable("talk-id") Long talkId, @PathVariable("user-id") Long userId, @RequestHeader("Authorization") String token) {
        UserDto userDto = usersService.getUserByToken(token);
        return ResponseEntity.ok(talksService.addSpeakerToTalk(talkId, userId, userDto));
    }

    @ApiOperation(value = "Удаление спикера из доклада")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно удалено", response = TalkDto.class)})
    @PreAuthorize("hasAuthority('SPEAKER')")
    @DeleteMapping("/talks/{talk-id}/users/{user-id}")
    @ResponseBody
    public ResponseEntity<TalkDto> deleteSpeakerFromTalk(@PathVariable("talk-id") Long talkId, @PathVariable("user-id") Long userId, @RequestHeader("Authorization") String token) {
        UserDto userDto = usersService.getUserByToken(token);
        return ResponseEntity.ok(talksService.deleteSpeakerIntoTalk(talkId, userId, userDto));
    }

    @ExceptionHandler({AddEntityException.class, DeleteEntityException.class, UsageException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionDto> talkSpeakersExceptions(Exception exception) {
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(exception.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.badRequest().body(exceptionDto);
    }

}
