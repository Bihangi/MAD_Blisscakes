package com.example.blisscakes

import android.app.Application
import com.example.blisscakes.utils.BatteryMonitor
import com.example.blisscakes.utils.DeviceSensorManager
import com.example.blisscakes.utils.NetworkMonitor

class BlissCakesApplication : Application() {

    lateinit var networkMonitor: NetworkMonitor
        private set

    lateinit var sensorManager: DeviceSensorManager
        private set

    lateinit var batteryMonitor: BatteryMonitor
        private set

    override fun onCreate() {
        super.onCreate()

        // Initialize utilities
        networkMonitor = NetworkMonitor(this)
        sensorManager = DeviceSensorManager(this)
        batteryMonitor = BatteryMonitor(this)

        // Start monitoring
        sensorManager.startListening()
        batteryMonitor.startMonitoring()
    }

    override fun onTerminate() {
        super.onTerminate()
        sensorManager.stopListening()
        batteryMonitor.stopMonitoring()
        networkMonitor.cleanup()
    }
}