package com.example.nutikas_restorani_serveerimissysteem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.nutikas_restorani_serveerimissysteem.logic.Table;


@SpringBootTest
public class ServeClientTest {
    @Autowired
    private PathController pc;

    @Test
    void testThatTableArrayIsMade() {
        /**
         * This tests that when PathController is made, it automatically makes ServeClient.
         * SerceClient automatically makes Tables.
         * Tables automatically makes GenAllTablesArray which populates the Table[] array in Tables class.
         * To test all that, PathController is made automatically with @Autowire
         * Then classes are extracted and all that is left to check is the size of the Table array
         */

        Object sc = ReflectionTestUtils.getField(pc, "sc");
        Object tables = ReflectionTestUtils.getField(sc, "mTables");
        Table[] arr = (Table[]) ReflectionTestUtils.getField(tables, "mTables");

        assertNotNull(arr);
        assertTrue(arr.length > 0);
    }
}

// Test is made with the help of AI