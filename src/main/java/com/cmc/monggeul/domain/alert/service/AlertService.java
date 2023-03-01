package com.cmc.monggeul.domain.alert.service;

import com.cmc.monggeul.domain.alert.dto.GetAlertRes;
import com.cmc.monggeul.domain.alert.entity.Alert;
import com.cmc.monggeul.domain.alert.repository.AlertRepository;
import com.cmc.monggeul.domain.diary.entity.Diary;
import com.cmc.monggeul.domain.diary.repository.DiaryRepository;
import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.domain.user.repository.UserRepository;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    private final AlertRepository alertRepository;
    private final String MOM="MOM";
    private final String DAD="DAD";
    private final String SON="SON";

    private final String DAU="DAUGHTER";

    public List<GetAlertRes> getAlert(String userEmail){
        Optional<User> user=userRepository.findByEmail(userEmail);
        String role=user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getRole().getRoleCode();

        checkNotResponseDiary(user,role);

        List<Alert>alertList=alertRepository.findUserAlertRecent(user.get().getId());

        List<GetAlertRes> getAlertResList=alertList.stream().map(
                alert -> GetAlertRes.builder()
                        .diaryId(alert.getDiary().getId())
                        .senderName(alert.getSender().getName())
                        .questionName(alert.getDiary().getQuestion().getName())
                        .messageType(alert.getMessageType())
                        .createdAt(alert.getCreatedAt())
                        .build()
        ).collect(Collectors.toList());
        return getAlertResList;


    }

    // 작성을 기다리는 글 처리 (createdAt 기준으로 5일이 넘어간 글일 경우)
    public void checkNotResponseDiary(Optional<User> user,String role){

        if(role.equals(MOM)||role.equals(DAD)){
            List<Diary>diaryList=diaryRepository.parentNotResponseMoreThanFive(user.get().getId());

            for(Diary diary:diaryList){
                Alert alert=alertRepository.findExistWaitAlert(diary.getFamily().getParent().getId(),diary.getId());

                if(alert==null){
                    alertRepository.save(Alert.builder()
                            .isRead(0)
                            .sender(diary.getFamily().getChild())
                            .user(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                            .messageType(Alert.MessageType.WAIT)
                            .diary(diary)
                            .family(diary.getFamily())
                            .build());

                }

            }


        }else if(role.equals(DAU)||role.equals(SON)){
            List<Diary>diaryList=diaryRepository.childNotResponseMoreThanFive(user.get().getId());
            for(Diary diary:diaryList){
                Alert alert=alertRepository.findExistWaitAlert(diary.getFamily().getChild().getId(),diary.getId());

                if(alert==null){
                    alertRepository.save(Alert.builder()
                            .isRead(0)
                            .sender(diary.getFamily().getParent())
                            .user(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                            .messageType(Alert.MessageType.WAIT)
                            .diary(diary)
                            .family(diary.getFamily())
                            .build());

                }

            }
        }

    }
}
