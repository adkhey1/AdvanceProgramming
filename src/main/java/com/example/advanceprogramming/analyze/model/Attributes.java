package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "attributes")
@IdClass(AttributesID.class)
public class Attributes implements Serializable {

    @Id
    @Column(name = "bID")
    private String bID;

    @Id
    @Column(name = "aName")
    private String aName;

    @Column(name = "content")
    private String content;

    @Column(name = "content_is_table")
    private boolean content_is_table;



}
