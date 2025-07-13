package com.community.backend.repository;

import com.community.backend.entity.Anonymous.AnonymousComment;
import com.community.backend.entity.Anonymous.AnonymousPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnonymousCommentRepository extends JpaRepository<AnonymousComment, Long> {
    List<AnonymousComment> findByWriterId(Long writerId);
    List<AnonymousComment> findByPostOrderByCreatedAtAsc(AnonymousPost post);
    void deleteByWriterId(Long userId);
}