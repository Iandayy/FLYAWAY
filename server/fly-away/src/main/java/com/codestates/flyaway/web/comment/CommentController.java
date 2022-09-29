package com.codestates.flyaway.web.comment;

import com.codestates.flyaway.domain.comment.entity.Comment;
import com.codestates.flyaway.domain.comment.service.CommentService;
import com.codestates.flyaway.global.dto.MultiResponseDto;
import com.codestates.flyaway.global.dto.SingleResponseDto;
import com.codestates.flyaway.web.comment.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto.CommentResponseDto writeComment(@PathVariable("boardId") Long boardId,
                                       @RequestBody CommentDto.Write writeDto) {

        writeDto.setBoardId(boardId);

        return commentService.write(writeDto);
    }

    @PatchMapping("/{boardId}/comment/{commentId}")
    public CommentDto.CommentResponseDto updateComment(@PathVariable("boardId") Long boardId,
                                        @PathVariable("commentId") Long commentId,
                                        @RequestBody CommentDto.Update updateDto) {

        updateDto.setCommentId(commentId);

        return commentService.update(updateDto);
    }

    @GetMapping("/{boardId}/comment")
    public MultiResponseDto readComment(@PathVariable("boardId") Long boardId,
                                         @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                         Pageable pageable) {

        Page<Comment> comments = commentService.readByBoardId(boardId, pageable);
        List<Comment> commentList = comments.getContent();
        List<CommentDto.MultiCommentDto> responses = CommentDto.MultiCommentDto.toResponsesDto(commentList);

        return new MultiResponseDto<>(responses, comments);
    }

    @DeleteMapping("/{boardId}/comment/{commentId}")
    public HttpStatus deleteComment(@PathVariable("commentId") Long commentId) {

        commentService.delete(commentId);

        return HttpStatus.NO_CONTENT;
    }
}
