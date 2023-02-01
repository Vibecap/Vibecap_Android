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
import java.time.Duration.ofSeconds

var imageuri:Uri? = null


class HomeCameraActivity: AppCompatActivity() {


    // storage 권한 처리에 필요한 변수
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val CAMERA_CODE = 98
    val STORAGE_CODE = 99
    var checking_camera: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_home_camera)
//        checking_camera = requestPermission(CAMERA, CAMERA_CODE)
//        Log.d("checkingcamera",checking_camera.toString())
        //requestPermission(CAMERA, CAMERA_CODE)
//        if(checking_camera) {
//            val checking_storage: Boolean = requestPermission(STORAGE, STORAGE_CODE)
//        }

        checking_camera= requestPermission(CAMERA,CAMERA_CODE)
        Log.d("checkingcamera",checking_camera.toString())
        Log.d("messs","first")
        Log.d("messs","second")
        Log.d("messs","third")
        Log.d("messs","four")
        CallCamera(checking_camera)

        // 카메라
        //val camera = findViewById<Button>(R.id.camera)
        //camera.setOnClickListener {
        //CallCamera()
        //}

//        // 사진저장
//        val picture = findViewById<Button>(R.id.gallery)
//        picture.setOnClickListener {
//            GetAlbum()
//        }

    }



//    override fun onStart() {
//        super.onStart()
//        CallCamera()
//
//
//    }


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
                }
            }
            STORAGE_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인해 주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    // 다른 권한등도 확인이 가능하도록
    fun requestPermission(permissions: Array<out String>, type: Int): Boolean {
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

     fun CallCamera(check: Boolean) {
//            if(checking_camera!=true) {
//                checking_camera = requestPermission(CAMERA, CAMERA_CODE)
//            }
//            else {
                Log.d("check", check.toString())
                val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(itt, CAMERA_CODE)
//            }

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

                    STORAGE_CODE -> {
                        val uri = data?.data
                        imageView.setImageURI(uri)
                        imageuri = uri
                        //val uriString = uri.toString()
                        val nextIntent = Intent(this, HomeCapturedActivity::class.java)
                        //nextIntent.putExtra("uri",uriString)
                        startActivity(nextIntent)
                    }


                }
            } else {
                Log.d("촬영취소", "촬영취소")
                finish()
            }


        }


        // 갤러리 취득
//    fun GetAlbum(){
//        if(checkPermission(STORAGE, STORAGE_CODE)){
//            val itt = Intent(Intent.ACTION_PICK)
//            itt.type = MediaStore.Images.Media.CONTENT_TYPE
//            startActivityForResult(itt, STORAGE_CODE)
//        }
//    }


    }
