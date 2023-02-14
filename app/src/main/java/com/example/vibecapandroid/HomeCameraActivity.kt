package com.example.vibecapandroid

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vibecapandroid.databinding.ActivityHomeCameraBinding
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat




class HomeCameraActivity: AppCompatActivity() {


    // storage 권한 처리에 필요한 변수
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val CAMERA_CODE = 98
    private val viewBinding: ActivityHomeCameraBinding by lazy {
        ActivityHomeCameraBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //카메라 권한 획득 및 내장 카메라로 이동
        checkPermission(CAMERA,CAMERA_CODE)

    }

    // 카메라 권한, 저장소 권한
    // 요청 권한
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "카메라 권한 승인완료", Toast.LENGTH_LONG).show()
                    CallCamera()
                }
            }

        }
    }

    // 다른 권한등도 확인이 가능하도록
    fun checkPermission(permissions: Array<out String>, type: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, type)
                    return false
                }
                else{
                    CallCamera()
                    return true
                }
            }
        CallCamera()
        return true
    }
    //원본 이미지의 주소를 저장할 변수
    var realUri:Uri?=null

    // 카메라에 찍은 사진을 저장하기 위한 Uri를 넘겨준다.
     fun CallCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageUri(newFileName(),"image/jpg")?.let { uri ->
            realUri=uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT,realUri)
            startActivityForResult(intent, CAMERA_CODE)
        }
     }

    //원본 이미지를 저장할 Uri를 MediaStore(데이터베이스)에 생성하는 메소드
    fun createImageUri(filename:String,mimeType:String) : Uri?{
        val values=ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE,mimeType)

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
    }

    //파일 이름을 생성하는 메서드
    fun newFileName():String{
        val sdf=SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename=sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }

    //원본 이미지를 불러오는 메소드
    fun loadBitmap(photoUri:Uri):Bitmap?{
        var image:Bitmap?=null
        try{
           if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O_MR1){
                val source=ImageDecoder.createSource(contentResolver,photoUri)
               image=  ImageDecoder.decodeBitmap(source)
            }else{
               image= MediaStore.Images.Media.getBitmap(contentResolver,photoUri)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return image
    }


    // 결과
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_CODE -> {
                    realUri?.let { uri->
                        val intent = Intent(this, HomeCapturedActivity::class.java)
                        intent.putExtra("image_uri",realUri)
                        intent.putExtra("frag_code",2)
                        startActivity(intent)
                        finish()
                        Log.d("URI Path of Storage","$realUri")
                    }
                }
            }
        } else {
            Log.d("촬영취소", "촬영취소")
            finish()
        }
    }


}
