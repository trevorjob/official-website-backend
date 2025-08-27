package com.msc.contact.form.service;

import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.dto.WaitlistRequest;

public interface WaitlistService {
    public ResponseDto joinWaitlist(WaitlistRequest dto);
}
