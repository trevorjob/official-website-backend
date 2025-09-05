package com.msc.contact.form.service;

import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.dto.WaitlistRequest;
import com.msc.contact.form.dto.WaitlistResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WaitlistService {
    public ResponseDto joinWaitlist(WaitlistRequest dto);
    Page<WaitlistResponseDto>getWaitlists(Pageable pageable);
    void deleteWaitlist(Long id);
}
