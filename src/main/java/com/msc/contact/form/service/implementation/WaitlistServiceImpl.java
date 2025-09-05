package com.msc.contact.form.service.implementation;

import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.dto.WaitlistRequest;
import com.msc.contact.form.dto.WaitlistResponseDto;
import com.msc.contact.form.exception.DuplicateEmailException;
import com.msc.contact.form.exception.ResourceNotFoundException;
import com.msc.contact.form.model.Waitlist;
import com.msc.contact.form.repository.WaitlistRepository;
import com.msc.contact.form.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitlistServiceImpl implements WaitlistService {

    private final WaitlistRepository waitlistRepository;


    @Override
    public ResponseDto joinWaitlist(WaitlistRequest dto) {
        waitlistRepository.findByEmail(dto.getEmail()).ifPresent(w -> {
            throw new DuplicateEmailException("Email already exists in waitlist");
        });

        Waitlist waitlist = Waitlist.builder()
                .email(dto.getEmail())
                .build();

        Waitlist saved = waitlistRepository.save(waitlist);

        return ResponseDto.builder()
                .id(saved.getId())
                .message("Successfully joined the waitlist")
                .build();
    }

    @Override
    public Page<WaitlistResponseDto> getWaitlists(Pageable pageable) {
        return waitlistRepository.findAll(pageable)
                .map(this::convertResponseDto);
    }

    @Override
    public void deleteWaitlist(Long id) {
        Waitlist waitlist = waitlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waitlist entry not found with id: " + id));
        waitlistRepository.delete(waitlist);
    }

    private WaitlistResponseDto convertResponseDto(Waitlist waitlist) {
        return WaitlistResponseDto.builder()
                .id(waitlist.getId())
                .email(waitlist.getEmail())
                .createdAt(waitlist.getCreatedAt())
                .build();
    }

}
