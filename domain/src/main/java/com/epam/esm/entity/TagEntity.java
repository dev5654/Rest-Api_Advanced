package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tag")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TagEntity extends BaseEntity{
    private String name;
}


