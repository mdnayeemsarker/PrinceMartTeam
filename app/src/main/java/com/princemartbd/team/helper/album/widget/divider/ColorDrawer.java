/*
 * Copyright 2018 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.princemartbd.team.helper.album.widget.divider;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.ColorInt;

class ColorDrawer extends Drawer {

    public ColorDrawer(int color, int width, int height) {
        super(new ColorDrawable(opaqueColor(color)), width, height);
    }

    @ColorInt
    public static int opaqueColor(@ColorInt int color) {
        int alpha = Color.alpha(color);
        if (alpha == 0) return color;
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(255, red, green, blue);
    }
}