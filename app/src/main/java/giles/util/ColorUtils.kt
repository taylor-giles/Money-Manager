package giles.util

import android.graphics.Color

object ColorUtils {
    fun getHue(color: Int): Float{
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return hsv[0]
    }

    fun getHexString(color: Int): String{
        return String.format("%06X", 0xFFFFFF and color)
    }

    fun getContrastingTextColor(color: Int): Int{
        return if(Color.red(color)*0.299 + Color.green(color)*0.587 + Color.blue(color)*0.114 > 186)
            Color.BLACK else Color.WHITE
    }
}