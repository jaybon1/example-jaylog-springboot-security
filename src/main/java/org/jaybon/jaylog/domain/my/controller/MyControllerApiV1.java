package org.jaybon.jaylog.domain.my.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaybon.jaylog.common.constants.Constants;
import org.jaybon.jaylog.common.dto.ResDTO;
import org.jaybon.jaylog.config.security.auth.CustomUserDetails;
import org.jaybon.jaylog.domain.my.dto.req.ReqMyPutInfoDTOApiV1;
import org.jaybon.jaylog.domain.my.dto.res.ResMyGetDTOApiV1;
import org.jaybon.jaylog.domain.my.service.MyServiceApiV1;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1/my")
@Slf4j
public class MyControllerApiV1 {

    private final MyServiceApiV1 myServiceApiV1;

    @GetMapping
    public ResponseEntity<ResDTO<ResMyGetDTOApiV1>> getBy(
            @NotNull @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return new ResponseEntity<>(
                ResDTO.<ResMyGetDTOApiV1>builder()
                        .code(Constants.ResCode.OK)
                        .message("마이페이지 조회에 성공했습니다.")
                        .data(myServiceApiV1.getBy(customUserDetails))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/info")
    public ResponseEntity<ResDTO<Object>> putBy(
            ReqMyPutInfoDTOApiV1 dto, // form-data로 받을 때는 @ModelAttribute 또는 어노테이션 없이 받아야함
            @NotNull @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        myServiceApiV1.putBy(dto, customUserDetails);
        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(Constants.ResCode.OK)
                        .message("회원 정보 수정에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

}
