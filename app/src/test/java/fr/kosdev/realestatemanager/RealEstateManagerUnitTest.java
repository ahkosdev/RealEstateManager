package fr.kosdev.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.kosdev.realestatemanager.Utils.Utils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RealEstateManagerUnitTest {
    @Test
    public void convertEuroToDollarTest() {
        int euro = 1000;
        int dollar = (int) Math.round(1000/0.812);
        assertEquals(dollar, Utils.convertEuroToDollar(euro), 0.001);
    }

    @Test
    public void todayDateTest()  {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
        String todayDate = sdf.format(new Date());
        assertEquals(todayDate, Utils.getTodayDateWithAppropriateFormat());
    }
}