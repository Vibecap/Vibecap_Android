package com.example.vibecapandroid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class HistoryPhoneGalleryActivity : AppCompatActivity() {

    val STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val STORAGE_CODE = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //내장 갤러리 사용
        checkPermission(STORAGE, STORAGE_CODE)

//        val itt = Intent(Intent.ACTION_PICK)
//        itt.type = MediaStore.Images.Media.CONTENT_TYPE
//        startActivityForResult(itt, STORAGE_CODE)
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
                    Toast.makeText(this, "저장소 권한을 승인완료", Toast.LENGTH_LONG).show()
                    GetAlbum()
                    Log.d("onRequestPermission","onRequestPermission")
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
                    Log.d("여기?","여기?")
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
                    //uri 얻고 bitmap 으로 변경
                    val uri = data?.data
                    val imagebitmap = MediaStore.Images.Media.getBitmap(
                        applicationContext.getContentResolver(),
                        uri
                    )
//                    val stream = ByteArrayOutputStream()
//                    imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//                    val byteArray = stream.toByteArray()
                    //감정 없음
                    feeling = " "

                    Log.d("imagebitmap", "imagebitmap")

                    //intent로 bitmap 넘겨주고 다음 activity 실행
                    val nextIntent = Intent(this, HomeCapturedActivity::class.java)
                    nextIntent.putExtra("imagebitmap", imagebitmap)
                    startActivity(nextIntent)
                    finish()
                }
            }
        } else {
            Log.d("촬영취소", "촬영취소")
            finish()
        }
    }

    // 갤러리 취득
    fun GetAlbum() {
        // if (checkPermission(STORAGE, STORAGE_CODE)) {

        Log.d("getalbum","getalbum")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, STORAGE_CODE)

        //}
        //}
    }
}
