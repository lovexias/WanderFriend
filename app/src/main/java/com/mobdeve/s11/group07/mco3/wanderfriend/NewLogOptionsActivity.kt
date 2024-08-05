package com.mobdeve.s11.group07.mco3.wanderfriend

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class NewLogOptionsActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var uploadPhotoBtn: Button
    private lateinit var takePhotoBtn: Button

    private lateinit var cameraButton: ImageButton
    private lateinit var journalButton: ImageButton
    private lateinit var mapButton: ImageButton

    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2

    private var selectedCountry: Country? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_log_options)

        // Request permissions at the start
        requestPermissions()

        backBtn = findViewById(R.id.backBtn)
        uploadPhotoBtn = findViewById(R.id.uploadPhotoBtn)
        takePhotoBtn = findViewById(R.id.takePhotoBtn)

        selectedCountry = intent.getParcelableExtra("selectedCountry")

        uploadPhotoBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        takePhotoBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, TAKE_PHOTO_REQUEST)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    TAKE_PHOTO_REQUEST
                )
            }
        }

        backBtn.setOnClickListener {
            finish()
        }

        // FOOTER BUTTONS, this code must be present in every activity with a footer
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
            // Implement start of activity once MapActivity is created
        }
    }

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), REQUEST_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    Log.d("NewLogOptionsActivity", "Permissions granted")
                } else {
                    // Permission denied
                    Toast.makeText(this, "Permissions are required to access media files", Toast.LENGTH_SHORT).show()
                }
            }
            TAKE_PHOTO_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, TAKE_PHOTO_REQUEST)
                } else {
                    Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        Log.d("NewLogOptionsActivity", "Selected image URI: $it")
                        navigateToLogDetailsActivity(it.toString())
                    }
                }
                TAKE_PHOTO_REQUEST -> {
                    val photoUri: Uri? = data?.data
                    if (photoUri == null) {
                        val bitmap = data?.extras?.get("data") as Bitmap
                        val tempUri = getImageUri(bitmap)
                        Log.d("NewLogOptionsActivity", "Taken photo URI: $tempUri")
                        navigateToLogDetailsActivity(tempUri.toString())
                    } else {
                        Log.d("NewLogOptionsActivity", "Taken photo URI: $photoUri")
                        navigateToLogDetailsActivity(photoUri.toString())
                    }
                }
            }
        } else {
            Log.d("NewLogOptionsActivity", "No result obtained")
        }
    }

    private fun navigateToLogDetailsActivity(photoUri: String) {
        Log.d("NewLogOptionsActivity", "Navigating to LogDetailsActivity with URI: $photoUri")
        val intent = Intent(this, LogDetailsActivity::class.java).apply {
            putExtra("photoUri", photoUri)
            putExtra("selectedCountry", selectedCountry)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Add this line
        }
        startActivity(intent)
        finish()
    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    companion object {
        const val REQUEST_PERMISSION_CODE = 100
    }
}