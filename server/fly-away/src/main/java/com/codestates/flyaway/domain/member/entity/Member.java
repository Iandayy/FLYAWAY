package com.codestates.flyaway.domain.member.entity;

import com.codestates.flyaway.domain.board.entity.Board;
import com.codestates.flyaway.domain.comment.entity.Comment;
import com.codestates.flyaway.domain.member.util.PasswordConverter;
import com.codestates.flyaway.domain.memberimage.MemberImage;
import com.codestates.flyaway.domain.record.entity.Record;
import com.codestates.flyaway.global.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Member extends Auditable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private MemberImage memberImage;

    @OneToMany(mappedBy = "member", cascade = REMOVE)
    private List<Record> records = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    private String name;
    private String email;

    @Convert(converter = PasswordConverter.class)
    private String password;

    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setMemberImage(MemberImage memberImage) {
        this.memberImage = memberImage;
    }


    //==================


    public void update(String name, String password) {
        Optional.ofNullable(name)
                .ifPresent(n -> this.name = n);
        Optional.ofNullable(password)
                .ifPresent(p -> this.password = p);
    }
}
