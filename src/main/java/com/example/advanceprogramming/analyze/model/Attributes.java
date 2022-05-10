package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "attributes")
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
