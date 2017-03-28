package com.notification.service;

import com.notification.domain.NotificationType;
import com.notification.domain.Recipient;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    void send(NotificationType type, Recipient recipient, String attachment) throws MessagingException, IOException;

}
