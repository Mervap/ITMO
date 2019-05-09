package hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hello.domain.User;
import hello.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/1")
public class UserController extends ApiController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users/authenticated")
    public User getAuthenticatedUser(User user) {
        return user;
    }

    @GetMapping("users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }
}
