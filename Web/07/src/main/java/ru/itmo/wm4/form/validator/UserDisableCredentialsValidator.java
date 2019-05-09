package ru.itmo.wm4.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wm4.domain.User;
import ru.itmo.wm4.form.UserDisableCredentials;
import ru.itmo.wm4.service.UserService;

@Component
public class UserDisableCredentialsValidator implements Validator {
    private final UserService userService;

    public UserDisableCredentialsValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDisableCredentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            UserDisableCredentials disableEnableForm = (UserDisableCredentials) target;
            User user = userService.findById(disableEnableForm.getTargetId());
            if (user == null) {
                errors.rejectValue("disable", "cant.find.target.user", "Can't find target user");
            }

            if (user != null && (user.getId() == disableEnableForm.getSourceId())) {
                errors.rejectValue("disable", "you.cant.disable.yourself", "You can't disable yourself");
            }
        }
    }
}
