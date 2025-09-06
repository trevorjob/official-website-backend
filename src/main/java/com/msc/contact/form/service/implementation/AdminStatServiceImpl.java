package com.msc.contact.form.service.implementation;

import com.msc.contact.form.dto.AdminStatsResponse;
import com.msc.contact.form.repository.ContactRepository;
import com.msc.contact.form.repository.WaitlistRepository;
import com.msc.contact.form.service.AdminStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminStatServiceImpl implements AdminStatService {

    private final ContactRepository contactRepository;
    private final WaitlistRepository waitlistRepository;

    @Override
    public AdminStatsResponse getAdminStats() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // Use soft delete aware methods - count only active (non-deleted) records
        long totalContacts = contactRepository.findAllActive().size();
        long unreadContacts = contactRepository.countByIsReadFalse();
        long readContacts = contactRepository.countByIsReadTrue();
        long recentContacts = contactRepository.countByCreatedAtGreaterThanEqual(sevenDaysAgo);

        long totalWaitlist = waitlistRepository.findAllActive().size();
        long recentWaitlist = waitlistRepository.countByCreatedAtGreaterThanEqual(sevenDaysAgo);

        return AdminStatsResponse.builder()
                .totalContacts(totalContacts)
                .unreadContacts(unreadContacts)
                .readContacts(readContacts)
                .recentContacts(recentContacts)
                .totalWaitlist(totalWaitlist)
                .recentWaitlist(recentWaitlist)
                .build();
    }
}
