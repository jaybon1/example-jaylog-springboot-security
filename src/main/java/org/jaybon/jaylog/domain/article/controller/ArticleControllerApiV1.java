package org.jaybon.jaylog.domain.article.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jaybon.jaylog.common.constants.Constants;
import org.jaybon.jaylog.common.dto.ResDTO;
import org.jaybon.jaylog.config.security.auth.CustomUserDetails;
import org.jaybon.jaylog.domain.article.dto.req.ReqArticlePostDTOApiV1;
import org.jaybon.jaylog.domain.article.dto.req.ReqArticlePutDTOApiV1;
import org.jaybon.jaylog.domain.article.dto.res.ResArticleGetByIdDTOApiV1;
import org.jaybon.jaylog.domain.article.dto.res.ResArticlePostDTOApiV1;
import org.jaybon.jaylog.domain.article.dto.res.ResArticlePostLikeByIdDTOApiV1;
import org.jaybon.jaylog.domain.article.service.ArticleServiceApiV1;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1/article")
public class ArticleControllerApiV1 {

    private final ArticleServiceApiV1 articleServiceApiV1;

    @GetMapping("/{id}")
    public ResponseEntity<ResDTO<ResArticleGetByIdDTOApiV1>> getByIdAndCustomUserDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return new ResponseEntity<>(
                ResDTO.<ResArticleGetByIdDTOApiV1>builder()
                        .code(Constants.ResCode.OK)
                        .message("게시글 조회에 성공했습니다.")
                        .data(articleServiceApiV1.getByIdAndCustomUserDetails(id, customUserDetails))
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ResDTO<ResArticlePostDTOApiV1>> postBy(
            @Valid @RequestBody ReqArticlePostDTOApiV1 dto,
            @NotNull @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return new ResponseEntity<>(
                ResDTO.<ResArticlePostDTOApiV1>builder()
                        .code(Constants.ResCode.OK)
                        .message("게시글 저장에 성공했습니다.")
                        .data(articleServiceApiV1.postBy(dto, customUserDetails))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> putBy(
            @PathVariable Long id,
            @Valid @RequestBody ReqArticlePutDTOApiV1 dto,
            @NotNull @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        articleServiceApiV1.putBy(id, dto, customUserDetails);
        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(Constants.ResCode.OK)
                        .message("게시글 수정에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> deleteByIdAndCustomUserDetails(
            @PathVariable Long id,
            @NotNull @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        articleServiceApiV1.deleteByIdAndCustomUserDetails(id, customUserDetails);
        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(Constants.ResCode.OK)
                        .message("게시글 삭제에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ResDTO<ResArticlePostLikeByIdDTOApiV1>> postLikeByIdAndCustomUserDetails(
            @PathVariable Long id,
            @NotNull @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return new ResponseEntity<>(
                ResDTO.<ResArticlePostLikeByIdDTOApiV1>builder()
                        .code(Constants.ResCode.OK)
                        .message("게시글 좋아요 변경에 성공했습니다.")
                        .data(articleServiceApiV1.postLikeByIdAndCustomUserDetails(id, customUserDetails))
                        .build(),
                HttpStatus.OK
        );
    }

}
