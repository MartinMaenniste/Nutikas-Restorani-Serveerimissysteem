package com.example.nutikas_restorani_serveerimissysteem.logic;

import com.example.nutikas_restorani_serveerimissysteem.logic.Tables;
import com.example.nutikas_restorani_serveerimissysteem.logic.Table;
import org.springframework.stereotype.Component;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

@Component
public class GenAllTablesArray {
    private Table[] mTables;

    private void genArray() {
        String path = "src/main/resources/tables.txt";
        
        try(Scanner s = new Scanner(new File(path))) {
            
            // Initial implementation places a lot of trust on the text file... Ordering and integrity
            int n = s.nextInt();
            mTables = new Table[n];

            for(int i = 0; i < mTables.length; i++) {
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

                mTables[i] = new Table(maxSeats, sizeName);
            }

        }catch (FileNotFoundException e) {
            System.out.println(path +" was not found!!");
        }
    }

    public GenAllTablesArray() {
        this.genArray();
    }
    public Table[] getArray() {
        return this.mTables;
    }
    public int arraySize() {
        return this.mTables.length;
    }
}