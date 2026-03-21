package com.example.nutikas_restorani_serveerimissysteem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.reflect.Method;

import java.util.Calendar;
import java.lang.NoSuchMethodException;
import java.lang.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;

import com.example.nutikas_restorani_serveerimissysteem.logic.Table;


@SpringBootTest
public class TestReservationRemoval {

    private long getDateTimeRightNow() {
        Calendar now = Calendar.getInstance();

		long dateTimeNow = now.get(now.YEAR);
		dateTimeNow *= 100;
		dateTimeNow += now.get(now.MONTH) + 1;
		dateTimeNow *= 100;
		dateTimeNow += now.get(now.DAY_OF_MONTH);
		dateTimeNow *= 100;
		dateTimeNow += now.get(now.HOUR_OF_DAY);
		dateTimeNow *= 100;
		dateTimeNow += now.get(now.MINUTE);

        return dateTimeNow;
    }

    private Method getSortReservationsMethod() throws NoSuchMethodException{
        Method method = Table.class.getDeclaredMethod("sortReservations", Table.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    public void testReservationRemoval() {
        // Add reservations that are older than right now
        // Call the sort method and check reservations array size
        Table t = new Table(4, "smallTable", new String[]{"inside"});

        long dateTimeNow = getDateTimeRightNow();

        // To push times to the future, it seems safes to use years. Others can't have values more than 30/60/23
        // Program should still work with incorrect dates, but it doesn't make much sense to test incorrect times.
        t.addReservation(dateTimeNow, dateTimeNow + 100000000L); // +1 year - should STAY
        t.addReservation(dateTimeNow - 200L, dateTimeNow - 100L); // -2h to -1h
        t.addReservation(dateTimeNow-1L, dateTimeNow-1L);
        t.addReservation(0L, 0L);
        t.addReservation(300001010000L, 300001010001L); // year 3000, first of jan, 00:00 to 00:01 - should STAY
        t.addReservation(dateTimeNow-500L, dateTimeNow + 100000000L); // -5h to +1 year - should STAY
        t.addReservation(dateTimeNow + 100000000L, dateTimeNow + 200000000L); // +1 year to +2 years - should STAY

        try {
            getSortReservationsMethod().invoke(t);
        }
        catch (NoSuchMethodException e) {
            System.out.println("No method found in calling getSortReservationsMethod() with variable 't'");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        assertEquals(t.getAmountOfReservations(), 4);
    }
}