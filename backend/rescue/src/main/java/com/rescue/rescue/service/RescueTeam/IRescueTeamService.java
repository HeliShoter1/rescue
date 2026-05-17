package com.rescue.rescue.service.RescueTeam;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.rescue.rescue.dto.RescueTeamDto;
import com.rescue.rescue.enums.RescueTeamStatus;
import com.rescue.rescue.request.CreateRescueTeamRequest;
import com.rescue.rescue.request.UpdateRescueTeamRequest;

public interface IRescueTeamService {
    RescueTeamDto getRescueTeamById(Long id);
    RescueTeamDto getRescueTeamByPostId(Long postId);
    List<RescueTeamDto> getAllRescueTeams(RescueTeamStatus status, Long cursor, Integer limit );
    void createRescueTeam(CreateRescueTeamRequest request);
    void updateRescueTeam(UpdateRescueTeamRequest request);
    
}
