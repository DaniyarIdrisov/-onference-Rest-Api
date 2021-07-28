package waveaccess.daniyar.idrisov.conferencerestapi.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserDto;
import waveaccess.daniyar.idrisov.conferencerestapi.dto.UserRegistrationForm;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;

    @Test
    public void registerUser() {
        UserRegistrationForm userRegistrationForm = UserRegistrationForm.builder()
                .email("email.this@mail.ru")
                .firstName("J")
                .lastName("Cole")
                .password("qwerty123")
                .organization("World")
                .build();
        UserDto userDto = registrationService.registerUser(userRegistrationForm);
        Assert.assertNotNull(userDto.getEmail());
        Assert.assertSame("LISTENER", userDto.getRole().toString());
        Assert.assertSame("ACTIVE", userDto.getState().toString());
        Assert.assertNotNull(userDto.getConfirmCode());
    }

}
