package com.example.vibecapandroid.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * 키보드 상태 감지 클래스
 */
class SoftKeyboardDetectorView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) :
    View(context, attrs) {

    private var mShownKeyboard = false
    private var mOnShownSoftKeyboard: OnShownKeyboardListener? = null
    private var onHiddenSoftKeyboard: OnHiddenKeyboardListener? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val activity = context as Activity
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)

        val statusBarHeight = rect.top
        val screenHeight = activity.windowManager.defaultDisplay.height
        val diffHeight = screenHeight - statusBarHeight - h

        if (diffHeight > 100 && !mShownKeyboard) { // 모든 키보드는 100px보다 크다고 가정
            mShownKeyboard = true
            onShownSoftKeyboard()
        } else if (diffHeight < 100 && mShownKeyboard) {
            mShownKeyboard = false
            onHiddenSoftKeyboard()
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun onHiddenSoftKeyboard() {
        if (onHiddenSoftKeyboard != null) onHiddenSoftKeyboard!!.onHiddenSoftKeyboard()
    }

    fun onShownSoftKeyboard() {
        if (mOnShownSoftKeyboard != null) mOnShownSoftKeyboard!!.onShowSoftKeyboard()
    }

    fun setOnShownKeyboard(listener: OnShownKeyboardListener?) {
        mOnShownSoftKeyboard = listener
    }

    fun setOnHiddenKeyboard(listener: OnHiddenKeyboardListener?) {
        onHiddenSoftKeyboard = listener
    }

    interface OnShownKeyboardListener {
        fun onShowSoftKeyboard()
    }

    interface OnHiddenKeyboardListener {
        fun onHiddenSoftKeyboard()
    }
}