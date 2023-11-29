package com.dicoding.courseschedule.ui.setting

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference Done✅
        val switchDarkMode: ListPreference? = findPreference(getString(R.string.pref_key_dark))
        switchDarkMode?.setOnPreferenceChangeListener { _, newValue ->
            val nightModeValue = when (newValue.toString()) {
                getString(R.string.pref_dark_auto) -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) NightMode.AUTO.value else NightMode.ON.value
                getString(R.string.pref_dark_off) -> NightMode.OFF.value
                else -> NightMode.ON.value
            }
            updateTheme(nightModeValue)
            true
        }
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference Done✅
        val switchNotification: SwitchPreference? = findPreference(getString(R.string.pref_key_notify))
        switchNotification?.setOnPreferenceChangeListener { _, newValue ->
            val broadcast = DailyReminder()
            when (newValue) {
                true -> {
                    broadcast.setDailyReminder(requireContext())
                }
                false -> {
                    broadcast.cancelAlarm(requireContext())
                }
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}