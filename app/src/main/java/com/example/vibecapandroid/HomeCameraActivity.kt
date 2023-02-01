package com.example.vibecapandroid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import java.lang.Thread.sleep
import java.time.Duration.ofSeconds

var imageuri:Uri? = null


class HomeCameraActivity: AppCompatActivity() {


    // storage 권한 처리에 필요한 변수
    val CAMERA = arrayOf(Manifest.permission.CAMERA)

    val CAMERA_CODE = 98
    val STORAGE_CODE = 99
    var checking_camera: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestingPermission(CAMERA,CAMERA_CODE)
        CallCamera()
    }





    // 카메라 권한, 저장소 권한
    // 요청 권한
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인해 주세요", Toast.LENGTH_LONG).show()
                    }
                    else{
                        val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(itt, CAMERA_CODE)
                    }
                }
            }

        }
    }

    // 다른 권한등도 확인이 가능하도록
    fun requestingPermission(permissions: Array<out String>, type: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, type)
                    return false
                }

            }
        }
        return true
    }

    // 카메라 촬영 - 권한 처리

     fun CallCamera() {
           if(requestingPermission(CAMERA, CAMERA_CODE)){
               Log.d("callcamera","callcamera")
                val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(itt, CAMERA_CODE)
           }

        }






    // 결과
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            val imageView = findViewById<ImageView>(R.id.picture)

            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    CAMERA_CODE -> {
                        if (data?.extras?.get("data") != null) {
                            val img = data?.extras?.get("data") as Bitmap

                            val nextIntent = Intent(this, HomeCapturedActivity::class.java)
                            nextIntent.putExtra("imagebitmap", img)
                            startActivity(nextIntent)
                            finish()
                        }


                    }
                }
            } else {
                Log.d("촬영취소", "촬영취소")
                finish()
            }


        }



    }
