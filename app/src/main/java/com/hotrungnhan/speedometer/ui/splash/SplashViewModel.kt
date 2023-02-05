package com.hotrungnhan.speedometer.ui.splash

import androidx.lifecycle.ViewModel
import com.hotrungnhan.speedometer.domain.privacyPolicy.PrivacyPolicyService


class SplashViewModel(
    private val privacyPolicyService: PrivacyPolicyService,
) : ViewModel() {

    fun isPrivacyPolicyAccepted(): Boolean {
        return privacyPolicyService.isPolicyAccepted()
    }

    fun acceptPrivacyPolicy() {
        privacyPolicyService.onAcceptPolicy()
    }
}