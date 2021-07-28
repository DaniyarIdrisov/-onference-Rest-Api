package waveaccess.daniyar.idrisov.conferencerestapi.utils;

public interface MailSender {

    void sendMail(String emailTo, String subject, String text);

}
