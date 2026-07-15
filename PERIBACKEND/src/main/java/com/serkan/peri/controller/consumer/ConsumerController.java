package com.serkan.peri.controller.consumer;

import com.serkan.peri.dto.request.consumer.ConsumerReqDto;
import com.serkan.peri.dto.request.consumer.ContactMessageReqDto;
import com.serkan.peri.dto.request.consumer.QuoteRequestReqDto;
import com.serkan.peri.dto.response.BaseResponse;
import com.serkan.peri.dto.response.CompanyResDto;
import com.serkan.peri.entity.consumer.ContactMessage;
import com.serkan.peri.entity.consumer.QuoteRequest;
import com.serkan.peri.repositorty.consumer.ContactMessageRepository;
import com.serkan.peri.repositorty.consumer.QuoteRequestRepository;
import com.serkan.peri.service.company.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.serkan.peri.utility.ApiPaths.BUY;
import static com.serkan.peri.utility.ApiPaths.CONTACT;
import static com.serkan.peri.utility.ApiPaths.QUOTE;
import static com.serkan.peri.utility.ApiPaths.CONSUMER;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(CONSUMER)
public class ConsumerController {

    private final CompanyService companyService;
    private final ContactMessageRepository contactMessageRepository;
    private final QuoteRequestRepository quoteRequestRepository;

    @PostMapping(BUY)
    public ResponseEntity<BaseResponse<CompanyResDto>> buyApplication(@RequestBody @Valid ConsumerReqDto consumerReqDto) {
        try {
            if (companyService.isValidtaxNumber(consumerReqDto.taxNumber())) {
                companyService.saveCompany(consumerReqDto);
                return ResponseEntity.ok(BaseResponse.<CompanyResDto>builder()
                        .code(HttpStatus.OK.value())
                        .message("Satın alma işlemi Başarılı")
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.<CompanyResDto>builder()
                                .code(4000)
                                .message("Vergi Numarası Geçersiz")
                                .build());
            }
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.<CompanyResDto>builder()
                            .code(5000)
                            .message(exception.getMessage())
                            .build());
        }
    }
    @PostMapping(CONTACT)
    public ResponseEntity<BaseResponse<Void>> saveContactMessage(@RequestBody @Valid ContactMessageReqDto dto) {
        try {
            ContactMessage msg = new ContactMessage();
            msg.setFullName(dto.fullName());
            msg.setSenderEmail(dto.senderEmail());
            msg.setMessage(dto.message());
            contactMessageRepository.save(msg);
            return ResponseEntity.ok(BaseResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Mesajınız başarıyla iletildi.")
                    .build());
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.<Void>builder()
                            .code(5000)
                            .message(exception.getMessage())
                            .build());
        }
    }

    @PostMapping(QUOTE)
    public ResponseEntity<BaseResponse<Void>> saveQuoteRequest(@RequestBody @Valid QuoteRequestReqDto dto) {
        try {
            QuoteRequest q = new QuoteRequest();
            q.setFullName(dto.fullName());
            q.setEmail(dto.email());
            q.setPhone(dto.phone());
            q.setCompanyName(dto.companyName());
            q.setEmployeeCount(dto.employeeCount());
            q.setNotes(dto.notes());
            quoteRequestRepository.save(q);
            return ResponseEntity.ok(BaseResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Teklif talebiniz alındı, en kısa sürede sizinle iletişime geçeceğiz.")
                    .build());
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.<Void>builder()
                            .code(5000)
                            .message(exception.getMessage())
                            .build());
        }
    }
}
