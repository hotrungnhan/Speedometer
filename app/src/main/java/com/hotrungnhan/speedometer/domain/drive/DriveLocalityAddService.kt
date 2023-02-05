package com.hotrungnhan.speedometer.domain.drive

import com.hotrungnhan.speedometer.domain.location.LocalityInfoCollector
import com.hotrungnhan.speedometer.repositories.DriveRepository

class DriveLocalityAddService(
    private val driveRepository: DriveRepository,
    private val localityInfoCollector: LocalityInfoCollector
) {

    suspend fun addLocalityInformation() {

        val drives = driveRepository.getDrivesWithNoLocalityInfo()

        drives.forEach {
            getAndUpdateLocality(it)
        }
    }

    suspend fun getAndUpdateLocality(drive: Drive): Drive? {
        val startLocality = localityInfoCollector.getLocalityInfo(
            drive.startLocation.latitude,
            drive.startLocation.longitude
        )
        val endLocality = localityInfoCollector.getLocalityInfo(
            drive.endLocation.latitude,
            drive.endLocation.longitude
        )

        return if (startLocality != null && endLocality != null) {
            driveRepository.updateDriveLocality(drive.getId(), startLocality, endLocality)
            drive.copy(startLocality = startLocality, endLocality = endLocality)
        } else {
            null
        }
    }
}