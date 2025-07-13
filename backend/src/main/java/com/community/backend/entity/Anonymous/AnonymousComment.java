package com.community.backend.entity.Anonymous;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class AnonymousComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Long writerId; // 내부 식별용 (삭제 용도)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    //지연 로딩
    //해당 엔티티를 처음 조회할 때 연관된 Category를 바로 가져오는 것이 아니라,
    //실제로 접근할 때 (getter 호출 등) 쿼리가 실행되어 가져옵니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private AnonymousPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private AnonymousComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<AnonymousComment> children = new java.util.ArrayList<>();
}