package com.example.bodyanalysistool.usecases

import com.example.bodyanalysistool.data.StorageRef
import com.example.bodyanalysistool.firebase.storage.repository.StorageRepository

class CurrentStorageReference(
    private val repository: StorageRepository
) {
    operator fun invoke(): StorageRef {
        return repository.currentStorageRef
    }
}