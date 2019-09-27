package sk.letsdream.helperMethods

import android.graphics.PorterDuff
import android.view.MotionEvent
import android.view.View

class ButtonEffects {

    fun ButtonClickEffect(button: View) {
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.background.setColorFilter(-0x1f0b8adf, PorterDuff.Mode.SRC_ATOP)
                    v.layoutParams.width += 120
                    v.layoutParams.height += 120
                    v.requestLayout()
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.layoutParams.width -= 120
                    v.layoutParams.height -= 120
                    v.requestLayout()
                    v.invalidate()
                }
            }
            false
        }
    }
}