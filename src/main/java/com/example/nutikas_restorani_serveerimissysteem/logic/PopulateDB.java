package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.Tables;
import com.example.nutikas_restorani_serveerimissysteem.logic.TableAsClass;
import org.springframework.stereotype.Component;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import java.lang.Integer;


import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is responsible for populating the database every time the appplication starts.
 * It is called by StartupRunner after Spring has finished initialisation.
 */
@Component
public class PopulateDB {
    @PersistenceContext
	private EntityManager mEM; // Handles database queries
    String path = "src/main/resources/tables.txt"; // All info for database is stored here. Could be removed by making sql init file.

    public void insertToTables(int maxSeats, String sizeName, String[] types) {
        // After getting info from file, insert into tables array
        // Then add type to tables via tabletypes table.

        int id = (int) mEM.createNativeQuery(
            "INSERT INTO restaurant_tables(m_max_seats, m_size_name)"
			+ "VALUES(:seats, :sizeName)"
            + "RETURNING id;", Integer.class)
		    .setParameter("seats", maxSeats)
            .setParameter("sizeName", sizeName)
            .getSingleResult();

        for (String type : types) {
            mEM.createNativeQuery(
                "INSERT INTO tabletypes(type, table_id)"
			    + "VALUES(:type, :table_id);")
		        .setParameter("type", type)
                .setParameter("table_id", id)
                .executeUpdate();
        }
    }
    @Transactional
    public void populateTables() {
        
        try(Scanner s = new Scanner(new File(path))) {
            
            /*
            TODO
            - Initial implementation places a lot of trust on the text file...
            - If the order or integrity of the file changes stuff breaks.
            */

            int n = s.nextInt(); // First line of file is a number that indicates how many tables are stored in the file.
            for(int i = 0; i < n; i++) {
                
                /*
                Every line is in the form of "seats sizeType type"
                seats - max seats at table
                sizeType - used for styling webpage. 1 - smallTable, 2 - mediumTable, 3 - mediumTable2, 4 - bigTable
                type - in the form "type1 type2 ..." specifies the table type - outside/inside/quiet corner etc. (currently only first 3 are used)
                */

                int maxSeats = s.nextInt();
                int sizeSpecifier = s.nextInt();
                String sizeName;

                switch (sizeSpecifier)
                {
                    case 1:
                        sizeName = "smallTable";
                        break;
                    case 2:
                        sizeName = "mediumTable";
                        break;
                    case 3:
                        sizeName = "mediumTable2";
                        break;
                    case 4:
                        sizeName = "bigTable";
                        break;
                    default:
                        sizeName = "error";
                        break;
                }
                String tableType = s.nextLine().trim();
                String[] types = tableType.split(" ");

                insertToTables(maxSeats, sizeName, types);

            }

        }catch (FileNotFoundException e) {
            System.out.println(path +" was not found!");
        }
    }

    public PopulateDB() {}
}