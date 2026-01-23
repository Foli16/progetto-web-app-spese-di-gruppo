package com.exercise.progetto_individuale.dtos.input_dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputGroupDto {
    private String name;
    private List<InputParticipantDto> participants = new ArrayList<>();
}
