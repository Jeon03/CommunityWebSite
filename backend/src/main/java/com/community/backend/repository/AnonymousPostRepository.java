package com.community.backend.repository;

import com.community.backend.entity.Anonymous.AnonymousPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnonymousPostRepository extends JpaRepository<AnonymousPost, Long> {
    List<AnonymousPost> findAllByOrderByUpdatedAtDescCreatedAtDesc();
    List<AnonymousPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
    List<AnonymousPost> findByWriterId(Long writerId);
    void deleteByWriterId(Long userId);
}
