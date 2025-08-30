package com.msc.contact.form.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminStatsResponse {
    private long totalContacts;
    private long unreadContacts;
    private long readContacts;
    private long recentContacts;
    private long totalWaitlist;
    private long recentWaitlist;
}
