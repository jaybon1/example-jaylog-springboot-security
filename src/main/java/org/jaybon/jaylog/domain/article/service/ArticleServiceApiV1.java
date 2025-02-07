package org.jaybon.jaylog.domain.article.service;


import lombok.RequiredArgsConstructor;
import org.jaybon.jaylog.common.constants.Constants;
import org.jaybon.jaylog.common.dto.ResDTO;
import org.jaybon.jaylog.common.exception.BadRequestException;
import org.jaybon.jaylog.config.security.auth.CustomUserDetails;
import org.jaybon.jaylog.domain.article.dto.req.ReqArticlePostDTOApiV1;
import org.jaybon.jaylog.domain.article.dto.req.ReqArticlePutDTOApiV1;
import org.jaybon.jaylog.domain.article.dto.res.ResArticleGetByIdDTOApiV1;
import org.jaybon.jaylog.domain.article.dto.res.ResArticlePostDTOApiV1;
import org.jaybon.jaylog.domain.article.dto.res.ResArticlePostLikeByIdDTOApiV1;
import org.jaybon.jaylog.model.article.entity.ArticleEntity;
import org.jaybon.jaylog.model.article.repository.ArticleRepository;
import org.jaybon.jaylog.model.like.entity.LikeEntity;
import org.jaybon.jaylog.model.like.repository.LikeRepository;
import org.jaybon.jaylog.model.user.entity.UserEntity;
import org.jaybon.jaylog.model.user.repository.UserRepository;
import org.jaybon.jaylog.util.UtilFunction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleServiceApiV1 {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final LikeRepository likeRepository;

    public ResArticleGetByIdDTOApiV1 getByIdAndCustomUserDetails(Long id, CustomUserDetails customUserDetails) {
        ArticleEntity articleEntity = UtilFunction.getArticleEntityBy(articleRepository, id);
        ResArticleGetByIdDTOApiV1 resArticleGetByIdDTOApiV1;
        if (customUserDetails != null) {
            UserEntity userEntity = UtilFunction.getUserEntityBy(userRepository, customUserDetails);
            resArticleGetByIdDTOApiV1 = ResArticleGetByIdDTOApiV1.of(articleEntity, userEntity);
        } else {
            resArticleGetByIdDTOApiV1 = ResArticleGetByIdDTOApiV1.of(articleEntity);
        }
        return resArticleGetByIdDTOApiV1;
    }

    @Transactional
    public ResArticlePostDTOApiV1 postBy(
            ReqArticlePostDTOApiV1 dto,
            CustomUserDetails customUserDetails
    ) {
        System.out.println("dto = " + dto);
        UserEntity userEntity = UtilFunction.getUserEntityBy(userRepository, customUserDetails);
        ArticleEntity articleEntity = articleRepository.save(dto.getArticle().toEntityWith(userEntity));
        return ResArticlePostDTOApiV1.of(articleEntity);
    }

    @Transactional
    public void putBy(
            Long id,
            ReqArticlePutDTOApiV1 dto,
            CustomUserDetails customUserDetails
    ) {
        UserEntity userEntity = UtilFunction.getUserEntityBy(userRepository, customUserDetails);
        ArticleEntity articleEntity = UtilFunction.getArticleEntityBy(articleRepository, id);
        if (!articleEntity.getUserEntity().equals(userEntity)) {
            throw new BadRequestException("게시글 작성자만 수정할 수 있습니다.");
        }
        dto.getArticle().update(articleEntity);
    }

    @Transactional
    public void deleteByIdAndCustomUserDetails(
            Long id,
            CustomUserDetails customUserDetails
    ) {
        UserEntity userEntity = UtilFunction.getUserEntityBy(userRepository, customUserDetails);
        ArticleEntity articleEntity = UtilFunction.getArticleEntityBy(articleRepository, id);
        if (!articleEntity.getUserEntity().equals(userEntity)) {
            throw new BadRequestException("게시글 작성자만 삭제할 수 있습니다.");
        }
        articleEntity.setDeleteDate(LocalDateTime.now());
    }


    @Transactional
    public ResArticlePostLikeByIdDTOApiV1 postLikeByIdAndCustomUserDetails(
            Long id,
            CustomUserDetails customUserDetails
    ) {
        UserEntity userEntity = UtilFunction.getUserEntityBy(userRepository, customUserDetails);
        ArticleEntity articleEntity = UtilFunction.getArticleEntityBy(articleRepository, id);
        boolean isLikeClicked = likeRepository.countByUserEntity_IdAndArticleEntity_Id(userEntity.getId(), articleEntity.getId()) != 0;
        if (isLikeClicked) {
            likeRepository.deleteByUserEntity_IdAndArticleEntity_Id(userEntity.getId(), articleEntity.getId());
        } else {
            LikeEntity likeEntityForSaving = LikeEntity.builder()
                    .userEntity(userEntity)
                    .articleEntity(articleEntity)
                    .build();
            likeRepository.save(likeEntityForSaving);
        }
        long likeCount = likeRepository.countByArticleEntity_Id(articleEntity.getId());
        return ResArticlePostLikeByIdDTOApiV1.of(likeCount, !isLikeClicked);
    }
}
