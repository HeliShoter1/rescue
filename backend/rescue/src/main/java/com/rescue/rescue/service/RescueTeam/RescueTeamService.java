package com.rescue.rescue.service.RescueTeam;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.RescueTeamDto;
import com.rescue.rescue.enums.RescueTeamStatus;
import com.rescue.rescue.exceptions.ResourceNotFoundException;
import com.rescue.rescue.model.Post;
import com.rescue.rescue.model.RescueTeam;
import com.rescue.rescue.reponsitory.PostRepository;
import com.rescue.rescue.reponsitory.RescueTeamRepository;
import com.rescue.rescue.request.CreateRescueTeamRequest;
import com.rescue.rescue.request.UpdateRescueTeamRequest;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RescueTeamService implements IRescueTeamService {
    private final RescueTeamRepository rescueTeamRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public RescueTeamDto getRescueTeamById(Long id) {
        RescueTeam rescueTeam = rescueTeamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rescue team not found with id: " + id));
        return RescueTeamDto.fromEntity(rescueTeam);
    }

    @Override
    public RescueTeamDto getRescueTeamByPostId(Long postId) {
        RescueTeam rescueTeam = rescueTeamRepository.findByPostId(postId);
        return RescueTeamDto.fromEntity(rescueTeam);
    }

    @Override
    public List<RescueTeamDto> getAllRescueTeams(RescueTeamStatus status, Long cursor, Integer limit) {
        return rescueTeamRepository.findByStatus(status, cursor, limit)
                .stream()
                .map(RescueTeamDto::fromEntity)
                .toList();
    }

    @Override
    public RescueTeamDto createRescueTeam(CreateRescueTeamRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + request.getPostId()));
        RescueTeam rescueTeam = RescueTeam.builder()
                .post(post)
                .status(RescueTeamStatus.AVAILABLE)
                .build();
        rescueTeamRepository.save(rescueTeam);
        return RescueTeamDto.fromEntity(rescueTeam);
    }

    @Override
    public void updateRescueTeam(UpdateRescueTeamRequest request) { 
        RescueTeam rescueTeam = rescueTeamRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Rescue team not found with id: " + request.getId()));
        modelMapper.map(request, rescueTeam);
        rescueTeamRepository.save(rescueTeam);
    }
    
}
