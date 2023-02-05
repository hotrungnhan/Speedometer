package com.hotrungnhan.speedometer.ui.compare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotrungnhan.speedometer.domain.drive.drivepath.DrivePathFilter
import com.hotrungnhan.speedometer.repositories.DriveRepository
import com.hotrungnhan.speedometer.uiModels.ComparableDriveData
import com.hotrungnhan.speedometer.uiModels.CompareData
import com.hotrungnhan.speedometer.uiModels.LocationWithTime
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class CompareViewModel(
    private val driveRepository: DriveRepository,
    private val drivePathFilter: DrivePathFilter
) : ViewModel() {

    private val _compareLiveData = MutableLiveData<CompareData>()
    val compareLiveData: LiveData<CompareData> = _compareLiveData

    fun fetchDataToCompare(
        currentDriveId: Long,
        compareDriveId: Long,
    ) {
        viewModelScope.launch {

            val currentDrive = driveRepository.getDrive(currentDriveId)
            val currentDrivePath =
                drivePathFilter.filter(driveRepository.getDrivePath(currentDriveId) ?: emptyList())

            if (currentDrive == null || currentDrivePath.isEmpty()) {
                return@launch
            }

            val timeScalerValue = when {
                currentDrive.getTotalTime() > TimeUnit.MILLISECONDS.toMinutes(5) -> {
                    TimeUnit.MINUTES.toSeconds(1).toDouble()
                        .div(TimeUnit.MILLISECONDS.toSeconds(currentDrive.getTotalTime()))
                }
                currentDrive.getTotalTime() > TimeUnit.MILLISECONDS.toMinutes(1) -> {
                    30.0.div(TimeUnit.MILLISECONDS.toSeconds(currentDrive.getTotalTime()))
                }
                else -> {
                    1.0
                }
            }

            if (compareDriveId == currentDriveId) {
                val compareData = CompareData(
                    comparableDriveData1 = ComparableDriveData(
                        startTime = currentDrive.startTime,
                        endTime = currentDrive.endTime,
                        distance = currentDrive.distance,
                        topSpeed = currentDrive.topSpeed,
                        path = currentDrivePath.map { p ->
                            LocationWithTime(
                                p.locationPoint.latitude,
                                p.locationPoint.longitude,
                                ((p.time - currentDrive.startTime) * timeScalerValue).toLong()
                            )
                        }
                    ),
                    comparableDriveData2 = null
                )
                _compareLiveData.postValue(compareData)
            } else {

                val compareDrive = driveRepository.getDrive(compareDriveId)
                val compareDrivePath = drivePathFilter.filter(
                    driveRepository.getDrivePath(compareDriveId) ?: emptyList()
                )

                if (compareDrive == null || compareDrivePath.isEmpty()) {
                    return@launch
                }


                val compareData = CompareData(
                    comparableDriveData1 = ComparableDriveData(
                        startTime = currentDrive.startTime,
                        endTime = currentDrive.endTime,
                        distance = currentDrive.distance,
                        topSpeed = currentDrive.topSpeed,
                        path = currentDrivePath.map { p ->
                            LocationWithTime(
                                p.locationPoint.latitude,
                                p.locationPoint.longitude,
                                ((p.time - currentDrive.startTime) * timeScalerValue).toLong()
                            )
                        }
                    ),
                    comparableDriveData2 = ComparableDriveData(
                        startTime = compareDrive.startTime,
                        endTime = compareDrive.endTime,
                        distance = compareDrive.distance,
                        topSpeed = compareDrive.topSpeed,
                        path = compareDrivePath.map { p ->
                            LocationWithTime(
                                p.locationPoint.latitude,
                                p.locationPoint.longitude,
                                ((p.time - compareDrive.startTime) * timeScalerValue).toLong()
                            )
                        }
                    ),
                )
                _compareLiveData.postValue(compareData)
            }
        }
    }
}