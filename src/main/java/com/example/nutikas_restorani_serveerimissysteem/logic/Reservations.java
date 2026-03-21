package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.TableAsClass;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

/**
 * The java class that defines reservations table.
 * foreign key to restaurant_tables.id
 * Start and end times as a long number - yyyymmddhhmm (year, month, day, hour, minute)
 * 
 * Stores reservations for the restaurant tables
 */

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