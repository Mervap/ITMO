package ru.itmo.wm4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wm4.domain.Notice;
import ru.itmo.wm4.domain.Role;
import ru.itmo.wm4.form.NoticeCredentials;
import ru.itmo.wm4.security.AnyRole;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class NoticePage extends Page {
    @AnyRole(Role.Name.ADMIN)
    @GetMapping(path = "/notice")
    public String noticeGet(Model model) {
        model.addAttribute("notice", new NoticeCredentials());
        return "NoticePage";
    }

    @AnyRole(Role.Name.ADMIN)
    @PostMapping(path = "/notice")
    public String noticePost(@Valid @ModelAttribute("notice") NoticeCredentials noticeCredentials,
                             BindingResult bindingResult, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "NoticePage";
        }

        Notice notice = new Notice();
        notice.setText(noticeCredentials.getText());
        String[] tags = noticeCredentials.getTags().split("[\\s]+");
        for (String tag : tags) {
            if (!tag.isEmpty()) {
                getTagService().save(tag, notice);
            }
        }
        getNoticeService().save(notice, getUser(httpSession));
        return "redirect:/notices";
    }
}
