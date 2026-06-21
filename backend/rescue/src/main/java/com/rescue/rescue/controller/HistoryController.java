package com.rescue.rescue.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.enums.HistoryStatus;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.service.History.IHistoryService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("${api.prefix}/histories")
@AllArgsConstructor
public class HistoryController {
    private final IHistoryService historyService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all-history")
    public ResponseEntity<ApiResponse> getAllHistory(@RequestParam(value="cursor",required = true, defaultValue = "0") Long cursor,
                                                    @RequestParam(value="limit",required = true,defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(new ApiResponse("success", historyService.GetAllByAdmin(cursor, limit)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAll(@RequestParam(value="cursor",required = true, defaultValue = "0") Long cursor,
                                                    @RequestParam(value="limit",required = true,defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(new ApiResponse("success", historyService.GetAllByUserId(cursor, limit)));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse> getHistory() {
        return ResponseEntity.ok(new ApiResponse("success", historyService.GetById()));
    }

    // @PostMapping("/{id}/create-history")
    // public ResponseEntity<ApiResponse> createHistory(@PathVariable Long id) {
    //     return ResponseEntity.ok(new ApiResponse("success", historyService.CreateHistory(id)));
    // }
    
    @PutMapping("/history/{id}")
    public ResponseEntity<ApiResponse> putMethodName(@PathVariable Long id, @RequestBody HistoryStatus entity) {
        return ResponseEntity.ok(new ApiResponse("success", historyService.UpdateStatus(id, entity)));
    }
    
}
