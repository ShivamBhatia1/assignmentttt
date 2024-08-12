package com.example.assignmenttt




import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
data class Note(
    val id: String? = null,
    val title: String = "",
    val content: String = "",
    val userId: String? = null
) : Parcelable {
    constructor() : this(null, "", "", null)
}


