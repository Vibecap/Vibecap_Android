package com.example.vibecapandroid

import android.Manifest
import android.R
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat


class HistoryPhoneGalleryActivity : AppCompatActivity() {

    val STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val STORAGE_CODE = 99
    var realUri:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //저장소 읽기 쓰기 권환 획득 및 내장 갤러리 사용
        checkPermission(STORAGE, STORAGE_CODE)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "저장소 권한 승인완료", Toast.LENGTH_LONG).show()
                    GetAlbum()
                }
            }
        }
    }

    fun checkPermission(permissions: Array<out String>, type: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, type)
                    Log.d("권한","권한없음")
                    return false
                }
                else{
                    GetAlbum()
                    return true
                }
            }
        }
        GetAlbum()
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                STORAGE_CODE -> {
                    feeling = " "
                    val uri = data?.data
                    val intent = Intent(this, HomeCapturedActivity::class.java)
                    intent.putExtra("image_uri", uri)
                    intent.putExtra("frag_code",3)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            Log.d("촬영취소", "촬영취소")
            finish()
        }
    }
    //원본 이미지를 저장할 Uri를 MediaStore(데이터베이스)에 생성하는 메소드
    fun createImageUri(filename:String,mimeType:String) : Uri?{
        val values= ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE,mimeType)

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
    }

    //파일 이름을 생성하는 메서드
    fun newFileName():String{
        val sdf= SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename=sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }


  /*  //bitmap scaleDown (사이즈만 조절) (퀄리티는 같음)
    fun scaleDown(bitmap : Bitmap): Bitmap {
//        val quality = if(bitmap.width > 2048 && bitmap.height > 2048) {
//            0.3
//        }else if(bitmap.width > 1024 && bitmap.height > 1024){
//            0.5
//        }else{
//            0.8
//        }
        val scaleDown = Bitmap.createScaledBitmap(bitmap, 160, 160, true)
        return scaleDown
    }*/


    // 갤러리 취득
    fun GetAlbum() {
        Log.d("getalbum","getalbum")
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, STORAGE_CODE)

    }
}
