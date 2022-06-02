package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserBusinessRelationID implements Serializable {
    private String businessId;
    private long userId;

}
