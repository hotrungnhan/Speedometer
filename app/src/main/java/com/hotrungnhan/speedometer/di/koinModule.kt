package com.hotrungnhan.speedometer.di

import android.content.Context
import android.location.LocationManager
import com.hotrungnhan.speedometer.components.LocationProvider
import com.hotrungnhan.speedometer.components.SingleLocationProvider
import com.hotrungnhan.speedometer.db.AppDatabase
import com.hotrungnhan.speedometer.domain.drive.DriveLocalityAddService
import com.hotrungnhan.speedometer.domain.drive.DriveService
import com.hotrungnhan.speedometer.domain.drive.DriveStatAnalyser
import com.hotrungnhan.speedometer.domain.drive.MapPolyLineCreator
import com.hotrungnhan.speedometer.domain.drive.currentDrive.EndForgotCalculator
import com.hotrungnhan.speedometer.domain.drive.currentDrive.PauseCalculator
import com.hotrungnhan.speedometer.domain.drive.drivepath.DrivePathBuilder
import com.hotrungnhan.speedometer.domain.drive.drivepath.DrivePathFilter
import com.hotrungnhan.speedometer.domain.drive.drivepath.PathAngleDiffChecker
import com.hotrungnhan.speedometer.domain.drive.drivepath.SpeedDiffCalculator
import com.hotrungnhan.speedometer.domain.location.LocalityInfoCollector
import com.hotrungnhan.speedometer.domain.preference.UserPreferenceManager
import com.hotrungnhan.speedometer.domain.privacyPolicy.PrivacyPolicyService
import com.hotrungnhan.speedometer.repositories.DriveRepository
import com.hotrungnhan.speedometer.ui.compare.CompareViewModel
import com.hotrungnhan.speedometer.ui.dashboard.DashboardViewModel
import com.hotrungnhan.speedometer.ui.driveReport.DriveReportViewModel
import com.hotrungnhan.speedometer.ui.home.HomeViewModel
import com.hotrungnhan.speedometer.ui.myAllDrive.MyAllDrivesViewModel
import com.hotrungnhan.speedometer.ui.mydrive.MyDrivesViewModel
import com.hotrungnhan.speedometer.ui.splash.SplashViewModel
import com.hotrungnhan.speedometer.utils.ClockUtils
import com.hotrungnhan.speedometer.utils.ConversionUtil
import com.hotrungnhan.speedometer.utils.SphericalUtil
import com.hotrungnhan.speedometer.utils.StateViewProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {

    single { DriveService() }

    single { DriveLocalityAddService(get(), get()) }

    factory { PrivacyPolicyService(get()) }

    factory { StateViewProvider() }

    factory { LocationProvider() }

    factory { androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    factory { ClockUtils() }

    factory { ConversionUtil() }

    factory { SphericalUtil() }

    factory { EndForgotCalculator() }

    factory { DriveRepository(get()) }

    factory { MapPolyLineCreator() }

    factory { DrivePathBuilder() }

    factory { PauseCalculator() }

    factory { PathAngleDiffChecker() }

    factory { SpeedDiffCalculator() }

    factory { DriveStatAnalyser() }

    factory { LocalityInfoCollector(androidContext()) }

    factory { UserPreferenceManager(androidContext()) }

    single { AppDatabase.invoke(androidContext()) }

    factory { SingleLocationProvider(get()) }

    factory { DrivePathFilter() }

    viewModel { HomeViewModel(get()) }

    viewModel { SplashViewModel(get()) }

    viewModel { DashboardViewModel() }

    viewModel { MyDrivesViewModel(get(), get()) }

    viewModel { DriveReportViewModel(get(), get(), get(), get(), get(), get()) }

    viewModel { MyAllDrivesViewModel(get()) }

    viewModel { CompareViewModel(get(), get()) }
}