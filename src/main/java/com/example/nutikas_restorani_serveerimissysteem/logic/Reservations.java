package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.TableAsClass;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "reservations")
public class Reservations {
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;

    private long starttime;
    private long endtime;

    @ManyToOne
    @JoinColumn(name="table_id", nullable=false)
    private TableAsClass table;
}