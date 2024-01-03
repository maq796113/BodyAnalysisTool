package com.example.bodyanalysistool.viewmodel

interface SessionCache{

    fun saveSession(session: Session)

    fun getActiveSession(): Session?

    fun clearSession()
}
