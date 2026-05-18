package com.rescue.rescue.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class CreatePlace {

    @NotEmpty
    @NotNull
    Double longtude;

    @NotEmpty
    @NotNull
    Double latitude;
    String name;
}
