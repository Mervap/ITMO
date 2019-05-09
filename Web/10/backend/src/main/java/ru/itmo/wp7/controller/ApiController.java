package hello.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import hello.domain.User;

import javax.servlet.http.HttpServletRequest;

public class ApiController {
    @ModelAttribute
    public User getUser(HttpServletRequest httpServletRequest) {
        return (User) httpServletRequest.getAttribute("user");
    }
}
