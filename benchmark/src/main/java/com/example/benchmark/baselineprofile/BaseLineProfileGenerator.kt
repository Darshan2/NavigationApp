package com.example.benchmark.baselineprofile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@RequiresApi(Build.VERSION_CODES.P)
class BaseLineProfileGenerator {
    @get:Rule
    val baseLineProfileRule = BaselineProfileRule()

    @Test
    fun generate() {
        baseLineProfileRule.collect(
            packageName = "com.example.navigationapp"
        ) {
            startActivityAndWait()
            device.wait(
                Until.hasObject(By.res("mainScreen:bottomNavigationBar")),
                30_000
            )
        }
    }

}