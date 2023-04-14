package com.example.crop_image

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso

import com.theartofdev.edmodo.cropper.CropImage

class MainActivity : AppCompatActivity() {

 private lateinit var crop : ImageView

    val CAMERA_REQUEST = 100;
    val STORAGE_REQUEST = 200;


    var camerapermission  = arrayOf( android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    var storagepermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var text : TextView = findViewById(R.id.text)
        crop = findViewById(R.id.crop)

        text.setOnClickListener {
              showImageViewDialog()
        }



    }

    private fun showImageViewDialog() {
       var option = arrayOf("Camera","Storage")

        var builder : AlertDialog.Builder=  AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")

        builder.setItems(option,DialogInterface.OnClickListener { dialog, i ->

          if (i==0){
              if (!checkCameraPermission()){
                  requestCameraPermission()
              }else{
                  pickFromGallery()
              }
          }
           else if (i==1){
                if (!checkstoragePermission()){
                    requestStoragePermission()
                }
              else{
                  pickFromGallery()
                }

            }
        })
        builder.create().show()


    }

    private fun requestCameraPermission() {
       requestPermissions(camerapermission,CAMERA_REQUEST)
    }

    private fun checkCameraPermission(): Boolean {
        var result : Boolean = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        var result1 : Boolean = ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return result && result1
    }

    private fun requestStoragePermission() {
        requestPermissions(storagepermission,STORAGE_REQUEST)
    }

    private fun checkstoragePermission(): Boolean {
      var result : Boolean = ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED)
    return result
    }

    private fun pickFromGallery() {
      CropImage.activity().start(this@MainActivity)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode){
         CAMERA_REQUEST -> {
                if (grantResults.size > 0){
                     var camera_accepted : Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    var writeStorageaccepted : Boolean = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (camera_accepted && writeStorageaccepted){

                         pickFromGallery()
                    }
                    else{
                        Toast.makeText(this, "Please Enable Camera and Storage Permission", Toast.LENGTH_SHORT).show()
                    }
                }
         }
          STORAGE_REQUEST -> {
              if (grantResults.size > 0){
                  var writeStorageaccepted : Boolean = grantResults[0] == PackageManager .PERMISSION_GRANTED
                  if (writeStorageaccepted) {
                      pickFromGallery()
                  }
                  else{
                      Toast.makeText(this, "Please Enable Storage Permission", Toast.LENGTH_SHORT).show()
                  }
              }
          }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                Picasso.get().load(resultUri).into(crop)
            }
        }
    }



}