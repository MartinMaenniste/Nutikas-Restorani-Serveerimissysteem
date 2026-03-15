package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.Tables;
import com.example.nutikas_restorani_serveerimissysteem.logic.Table;
import org.springframework.stereotype.Component;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

// Generates and sets Table array in Tables class
@Component
public class GenAllTablesArray {
    private Table[] mTables;

    private void genArray() {
        String path = "src/main/resources/tables.txt"; // Info for generating array of Table[] is stored inside the text file
        
        try(Scanner s = new Scanner(new File(path))) {
            
            /*
            TODO
            - Initial implementation places a lot of trust on the text file...
            - If the order or integrity of the file changes stuff breaks.
            */

            int n = s.nextInt(); // First line of file is a number that indicates how many tables are stored in the file.
            mTables = new Table[n];

            for(int i = 0; i < mTables.length; i++) {
                
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

                mTables[i] = new Table(maxSeats, sizeName, types);
            }

        }catch (FileNotFoundException e) {
            System.out.println(path +" was not found!");
        }
    }

    public GenAllTablesArray() {
        this.genArray();
    }
    public Table[] getArray() {
        return this.mTables;
    }
}