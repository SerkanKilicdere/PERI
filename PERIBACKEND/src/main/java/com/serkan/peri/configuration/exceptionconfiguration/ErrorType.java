package com.serkan.peri.configuration.exceptionconfiguration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor

public enum ErrorType {

    KULLANICI_ADI_YADA_PAROLA_HATALI(90001, "Kullanıcı adı ya da parola hatalıdır", UNAUTHORIZED),
    UYGULAMA_ÇALIŞMA_HATASI(90011,"Uygulama çalışırken beklenmeyen bir hata oluştu", SERVICE_UNAVAILABLE),
    PARAMETRE_HATASI(90021,"Girilen değerler hatalıdır",FORBIDDEN),
    FILE_UPLOAD_ERROR(90031,"Dosya yüklenemedi", INTERNAL_SERVER_ERROR),
    FILE_SIZE_TOO_LARGE(90032,"Dosya boyutunu küçültünüz", BAD_REQUEST),
    INVALID_FILE_TYPE(90033,"Dosya formatı desteklenmiyor", UNAUTHORIZED),
    INVALID_FILE_NAME(90034,"Dosya ismi hatalı",FORBIDDEN),
    INVALID_FILE_EXTENSION(90035,"Dosya uzantısı desteklenmiyor",UNAUTHORIZED),
    USER_NOT_FOUND( 90041, "Kullanıcı bulunamadı", NOT_FOUND),
    INVALID_USER_CATEGORY(90051, "Geçersiz kullanıcı kategorisi seçildi", BAD_REQUEST),
    PERMISSION_DENIED(90061, "Kullanıcı limiti aşıldı", BAD_REQUEST);


    int applicationErrorCode;
    String errorMessage;
    HttpStatus httpStatusCode;
}
