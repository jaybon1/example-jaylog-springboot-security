package org.jaybon.jaylog.domain.main.controller;

import lombok.RequiredArgsConstructor;
import org.jaybon.jaylog.common.constants.Constants;
import org.jaybon.jaylog.common.dto.ResDTO;
import org.jaybon.jaylog.config.security.auth.CustomUserDetails;
import org.jaybon.jaylog.domain.main.dto.res.ResMainGetDTOApiV1;
import org.jaybon.jaylog.domain.main.service.MainServiceApiV1;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/main")
public class MainControllerApiV1 {

    private final MainServiceApiV1 mainServiceApiV1;

    @GetMapping
    public ResponseEntity<ResDTO<ResMainGetDTOApiV1>> getBy(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return new ResponseEntity<>(
                ResDTO.<ResMainGetDTOApiV1>builder()
                        .code(Constants.ResCode.OK)
                        .message("메인 조회에 성공했습니다.")
                        .data(mainServiceApiV1.getBy(searchValue, pageable, customUserDetails))
                        .build(),
                HttpStatus.OK
        );
    }

}
