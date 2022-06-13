package com.example.advanceprogramming.analyze.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CorrelationAnalysisDTO {

    String attributeName;
    double correlation;
}
