package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "postalcodeview")
public class PostalCodeView {

    @Id
    @Column(name = "business_id")
    private String business_id;

    @Column(name = "postal_code")
    private String postal_code;

    @Column(name = "categories")
    private String categories;

    @Column(name = "attributes")
    private String attributes;



}
