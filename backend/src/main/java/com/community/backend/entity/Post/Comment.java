package com.community.backend.entity.Post;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Long userId; // 댓글 작성자 ID
    private String nickname; // 댓글 작성자 닉네임

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    //ManyToOne 여러 댓글이 하나의 게시글(Post)에 연결
    //@ManyToOne(fetch = FetchType.LAZY) 는
    //해당 엔티티를 조회할 때 연관된 엔티티를 즉시 불러오지 않고,
    //실제로 사용할 때(DB 접근 필요할 때) 그제서야 불러오도록 하는 설정입니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    //현재 테이블에 외래 키 컬럼으로 parent_id를 생성.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;


    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Comment> replies = new java.util.ArrayList<>();
}