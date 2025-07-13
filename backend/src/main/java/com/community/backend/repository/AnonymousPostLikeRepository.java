package com.community.backend.repository;

import com.community.backend.entity.Anonymous.AnonymousPost;
import com.community.backend.entity.Anonymous.AnonymousPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnonymousPostLikeRepository extends JpaRepository<AnonymousPostLike, Long> {
    Optional<AnonymousPostLike> findByPostAndUserId(AnonymousPost post, Long userId);
    List<AnonymousPostLike> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}