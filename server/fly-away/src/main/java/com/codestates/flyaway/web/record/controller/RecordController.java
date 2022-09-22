package com.codestates.flyaway.web.record.controller;

import com.codestates.flyaway.domain.record.service.RecordService;
import com.codestates.flyaway.global.dto.SingleResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.codestates.flyaway.web.record.dto.RecordDto.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;

    //영상시청 후 운동시간 저장 요청
    @ApiOperation(value = "운동 시간 기록", notes = "운동영상 시청 시간을 회원의 운동 기록에 반영")
    @ResponseStatus(value = CREATED)
    @PostMapping("/{memberId}")      //memberController에서 처리하는 게 맞을까?
    public SingleResponseDto insertRecord(@PathVariable long memberId,
                                       @Validated @RequestBody InsertRequestDto recordDto) {

        InsertResponseDto insertResponseDto = recordService.insertRecord(memberId, recordDto);

        return new SingleResponseDto(insertResponseDto);
    }
}