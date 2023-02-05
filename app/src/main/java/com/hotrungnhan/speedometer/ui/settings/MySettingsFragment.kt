package com.hotrungnhan.speedometer.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.hotrungnhan.speedometer.R
import com.hotrungnhan.speedometer.domain.preference.UserPreferenceManager
import com.hotrungnhan.speedometer.utils.ViewUtils
import org.koin.android.ext.android.inject

class MySettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val userPreferenceManager: UserPreferenceManager by inject()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "theme") {
            ViewUtils.setTheme(userPreferenceManager.getTheme())
        }
    }
}