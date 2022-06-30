package com.example.advanceprogramming.analyze.model;

import com.example.advanceprogramming.auth.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@IdClass(UserBusinessRelationID.class)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserBusinessRelation {

    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "business_id")
    private String businessId;

}
