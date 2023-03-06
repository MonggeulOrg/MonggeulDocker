package com.cmc.monggeul.domain.alert.dto;

import com.cmc.monggeul.domain.alert.entity.Alert;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetAlertRes {

    private Long alertId;
    private String senderName;
    private LocalDateTime createdAt;

    private String questionName;

    private Long diaryId;

    private Alert.MessageType messageType;
}
