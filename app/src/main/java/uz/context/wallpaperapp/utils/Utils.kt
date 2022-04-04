package uz.context.wallpaperapp.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(text: String) {
    Toast.makeText(this.requireContext(), text, Toast.LENGTH_SHORT).show()
}