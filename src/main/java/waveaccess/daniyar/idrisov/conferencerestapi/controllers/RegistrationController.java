package waveaccess.daniyar.idrisov.conferencerestapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.ExceptionDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserRegistrationForm;
import waveaccess.daniyar.idrisov.conferencerestapi.exceptions.EmailExistsException;
import waveaccess.daniyar.idrisov.conferencerestapi.services.RegistrationService;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @ApiOperation(value = "Регистрация пользователя")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно зарегистрирован", response = UserDto.class)})
    @PermitAll
    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody @Valid UserRegistrationForm userRegistrationForm) {
        return ResponseEntity.ok(registrationService.registerUser(userRegistrationForm));
    }

    @ApiOperation(value = "Активация аккаунта")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно активирован", response = UserDto.class)})
    @PermitAll
    @GetMapping("/activation/{confirm-code}")
    public ResponseEntity<UserDto> activateAccount(@PathVariable("confirm-code") String confirmCode) {
        return ResponseEntity.ok(registrationService.activateAccount(confirmCode));
    }

    @ExceptionHandler({EmailExistsException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionDto> registrationExceptions(Exception exception) {
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(exception.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.badRequest().body(exceptionDto);
    }


}
