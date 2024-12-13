package net.mymenu.service;

import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import net.mymenu.enums.auth.EmailCodeType;
import net.mymenu.exception.*;
import net.mymenu.models.Company;
import net.mymenu.models.User;
import net.mymenu.models.auth.EmailCode;
import net.mymenu.repository.auth.EmailCodeRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${sendgrid.dynamic_template_id.code}")
    private String codeTemplateId;

    public void sendUserEmail() {
        User user = jwtHelper.extractUser();

        if (user.isVerifiedEmail()) {
            throw new AccountAlreadyVerifiedException("Your account is already verified");
        }

        sendEmailCode(user, EmailCodeType.USER, user.getEmail());
    }

    public void sendCompanyEmail() {
        User user = jwtHelper.extractUser();
        Company company = user.getCompanies().getFirst();

        if (!user.isVerifiedEmail()) {
            throw new AccountNotVerifiedException("Your account is not verified");
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
        List<EmailCode> emailCodeList = emailCodeRepository.findAllByUserIdAndType(user.getId(), type)
                .orElseThrow(() -> new NotFoundException("Email code not found"));

        emailCodeRepository.deleteAll(emailCodeList);

        EmailCode emailCode = emailCodeList.stream()
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(EmailCodeInvalidException::new);

        LocalDateTime now = LocalDateTime.now();

        if (emailCode.getCreatedAt().plusMinutes(5).isBefore(now)) {
            throw new EmailCodeExpiredException();
        }

        return true;
    }

    private void validateEmailCodeCooldown(User user, EmailCodeType type) {
        List<EmailCode> lastUserEmailCode = emailCodeRepository
                .findByUserIdAndTypeOrderByCreatedAtDesc(user.getId(), type)
                .orElse(null);

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
        Personalization personalization = new Personalization();

        personalization.addDynamicTemplateData("name", user.getName().split(" ")[0]);
        personalization.addDynamicTemplateData("code", emailCode.getCode());
        personalization.addTo(new Email(toEmail));

        emailSenderService.sendEmail(personalization, codeTemplateId);
    }

    private EmailCode createEmailCode(User user, EmailCodeType type) {
        EmailCode emailCode = EmailCode.builder()
                .code(generateRandomCode())
                .userId(user.getId())
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
