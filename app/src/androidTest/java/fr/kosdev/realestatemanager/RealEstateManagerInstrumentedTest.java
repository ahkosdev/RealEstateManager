package fr.kosdev.realestatemanager;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import fr.kosdev.realestatemanager.Utils.Utils;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RealEstateManagerInstrumentedTest {
    @Test
    public void activeNetworkTest() {
        // Context of the app under test.
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        assertEquals(Utils.isInternetAvailable(context), true);
    }
}