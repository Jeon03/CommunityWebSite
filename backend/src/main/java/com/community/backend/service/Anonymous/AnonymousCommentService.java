package com.community.backend.service.Anonymous;


import com.community.backend.dto.Anonymous.AnonymousCommentRequest;
import com.community.backend.dto.Anonymous.AnonymousCommentResponse;
import com.community.backend.entity.Anonymous.AnonymousComment;
import com.community.backend.entity.Anonymous.AnonymousPost;
import com.community.backend.repository.AnonymousCommentRepository;
import com.community.backend.repository.AnonymousPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class AnonymousCommentService {

    private final AnonymousCommentRepository commentRepository;
    private final AnonymousPostRepository postRepository;

    @Transactional
    public AnonymousComment createComment(AnonymousCommentRequest request, Long userId) {
        AnonymousPost post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        AnonymousComment comment = new AnonymousComment();
        comment.setContent(request.getContent());
        comment.setWriterId(userId);
        comment.setPost(post);

        if (request.getParentId() != null) {
            AnonymousComment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글이 존재하지 않습니다."));
            comment.setParent(parent);
        }

        return commentRepository.save(comment);
    }


    public List<AnonymousCommentResponse> getCommentDtosByPost(AnonymousPost post) {
        //해당게시물 모든 댓글조회
        List<AnonymousComment> comments = commentRepository.findByPostOrderByCreatedAtAsc(post);

        //익명이름을 사용자별로 매핑할 Map과 번호카운터 생성
        Map<Long, String> anonymousMap = new HashMap<>();
        AtomicInteger counter = new AtomicInteger(1);
        
        List<AnonymousCommentResponse> dtoList = new ArrayList<>();
        
        //각댓글에 대한 익명이름 부여 및 DTO 반환
        for (AnonymousComment comment : comments) {
            Long writerId = comment.getWriterId();
            String anonName = anonymousMap.computeIfAbsent(writerId, id -> "익명" + counter.getAndIncrement());

            //dto객체로 변환하여 리스트에 추가
            dtoList.add(AnonymousCommentResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .anonymousName(anonName)
                    .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                    .createdAt(comment.getCreatedAt())
                    .userId(writerId)
                    .build());
        }
        return dtoList;
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        AnonymousComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        if (!comment.getWriterId().equals(userId)) {
            throw new IllegalArgumentException("본인만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }


}