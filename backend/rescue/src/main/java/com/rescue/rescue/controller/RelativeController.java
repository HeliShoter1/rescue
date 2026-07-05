package com.rescue.rescue.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.dto.RelativeDto;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.request.CreateRelative;
import com.rescue.rescue.request.UpdateRelativeStatus;
import com.rescue.rescue.service.Relative.IRelativeService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/relatives")
public class RelativeController {
    private final IRelativeService relativeService;

    @GetMapping("/getListRelative")
    public ResponseEntity<ApiResponse> getAllRelative(
        @RequestParam(value = "cursor",defaultValue = "0") Long Cursor,
        @RequestParam(value = "limit",defaultValue = "10") Integer limit) {
        List<RelativeDto> relatives = relativeService.getRelativesByUserId(Cursor, limit);
        return ResponseEntity.ok(new ApiResponse("success", relatives));
    }

    @PostMapping("/addRelative")
    public ResponseEntity<ApiResponse> addRelative(@RequestBody CreateRelative relative) {
        //TODO: process POST request
        RelativeDto createdRelative = relativeService.createRelative(relative);
        return ResponseEntity.ok(new ApiResponse("success", createdRelative));
    }
    @DeleteMapping("/deleteRelative/{id}")
    public ResponseEntity<ApiResponse> deleteRelative(@PathVariable Long id) {
        relativeService.deleteRelative(id);
        return ResponseEntity.ok(new ApiResponse("success", null));
    }

    @PutMapping("/updateRelative/user")
    public ResponseEntity<ApiResponse> updateRelative(@RequestBody UpdateRelativeStatus updateStatus) {
        relativeService.updateStatusRelative(updateStatus.getUserId(), updateStatus.getStatus());
        return ResponseEntity.ok(new ApiResponse("success", null));
    }

    @PutMapping("/updateRelative/Relative")
    public ResponseEntity<ApiResponse> updateRelativeById(@RequestBody UpdateRelativeStatus updateStatus) {
        relativeService.updateStatusRelative(updateStatus.getRelativeId(), updateStatus.getStatus());
        return ResponseEntity.ok(new ApiResponse("success", null));
    }
}
