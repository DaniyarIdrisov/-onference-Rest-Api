package waveaccess.daniyar.idrisov.conferencerestapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.ExceptionDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.TokenDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserLoginForm;
import waveaccess.daniyar.idrisov.conferencerestapi.services.LoginService;

import javax.annotation.security.PermitAll;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "Авторизация пользователя и получение токена, для дальнейшего доступа к запросам требующиим права доступа ADMIN или SPEAKER")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Успешно авторизирован", response = TokenDto.class)})
    @PermitAll
    @PostMapping("/login")
    private ResponseEntity<TokenDto> login(@RequestBody UserLoginForm userLoginForm) {
        return ResponseEntity.ok(loginService.login(userLoginForm));
    }

    @ExceptionHandler({UsernameNotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionDto> loginExceptions(Exception exception) {
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(exception.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.badRequest().body(exceptionDto);
    }


}
