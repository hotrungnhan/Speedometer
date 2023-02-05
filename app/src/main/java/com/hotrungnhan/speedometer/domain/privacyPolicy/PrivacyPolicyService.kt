package com.hotrungnhan.speedometer.domain.privacyPolicy

import com.hotrungnhan.speedometer.domain.preference.UserPreferenceManager

class PrivacyPolicyService(private val userPreferenceManager: UserPreferenceManager) {

    fun isPolicyAccepted() = userPreferenceManager.getPrivacyPolicyReadStatus()

    fun onAcceptPolicy() = userPreferenceManager.setPrivacyPolicyReadStatus()
}