package uz.context.wallpaperapp.model

import com.google.firebase.Timestamp

data class WallpapersModel(
    val name: String = "",
    val image: String = "",
    val date: Timestamp? = null
)