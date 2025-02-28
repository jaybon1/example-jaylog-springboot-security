package org.jaybon.jaylog.domain.my.service;


import lombok.RequiredArgsConstructor;
import org.jaybon.jaylog.common.constants.Constants;
import org.jaybon.jaylog.common.dto.ResDTO;
import org.jaybon.jaylog.config.security.auth.CustomUserDetails;
import org.jaybon.jaylog.domain.my.dto.req.ReqMyPutInfoDTOApiV1;
import org.jaybon.jaylog.domain.my.dto.res.ResMyGetDTOApiV1;
import org.jaybon.jaylog.model.article.entity.ArticleEntity;
import org.jaybon.jaylog.model.like.entity.LikeEntity;
import org.jaybon.jaylog.model.like.repository.LikeRepository;
import org.jaybon.jaylog.model.user.entity.UserEntity;
import org.jaybon.jaylog.model.user.repository.UserRepository;
import org.jaybon.jaylog.util.UtilFunction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyServiceApiV1 {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder passwordEncoder;

    public ResMyGetDTOApiV1 getBy(CustomUserDetails customUserDetails) {
        UserEntity userEntity = UtilFunction.getUserEntityBy(userRepository, customUserDetails);
        List<ArticleEntity> myArticleEntityList = userEntity.getArticleEntityList();
        List<LikeEntity> myLikeEntityList = likeRepository.findByUserEntity_Id(userEntity.getId());
        List<ArticleEntity> likeArticleEntityList = myLikeEntityList
                .stream()
                .filter(thisLikeEntity -> thisLikeEntity.getArticleEntity().getDeleteDate() == null)
                .map(thisLikeEntity -> thisLikeEntity.getArticleEntity())
                .sorted((thisArticleEntity1, thisArticleEntity2) -> thisArticleEntity2.getId().compareTo(thisArticleEntity1.getId()))
                .toList();
        return ResMyGetDTOApiV1.of(userEntity, myArticleEntityList, likeArticleEntityList);
    }

    @Transactional
    public void putBy(ReqMyPutInfoDTOApiV1 dto, CustomUserDetails customUserDetails) {
        UserEntity userEntity = UtilFunction.getUserEntityBy(userRepository, customUserDetails);
        dto.updateWith(passwordEncoder, userEntity);
    }

}
