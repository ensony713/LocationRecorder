package com.sample.locationrecorder

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class RecordActivity : AppCompatActivity() {

    private val TAG = "RecordLocation"
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private var path: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "main activity onCreate is called")

        enableEdgeToEdge()
        setContentView(R.layout.activity_record)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (intent.hasExtra("dir")) {
            path = intent.getParcelableExtra("dir")
            Log.d(TAG, "get $path")
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        initLocationClient()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun initLocationClient() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationRequest = LocationRequest.create().apply {
            interval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            super.onLocationResult(location)
            for (loc in location.locations) {
                Log.d("LocationListener", "lat" + loc.latitude + ", log" + loc.longitude)
            }
        }
    }
}