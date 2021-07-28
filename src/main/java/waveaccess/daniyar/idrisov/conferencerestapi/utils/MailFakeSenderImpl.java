package waveaccess.daniyar.idrisov.conferencerestapi.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class MailFakeSenderImpl implements MailSender{

    @Override
    public void sendMail(String emailTo, String subject, String text) {
        System.out.println(text);
    }
}
