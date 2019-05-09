package ru.itmo.wm4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wm4.domain.Comment;
import ru.itmo.wm4.domain.Notice;
import ru.itmo.wm4.domain.Role;
import ru.itmo.wm4.form.NoticeCredentials;
import ru.itmo.wm4.security.AnyRole;
import ru.itmo.wm4.security.Guest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class NoticeCommentsPage extends Page {

    @Guest
    @GetMapping(path = "/notice/{id:[\\d]{1,10}}")
    public String noticeCommentGet(Model model, @PathVariable long id) {
        Notice notice = getNoticeService().findById(id);
        if (notice != null) {
            model.addAttribute("notice", notice);
            model.addAttribute("commentForm", new Comment());
        } else {
            model.addAttribute("noticeError", "No such notice");
        }

        return "NoticeCommentsPage";
    }

    @Guest
    @PostMapping(path = "/notice/{id:[\\d]{1,10}}")
    public String noticeCommentPost(@Valid @ModelAttribute("commentForm") Comment comment,
                                    BindingResult bindingResult, HttpSession httpSession, Model model, @PathVariable long id) {
        Notice notice = getNoticeService().findById(id);
        if (notice == null) {
            model.addAttribute("noticeError", "No such notice");
            return "NoticeCommentsPage";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("notice", notice);
            return "NoticeCommentsPage";
        }

        getCommentService().save(comment, notice, getUser(httpSession));
        return "redirect:/notice/" + id;
    }
}
