package com.example.automation;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "WatchAds";
    private UiDevice uiDevice;
    private Context context;

    @Before
    public void setup() {
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        uiDevice.pressHome();

//        uiDevice.pressKeyCode(KeyEvent.KEYCODE_APP_SWITCH);
//        uiDevice.swipe(522, 1467, 522, 90, 10);
//        uiDevice.pressHome();

        try {
            uiDevice.executeShellCommand("am force-stop com.google.android.youtube");
        } catch (Exception e) {
            Log.d(TAG, "setup: " + e.getMessage());
        }
    }

    @Test
    public void watchAds() {
        try {
            // Open youtube app
            ComponentName componentName = new ComponentName("com.google.android.youtube", "com.google.android.apps.youtube.app.WatchWhileActivity");
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(componentName);
            context.startActivity(intent);
            uiDevice.waitForIdle();

            // Search video by name
            String videoTitle = "BLACKPINK - 'How You Like That' M/V";
            UiObject searchButton = uiDevice.findObject(new UiSelector().resourceId("com.google.android.youtube:id/menu_item_0"));
            searchButton.clickAndWaitForNewWindow();
            UiObject searchBox = uiDevice.findObject(new UiSelector().resourceId("com.google.android.youtube:id/search_edit_text"));
            searchBox.setText(videoTitle);
            uiDevice.pressEnter();
            uiDevice.waitForIdle();

            // Click first result
            UiObject recyclerView = uiDevice.findObject(new UiSelector().resourceId("com.google.android.youtube:id/results"));
            UiObject videoView = recyclerView.getChild(new UiSelector().descriptionContains(videoTitle));
            videoView.clickAndWaitForNewWindow();
            uiDevice.waitForIdle();

            UiObject adView = getAdView();
            adView.clickAndWaitForNewWindow();
        } catch (Exception e) {
            Log.d(TAG, "watchAds: " + e.getMessage());
        }
    }

    private UiObject getAdView() {
        // Lazada / Shopee
        UiObject view =  uiDevice.findObject(new UiSelector().description("INSTALL"));
        if (!view.exists()) {
            // Website
            view = uiDevice.findObject(new UiSelector().description("MUA NGAY"));
        }
        return view;
    }
}