package ru.itmo.wm4.service;

import org.springframework.stereotype.Service;
import ru.itmo.wm4.domain.Notice;
import ru.itmo.wm4.domain.User;
import ru.itmo.wm4.repository.NoticeRepository;

import java.util.List;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<Notice> findByOrderByCreationTimeDesc() {
        return noticeRepository.findByOrderByCreationTimeDesc();
    }

    public Notice findById(Long noticeId) {
        return noticeId == null ? null : noticeRepository.findById(noticeId).orElse(null);
    }

    public void save(Notice notice, User user) {
        user.addNotice(notice);
        notice.setUser(user);
        noticeRepository.save(notice);
    }
}
