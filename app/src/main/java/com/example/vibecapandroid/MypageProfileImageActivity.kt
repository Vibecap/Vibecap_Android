package com.example.vibecapandroid

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.vibecapandroid.coms.CheckMypageWritedResponse
import com.example.vibecapandroid.coms.MypageApiInterface
import com.example.vibecapandroid.coms.patchMypageImgResponse
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class MypageProfileImageActivity : AppCompatActivity() {

    val STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val STORAGE_CODE = 99
    var imagebitmap:Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_profile_image)
        initImageViewProfile()

        checkPermission(STORAGE, STORAGE_CODE)

        val profile_image = findViewById<ImageView>(R.id.activity_mypage_profile_imageview)
        profile_image.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MypageProfileActivity::class.java)
            startActivity(intent)
            finish()
        })
    }
    private fun initImageViewProfile(){
        val profile_image = findViewById<ImageView>(R.id.activity_mypage_profile_imageview)
        profile_image.setOnClickListener {
            when{
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED
                ->{
                    GetAlbum()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                -> {
                    showPermissionContextPopup()
                }

                // 권한 요청 하기(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
                else -> requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    GetAlbum()
                else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                //
            }
        }
    }

    fun GetAlbum() {
        Log.d("getalbum","getalbum")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, STORAGE_CODE)

    }
/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 예외처리
        if (resultCode != Activity.RESULT_OK)
            return

        when (requestCode) {
            // 2000: 이미지 컨텐츠를 가져오는 액티비티를 수행한 후 실행되는 Activity 일 때만 수행하기 위해서
            2000 -> {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    val profile_image = findViewById<ImageView>(R.id.activity_mypage_profile_imageview)
                    profile_image.setImageURI(selectedImageUri)
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 못가져옴.", Toast.LENGTH_SHORT).show()
            }
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                STORAGE_CODE -> {
                    val selectedImageUri: Uri? = data?.data
                    val imagebitmap = MediaStore.Images.Media.getBitmap(
                        applicationContext.getContentResolver(),
                        selectedImageUri
                    )

                    if (selectedImageUri != null) {
                        val profile_image = findViewById<ImageView>(R.id.activity_mypage_profile_imageview)
                        profile_image.setImageURI(selectedImageUri)
                        Log.d("profileimg","${profile_image}")
/*
                        val intent = Intent(this, MypageProfileActivity::class.java)
                        startActivity(intent)*/


                            this.imagebitmap = intent.getParcelableExtra<Bitmap>("imagebitmap")
                            val fileName = "VibeCap_Photo" + ".jpg"
                            Log.d("profile_image","${imagebitmap.toString()}")
                            val stream = ByteArrayOutputStream()
                            imagebitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                            val byteArray = stream.toByteArray()
                            val body = RequestBody.create(MediaType.parse("image/*"), byteArray, 0, byteArray.size)
                            val image: MultipartBody.Part = MultipartBody.Part.createFormData("profile_image", fileName ,body)
                            Log.d("파일",profile_image.toString())


                            val apiService = retrofit.create(MypageApiInterface::class.java)
                            apiService.patchMypageImgChange(userToken, MEMBER_ID, image).enqueue(object :
                                Callback<patchMypageImgResponse> {
                                @SuppressLint("ResourceType")
                                override fun onResponse(
                                    call: Call<patchMypageImgResponse>,
                                    response: Response<patchMypageImgResponse>

                                ) {
                                    val responseData = response.body()
                                    Log.d(
                                        "Retrofit",
                                        "MypageResponse\n"+
                                                "isSuccess:${responseData?.is_success}" +
                                                "Code:${responseData?.code}"+
                                                "Message:${responseData?.message}"+
                                                "Result:${responseData?.result}"
                                    )

                                    if (response?.isSuccessful==true) {

                                        if (responseData !== null) {


                                            if(responseData.is_success){
                                                Log.d("responseData","${responseData?.result}")
                                                if(responseData.result.isEmpty()){
                                                    Log.d("프로필 사진 없음","프로필 사진 없음")
                                                }
                                                else{

                                                }
                                            }
                                        }
                                        else{
                                            Log.d("Retrofitnull","Null data") }

                                    } else {
                                        Log.w("Retrofit_notsuccess", "Response Not Successful${response.code()}")
                                    }
                                }

                                override fun onFailure(call: Call<patchMypageImgResponse>, t: Throwable) {
                                    Log.e("Retrofitfail","Error",t)
                                }

                            })


                    } else {
                        Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        } else {
            Log.d("촬영취소", "촬영취소")
            finish()
        }
    }



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
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
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

}

