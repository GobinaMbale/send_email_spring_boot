package com.eda.sendemail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
public class MimeMessageHelperDto {
    private String to;
    private String subject;
    private String from;
    private String text;
    private Boolean isUseHtml;
    private Boolean textIsUse;
}
