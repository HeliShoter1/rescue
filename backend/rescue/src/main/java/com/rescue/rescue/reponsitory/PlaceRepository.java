package com.rescue.rescue.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rescue.rescue.model.Place;

import jakarta.transaction.Transactional;

@Transactional
public interface PlaceRepository extends JpaRepository<Place, Long> {
    
}
