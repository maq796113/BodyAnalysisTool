package com.example.bodyanalysistool.events

import android.net.Uri

sealed interface UserEvent {

    data class CheckImageDownloaded(val url: String): UserEvent

    data class UploadUserImage(val uri: Uri, val email: String): UserEvent

    data object CurrentStorageReference: UserEvent

    data object BodyAnalysis : UserEvent
}