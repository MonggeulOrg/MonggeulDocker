package com.cmc.monggeul.domain.user.entity;

import com.cmc.monggeul.domain.alert.entity.Alert;
import com.cmc.monggeul.domain.community.entity.Article;
import com.cmc.monggeul.domain.diary.entity.UserQuestionMapping;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    private int age;

    private String matchingCode;

    @Enumerated(value = EnumType.STRING)
    private OAuthType oAuthType;


    //[Matching]
    @OneToMany(mappedBy = "child")
    private List<Family> children=new ArrayList<>();

    @OneToMany(mappedBy = "parent")
    private List<Family> parents=new ArrayList<>();

    //[Role]

    // 1(유저) : 1(role)  1(role):N(유저)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roleCode")
    private Role role;

    //[UserQuestion]
    @OneToMany(mappedBy = "user")
    private List<UserQuestionMapping> userQuestionMappings=new ArrayList<>();



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

    public User() {
        
    }


    public enum OAuthType{

        GOOGLE,
        KAKAO,
        APPLE
    }


    @Builder

    public User(String name,String email,String profileImgUrl,Role role,int age,OAuthType oAuthType,String matchingCode) {
        this.name=name;
        this.email=email;
        this.profileImgUrl=profileImgUrl;
        this.role=role;
        this.age=age;
        this.oAuthType=oAuthType;
        this.matchingCode=matchingCode;

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
        return name;
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

    public void updateName(String name){
        this.name=name;
    }

    public void updateProfileImg(String profileImgUrl){
        this.profileImgUrl=profileImgUrl;
    }

    public void updateAge(int age){
        this.age=age;
    }


}
