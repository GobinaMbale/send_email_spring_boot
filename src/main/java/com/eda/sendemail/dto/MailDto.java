package com.eda.sendemail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.File;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
public class MailDto {
    private String recipientName;
    private String subject;
    private String from;
    private String to;
    private String message;
    private List<File> files;
    private boolean isEnabled;
}
