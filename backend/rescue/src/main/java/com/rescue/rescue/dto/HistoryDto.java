package com.rescue.rescue.dto;

import java.time.LocalDate;

import com.rescue.rescue.enums.HistoryStatus;
import com.rescue.rescue.model.History;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Builder
public class HistoryDto {
    private Long id;
    private RescueTeamDto rescueTeam;
    private PostDto post;
    private LocalDate createAt;
    private LocalDate updateAt;
    private HistoryStatus status;

    public static HistoryDto fromEntity(History history) {
        return HistoryDto.builder()
                .id(history.getId())
                .rescueTeam(RescueTeamDto.fromEntity(history.getRescueTeam()))
                .post(PostDto.fromEntity(history.getPost()))
                .createAt(history.getCreateAt())
                .updateAt(history.getUpdateAt())
                .status(history.getStatus())
                .build();
    }
}
