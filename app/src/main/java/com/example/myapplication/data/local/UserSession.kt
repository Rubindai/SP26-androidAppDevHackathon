package com.example.myapplication.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

// Persists the signed-in user across app launches.
// Backed by SharedPreferences (built into Android, no extra deps).
@Singleton
class UserSession @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _userId = MutableStateFlow(prefs.getInt(KEY_USER_ID, -1).takeIf { it > 0 })
    val userId: StateFlow<Int?> = _userId.asStateFlow()

    private val _netid = MutableStateFlow(prefs.getString(KEY_NETID, null))
    val netid: StateFlow<String?> = _netid.asStateFlow()

    private val _scheduleId = MutableStateFlow(prefs.getInt(KEY_SCHEDULE_ID, -1).takeIf { it > 0 })
    val scheduleId: StateFlow<Int?> = _scheduleId.asStateFlow()

    fun setUser(id: Int, netid: String) {
        prefs.edit().putInt(KEY_USER_ID, id).putString(KEY_NETID, netid).apply()
        _userId.value = id
        _netid.value = netid
    }

    fun setScheduleId(scheduleId: Int) {
        prefs.edit().putInt(KEY_SCHEDULE_ID, scheduleId).apply()
        _scheduleId.value = scheduleId
    }

    fun clear() {
        prefs.edit().clear().apply()
        _userId.value = null
        _netid.value = null
        _scheduleId.value = null
    }

    val isSignedIn: Boolean get() = _userId.value != null

    private companion object {
        const val PREFS_NAME = "user_session"
        const val KEY_USER_ID = "user_id"
        const val KEY_NETID = "netid"
        const val KEY_SCHEDULE_ID = "schedule_id"
    }
}
