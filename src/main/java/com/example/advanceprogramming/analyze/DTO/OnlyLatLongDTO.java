package com.example.advanceprogramming.analyze.DTO;

import com.example.advanceprogramming.analyze.model.LongLatResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OnlyLatLongDTO {

    private List<LongLatResult> output;
}
