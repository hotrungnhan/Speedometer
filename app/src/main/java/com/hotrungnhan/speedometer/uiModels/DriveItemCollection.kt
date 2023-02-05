package com.hotrungnhan.speedometer.uiModels

data class DriveItemCollection(
    val driveItemWithMapList: List<DriveItemWithMapData>,
    val maxFetched: Int
) {
    fun isEmpty() = driveItemWithMapList.isEmpty()
}