package ru.itmo.wm4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wm4.domain.Notice;
import ru.itmo.wm4.service.NoticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AddNoticePage extends Page {
    private final NoticeService noticeService;

    public AddNoticePage(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping(path = "/addNotice")
    public String addNoticeGet(Model model, HttpSession httpSession) {
        if (getUser(httpSession) == null) {
            return "redirect:/";
        }
        model.addAttribute("notice", new Notice());
        return "AddNoticePage";
    }

    @PostMapping(path = "/addNotice")
    public String addNoticePost(@Valid @ModelAttribute("notice") Notice notice,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "AddNoticePage";
        }

        noticeService.add(notice);
        return "redirect:/";
    }
}
