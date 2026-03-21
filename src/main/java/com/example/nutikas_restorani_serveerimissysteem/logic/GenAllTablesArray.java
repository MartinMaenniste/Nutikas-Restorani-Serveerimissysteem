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
import jakarta.annotation.PostConstruct;

// Generates and sets Table array in Tables class
@Component
public class GenAllTablesArray {
    @PersistenceContext
	private EntityManager mEM;

    @PostConstruct
    public void init() {
        this.genArray();
    }


    private void genArray() {
        String path = "src/main/resources/tables.txt"; // Info for generating array of Table[] is stored inside the text file
        
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

                /**
                 * 
                 * Add new row to table class
                 * - maxSeats, sizeName
                 * 
                 * Add new row(s) to tabletypes table
                 * - Id of TableAsClass, types[i]
                 * 
                 *  */

                int id = (int) mEM.createNativeQuery(
                "INSERT INTO tables(m_max_seats, m_size_name)"
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
                    .setParameter("table_id", id);
                }
            }

        }catch (FileNotFoundException e) {
            System.out.println(path +" was not found!");
        }
    }

    public GenAllTablesArray() {
        //this.genArray();
    }
}