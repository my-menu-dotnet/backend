package net.mymenu.service;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import net.mymenu.exception.AccountAlreadyVerifiedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailCodeService {

    private static final Logger logger = LoggerFactory.getLogger(EmailCodeService.class);

    public boolean validateEmailCode(String code, String toEmail) {
        logger.info("Validating email code: " + code + " for: " + toEmail);

        VerificationCheck check = VerificationCheck.creator("VAc9dc602b62e1ee98159837f2af5a1d3e")
                .setTo(toEmail)
                .setCode(code)
                .create();
        return check.getValid();
    }

    public void sendEmailCode(String toEmail) {
        logger.info("Sending email code to: " + toEmail);

        Verification verification = Verification
                .creator("VAc9dc602b62e1ee98159837f2af5a1d3e", toEmail, "email")
                .create();

        if (verification.getStatus().equals("approved")) {
            throw new AccountAlreadyVerifiedException("Account already verified");
        }
        if (!verification.getStatus().equals("pending")) {
            throw new RuntimeException("Failed to send email code");
        }
    }
}
