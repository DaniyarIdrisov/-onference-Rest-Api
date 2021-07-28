package waveaccess.daniyar.idrisov.conferencerestapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.AddUserForm;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.ExceptionDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.TalkDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.AddEntityException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.DeleteEntityException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.EmailExistsException;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.UsageException;
import waveaccess.daniyar.idrisov.conferencerestapi.services.TalksService;
import waveaccess.daniyar.idrisov.conferencerestapi.services.UsersService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private TalksService talksService;

    @ApiOperation(value = "Получение текущего пользователя")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = UserDto.class)})
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/token/user")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usersService.getUserByToken(token));
    }

    @ApiOperation(value = "Получение всех пользователй")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = UserDto.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @ApiOperation(value = "Добавление пользователя")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно добавлено", response = UserDto.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid AddUserForm addUserForm) {
        return ResponseEntity.ok(usersService.addUser(addUserForm));
    }
    @ApiOperation(value = "Получение пользователя по определенному id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = UserDto.class)})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/users/{user-id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("user-id") Long userId) {
        return ResponseEntity.ok(usersService.getUserById(userId));
    }

    @ApiOperation(value = "Изменение пользователя по определенному id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно изменено", response = UserDto.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/users/{user-id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable("user-id") Long userId, @RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(usersService.updateUserById(userId, userDto));
    }

    @ApiOperation(value = "Удаление пользователя по определенному id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно удалено", response = UserDto.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/{user-id}")
    @ResponseBody
    public ResponseEntity<UserDto> deleteUserById(@PathVariable("user-id") Long userId, @RequestHeader("Authorization") String token) {
        UserDto userDto = usersService.getUserByToken(token);
        return ResponseEntity.ok(usersService.deleteUserById(userId, userDto));
    }

    @ApiOperation(value = "Получение докладов по определенному id спикера")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = TalkDto.class)})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SPEAKER')")
    @GetMapping("/users/{user-id}/talks")
    public ResponseEntity<List<TalkDto>> getTalksByUserId(@PathVariable("user-id") Long userId) {
        return ResponseEntity.ok(talksService.getTalksByUserId(userId));
    }
    @ApiOperation(value = "Получение пользователей по определенной роли")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно получено", response = UserDto.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/role")
    public ResponseEntity<List<UserDto>> getUsersByRole(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(usersService.getUsersByRole(userDto.getRole()));
    }

    @ExceptionHandler({AddEntityException.class, DeleteEntityException.class, UsageException.class, EmailExistsException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionDto> usersExceptions(Exception exception) {
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(exception.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.badRequest().body(exceptionDto);
    }

}
