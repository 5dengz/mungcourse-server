package com._dengz.mungcourse.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users") // SQL에서 예약어 충돌 방지
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sub;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String name;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = true)
    private String userImgUrl;

    @Lob
    @Column(name = "pkl_file", columnDefinition = "MEDIUMBLOB")  // MEDIUMBLOB으로 설정
    private byte[] pklFile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Dog> dogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Walk> walks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Routine> routines;

    private User(String sub, String email, String name, String userImgUrl, String provider, byte[] pklFile)
    {
        this.sub = sub;
        this.email = email;
        this.name = name;
        this.userImgUrl = userImgUrl;
        this.provider = provider;
        this.pklFile = pklFile;
    }

    public static User create(String sub, String email, String name, String userImgUrl, String provider, byte[] pklFile) {
        return new User(sub, email, name, userImgUrl, provider, pklFile);
    }

    public static User create(String sub, String email, String name, String provider, byte[] pklFile) {
        return new User(sub, email, name, null, provider, pklFile);
    }
}
