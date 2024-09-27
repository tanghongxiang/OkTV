package com.thx.resourcelib.ext

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

inline fun <reified T : DialogFragment> FragmentManager.showDialog(dialog: T) {
    try {
        dialog.show(this, dialog::class.java.simpleName)
    } catch (e: Exception) {
        Log.e("", "$e")
    }
}


inline fun <reified T : DialogFragment> FragmentManager.dismissDialog() {
    if ((findFragmentByTag(T::class.java.simpleName) as? DialogFragment) != null) {
        (findFragmentByTag(T::class.java.simpleName) as? DialogFragment)?.dismissAllowingStateLoss()
    }
}