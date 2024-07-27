package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.lifecycle.lifecycle

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var cameraButton: ImageButton
    private lateinit var journalButton: ImageButton
    private lateinit var mapButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)
        mapView.lifecycle().apply {
            onCreate()
        }

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

        // Footer buttons
        cameraButton = findViewById(R.id.cameraButton)
        cameraButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        journalButton = findViewById(R.id.journalButton)
        journalButton.setOnClickListener {
            val intent = Intent(this, JournalMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mapButton = findViewById(R.id.mapButton)
        mapButton.setOnClickListener {
            // Already in MapActivity, do nothing or refresh map
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.lifecycle().onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.lifecycle().onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.lifecycle().onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.lifecycle().onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.lifecycle().onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.lifecycle().onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.lifecycle().onSaveInstanceState(outState)
    }
}
