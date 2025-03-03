package net.mymenu.service;

import com.amazonaws.services.organizations.model.AccountOwnerNotVerifiedException;
import net.mymenu.enums.auth.EmailCodeType;
import net.mymenu.exception.*;
import net.mymenu.models.Company;
import net.mymenu.models.User;
import net.mymenu.models.auth.EmailCode;
import net.mymenu.repository.auth.EmailCodeRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailCodeService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private EmailCodeRepository emailCodeRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private JwtHelper jwtHelper;

    public void sendUserEmail() {
        User user = jwtHelper.extractUser();
        sendUserEmail(user);
    }

    public void sendUserEmail(User user) {
        if (user.isVerifiedEmail()) {
            throw new AccountAlreadyVerifiedException("Your account is already verified");
        }

        sendEmailCode(user, EmailCodeType.USER, user.getEmail());
    }

    public void sendUserEmailWithoutVerification(User user) {
        sendEmailCode(user, EmailCodeType.USER, user.getEmail());
    }

    public void sendCompanyEmail() {
        User user = jwtHelper.extractUser();
        Company company = user.getCompany();

        if (!user.isVerifiedEmail()) {
            throw new AccountOwnerNotVerifiedException("Your account is not verified");
        }
        if (company.isVerifiedEmail()) {
            throw new AccountAlreadyVerifiedException("Your company is already verified");
        }

        sendEmailCode(user, EmailCodeType.COMPANY, company.getEmail());
    }

    public boolean validateUserEmailCode(User user, String code) {
        return validateEmailCode(user, code, EmailCodeType.USER);
    }

    public boolean validateCompanyEmailCode(User user, String code) {
        return validateEmailCode(user, code, EmailCodeType.COMPANY);
    }

    private boolean validateEmailCode(User user, String code, EmailCodeType type) {
        EmailCode emailCode = emailCodeRepository.findAllByEmailAndTypeAndCode(user.getEmail(), type, code)
                .orElseThrow(() -> new NotFoundException("Email code not found"));

        emailCodeRepository.delete(emailCode);

        LocalDateTime now = LocalDateTime.now();

        if (emailCode.getCreatedAt().plusMinutes(5).isBefore(now)) {
            throw new EmailCodeExpiredException();
        }

        return true;
    }

    private void validateEmailCodeCooldown(User user, EmailCodeType type) {
        List<EmailCode> lastUserEmailCode = emailCodeRepository
                .findAllByEmailAndTypeOrderByCreatedAtDesc(user.getEmail(), type);

        LocalDateTime now = LocalDateTime.now();

        if (lastUserEmailCode != null
                && !lastUserEmailCode.isEmpty()
                && lastUserEmailCode.getFirst().getCreatedAt().plusMinutes(1).isAfter(now)) {
            throw new EmailCodeRequestTooSoonException("Wait until 1 minute to send other code");
        }
    }

    private void sendEmailCode(User user, EmailCodeType type, String toEmail) {
        validateEmailCodeCooldown(user, type);

        EmailCode emailCode = createEmailCode(user, type);

        Context context = new Context();
        context.setVariable("code", emailCode.getCode());

        // TODO: Change this email to the user email
        emailSenderService.sendEmail(
                "thiago@my-menu.net",
                "MyMenu - Verifique sua conta",
                "email-verification",
                context);
    }

    private EmailCode createEmailCode(User user, EmailCodeType type) {
        EmailCode emailCode = EmailCode.builder()
                .code(generateRandomCode())
                .email(user.getEmail())
                .type(type)
                .build();

        emailCodeRepository.save(emailCode);

        return emailCode;
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
