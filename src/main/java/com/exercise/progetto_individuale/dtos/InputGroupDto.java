package com.exercise.progetto_individuale.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputGroupDto {
    private String name;
    private List<ParticipantDto> participants = new ArrayList<>();
}
