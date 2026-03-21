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
 * The java class that defines tabletypes table.
 * foreign key to restaurant_tables.id
 * type - holds the type of table (outside/inside/quiet corner) which is for filtering different seats
 * 
 * Holds type field for restaurant table since a table can have many types at once.
 */

@Entity
@Table(name = "tabletypes")
public class TableTypes {
	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;

    private String type;

    @ManyToOne
    @JoinColumn(name="table_id", nullable=false)
    private TableAsClass table;
}