package com.sample.locationrecorder

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val TAG = "LaunchingActivity"
    private val REQUEST_CODE = 101
    private var selectedDir: Uri? = null

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    private val dirSelector = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            Log.d(TAG, data.toString())
            selectedDir = data
        }

        launchMainActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (!doGetPermissions()) {
            requestPermissions(permissions, REQUEST_CODE)
        }

        if (selectedDir == null) {
            openDirectory()
        }

        launchMainActivity()
    }

    private fun launchMainActivity() {
        val intent = Intent(this, RecordActivity::class.java).apply {
            putExtra("dir", selectedDir)
        }
        startActivity(intent)
        finish()
    }

    private fun doGetPermissions() : Boolean {

        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openDirectory(/*pickerInitialUri: Uri*/) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)//.apply {
            //putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        //}

        dirSelector.launch(intent)
    }
}