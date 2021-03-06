package ir.nwise.app.ui.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Replace old items with new items
 * @param items New items
 */
fun <T> MutableCollection<T>.replaceAll(items: Collection<T>) {
    clear()
    addAll(items)
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}