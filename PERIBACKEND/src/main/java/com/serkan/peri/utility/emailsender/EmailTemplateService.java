package com.serkan.peri.utility.emailsender;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {


    public String generateContactTemplate(String senderName, String senderEmail, String userMessage) {
        return String.format(
            "<div style='font-family:sans-serif; max-width:600px; margin:auto; border:1px solid #eee; padding:20px;'>" +
                "<h2 style='color:#00796B;'>Yeni İletişim Formu Mesajı</h2>" +
                "<p><b>Gönderen:</b> %s</p>" +
                "<p><b>E-Posta:</b> <a href='mailto:%s'>%s</a></p>" +
                "<hr style='border:none; border-top:1px solid #eee; margin:16px 0;'/>" +
                "<p><b>Mesaj:</b></p>" +
                "<p style='background:#f9f9f9; padding:12px; border-radius:6px; white-space:pre-wrap;'>%s</p>" +
                "<p style='font-size:12px; color:#999; margin-top:20px;'>Bu mesaj PERI İK Landing sayfası iletişim formundan gönderilmiştir.</p>" +
            "</div>",
            escapeHtml(senderName), escapeHtml(senderEmail), escapeHtml(senderEmail), escapeHtml(userMessage)
        );
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    public String generateActionTemplate(String name, String title, String message, String link, String btnText) {
        return String.format(
            "<div style='font-family:sans-serif; max-width:600px; margin:auto; border:1px solid #eee; padding:20px;'>" +
                "<h2 style='color:#00796B;'>%s</h2>" +
                "<p>Merhaba <b>%s</b>,</p>" +
                "<p>%s</p>" +
                "<div style='text-align:center; margin:30px 0;'>" +
                    "<a href='%s' style='background:#00796B; color:white; padding:12px 25px; text-decoration:none; border-radius:5px; font-weight:bold;'>%s</a>" +
                "</div>" +
                "<p style='font-size:12px; color:#999;'>Eğer bu işlemi siz yapmadıysanız, lütfen bu e-postayı dikkate almayınız.</p>" +
            "</div>", 
            title, name, message, link, btnText
        );
    }
}