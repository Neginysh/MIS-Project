package com.example.lenovocom.lieortruth;

import android.content.Context;

// reference: https://github.com/ebanx/swipe-button
final class DimentionUtils {

    private DimentionUtils() {
    }

    static float converPixelsToSp(float px, Context context) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }
}
