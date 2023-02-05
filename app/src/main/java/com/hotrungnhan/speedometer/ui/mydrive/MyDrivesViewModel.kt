package com.hotrungnhan.speedometer.ui.mydrive

import androidx.lifecycle.*
import com.hotrungnhan.speedometer.domain.drive.MapPolyLineCreator
import com.hotrungnhan.speedometer.repositories.DriveRepository
import com.hotrungnhan.speedometer.uiModels.DriveItemCollection
import com.hotrungnhan.speedometer.uiModels.DriveItemWithMapData
import com.hotrungnhan.speedometer.utils.RemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyDrivesViewModel(
    private val driveRepository: DriveRepository,
    private val polyLineCreator: MapPolyLineCreator
) : ViewModel() {

    fun getMyDrives(): LiveData<DriveItemCollection> {
        return driveRepository.getDrivesLiveData(
            count = RemoteConfig.getMaximumDrivesToShow()
        ).map { driveList ->

            val driveItemList = driveList.map {
                DriveItemWithMapData(
                    driveId = it.id ?: 0,
                    tag = it.tag,
                    startLocationPoint = it.startLocation,
                    startLocality = it.startLocality,
                    endLocationPoint = it.endLocation,
                    endLocality = it.endLocality,
                    topSpeed = it.topSpeed,
                    distance = it.distance,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    polyLineOptions = polyLineCreator.createLitePolyLineOptions(it.locationPath)
                )
            }

            DriveItemCollection(driveItemList, RemoteConfig.getMaximumDrivesToShow())
        }
    }

    fun getMyDriveStats(): LiveData<DrivesReport> {
        val mediatorLiveData = MediatorLiveData<DrivesReport>()
        var topSpeed = 0.0
        var totalDrives = 0

        mediatorLiveData.addSource(driveRepository.getTopSpeedLiveData()) {
            topSpeed = it ?: 0.0
            mediatorLiveData.value = DrivesReport(topSpeed, totalDrives)
        }

        mediatorLiveData.addSource(driveRepository.getTotalDrives()) {
            totalDrives = it ?: 0
            mediatorLiveData.value = DrivesReport(topSpeed, totalDrives)
        }

        return mediatorLiveData
    }

    fun deleteDrive(driveId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            driveRepository.deleteRace(driveId)
        }
    }

    fun changeTag(driveId: Long, tag: String) {
        viewModelScope.launch(Dispatchers.IO) {
            driveRepository.updateTag(driveId, tag)
        }
    }
}