package com.cmc.monggeul.domain.user.entity;

import com.cmc.monggeul.domain.alert.entity.Alert;
import com.cmc.monggeul.domain.community.entity.Article;
import com.cmc.monggeul.domain.diary.entity.Diary;
import com.cmc.monggeul.domain.diary.entity.UserDiarySharing;
import com.cmc.monggeul.domain.diary.entity.UserQuestionMapping;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
public class User extends BaseEntity implements UserDetails  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String profileImgUrl;

    @Enumerated(value = EnumType.STRING)
    private Age age;

    private String matchingCode;

    @Enumerated(value = EnumType.STRING)
    private OAuthType oAuthType;


    //[Matching]
    @OneToMany(mappedBy = "user")
    private List<Matching> users=new ArrayList<>();

    @OneToMany(mappedBy = "matchingUser")
    private List<Matching> matchingUsers=new ArrayList<>();

    //[Role]

    // 1(유저) : 1(role)  1(role):N(유저)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="roleCode")
    private Role role;

    //[UserQuestion]
    @OneToMany(mappedBy = "user")
    private List<UserQuestionMapping> userQuestionMappings=new ArrayList<>();

    //[Diary]
    // 1(user) : N(sharing)
    @OneToMany(mappedBy = "user")
    private List<UserDiarySharing> userDiarySharingList=new ArrayList<>();

    //[Alert]
    @OneToMany(mappedBy = "user")
    private List<Alert> userAlerts=new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Alert> senderAlerts=new ArrayList<>();

    //[Article]
    @OneToMany(mappedBy = "writer")
    private List<Article> writerList=new ArrayList<>();

    @OneToMany(mappedBy = "reviewer")
    private List<Article> reviewerList=new ArrayList<>();



    public enum OAuthType{

        GOOGLE,
        KAKAO,
        APPLE
    }


    public enum Age{

        TEN,
        TWENTY,
        THIRTY,
        FOURTY,
        FIFTY,
        SIXTY
    }

    public User() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
