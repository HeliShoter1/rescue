package com.rescue.rescue.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateRelative {
    @NotNull
    @NotBlank
    private Long relativeId;
    @NotNull
    @NotBlank
    private String relationship;    
}
