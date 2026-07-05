package com.rescue.rescue.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.dto.PlaceDto;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.request.CreatePlace;
import com.rescue.rescue.service.Place.IPlaceService;

import lombok.AllArgsConstructor;

import org.apache.ibatis.annotations.Delete;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("${api.prefix}/places")
@AllArgsConstructor
public class PlaceController {
    private final IPlaceService placeService;

    @GetMapping("/{id}/place")
    public ResponseEntity<ApiResponse> getMethodName(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse("Place found", placeService.getPlaceById(id)));
    }

    @PostMapping("/create-place")
    public ResponseEntity<ApiResponse> postMethodName(@RequestBody CreatePlace entity) {
        PlaceDto placeDto = placeService.createPlace(entity);
        return ResponseEntity.ok(new ApiResponse("Place created", placeDto));
    }
    
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllPlaces() {
        return ResponseEntity.ok(new ApiResponse("All places found", placeService.getAllPlaces()));
    }
    
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deletePlaceById(@PathVariable Long id) {
        placeService.deletePlaceById(id);
        return ResponseEntity.ok(new ApiResponse("Place deleted", null));
    }
}
