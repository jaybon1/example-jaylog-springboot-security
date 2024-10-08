package org.jaybon.jaylog.domain.article.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jaybon.jaylog.model.article.entity.ArticleEntity;
import org.jaybon.jaylog.model.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResArticleGetByIdDTOApiV1 {

    private Article article;

    public static ResArticleGetByIdDTOApiV1 of(ArticleEntity articleEntity, UserEntity loginUserEntity) {
        return ResArticleGetByIdDTOApiV1.builder()
                .article(Article.from(articleEntity, loginUserEntity))
                .build();
    }

    public static ResArticleGetByIdDTOApiV1 of(ArticleEntity articleEntity) {
        return ResArticleGetByIdDTOApiV1.builder()
                .article(Article.from(articleEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Article {

        private Long id;
        private Writer writer;
        private String title;
        private String thumbnail;
        private String content;
        private Long likeCount;
        private Boolean isLikeClicked;
        private LocalDateTime createDate;

        public static Article from(ArticleEntity articleEntity, UserEntity loginUserEntity) {
            Writer writer;
            if (Objects.isNull(articleEntity.getUserEntity().getDeleteDate())) {
                writer = Writer.builder()
                        .username(articleEntity.getUserEntity().getUsername())
                        .profileImage(articleEntity.getUserEntity().getProfileImage())
                        .build();
            } else {
                writer = Writer.builder()
                        .username("탈퇴한 사용자")
                        .profileImage("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png")
                        .build();
            }
            boolean isLikeClicked = articleEntity.getLikeEntityList()
                    .stream()
                    .anyMatch(likeEntity -> Objects.equals(likeEntity.getUserEntity().getId(), loginUserEntity.getId()));
            return Article.builder()
                    .id(articleEntity.getId())
                    .writer(writer)
                    .title(articleEntity.getTitle())
                    .thumbnail(articleEntity.getThumbnail())
                    .content(articleEntity.getContent())
                    .likeCount((long) articleEntity.getLikeEntityList().size())
                    .isLikeClicked(isLikeClicked)
                    .createDate(articleEntity.getCreateDate())
                    .build();
        }

        public static Article from(ArticleEntity articleEntity) {
            Writer writer;
            if (Objects.isNull(articleEntity.getUserEntity().getDeleteDate())) {
                writer = Writer.builder()
                        .username(articleEntity.getUserEntity().getUsername())
                        .profileImage(articleEntity.getUserEntity().getProfileImage())
                        .build();
            } else {
                writer = Writer.builder()
                        .username("탈퇴한 사용자")
                        .profileImage("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png")
                        .build();
            }
            return Article.builder()
                    .id(articleEntity.getId())
                    .writer(writer)
                    .title(articleEntity.getTitle())
                    .thumbnail(articleEntity.getThumbnail())
                    .content(articleEntity.getContent())
                    .likeCount((long) articleEntity.getLikeEntityList().size())
                    .isLikeClicked(false)
                    .createDate(articleEntity.getCreateDate())
                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Writer {
            private String username;
            private String profileImage;
        }

    }

}
