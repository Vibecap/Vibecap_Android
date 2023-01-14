package com.example.vibecapandroid

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class HomeCaptureActivity : AppCompatActivity() {
    //Manifest 에서 설정한 권한을 가지고 온다.
    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val STORAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)

    //권한 플래그값 정의
    val FLAG_PERM_CAMERA = 98
    val FLAG_PERM_STORAGE = 99

    //카메라와 갤러리를 호출하는 플래그
    val FLAG_REQ_CAMERA = 101
    val FLAG_REA_STORAGE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_home_captured)
        setViews()
    }

    private fun setViews() {
            //카메라 호출 메소드
//            openCamera()
        //앨범 가져오기 메소드
            getAlbum()
    }

    private fun getAlbum(){
        val intent2:Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent2.type = "image/*"
        startActivityForResult(intent2,FLAG_REA_STORAGE)
        
        
    }
    

    private fun openCamera() {
        //카메라 권한이 있는지 확인
            val intent:Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
             startActivityForResult(intent,FLAG_REQ_CAMERA)

        }




    fun saveFile(fileName: String, mimeType: String, bitmap: Bitmap): Uri?
    {
        var CV = ContentValues()
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CV.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)

        if (uri != null) {
            var scriptor = contentResolver.openFileDescriptor(uri, "w")

            if (scriptor != null) {
                val fos = FileOutputStream(scriptor.fileDescriptor)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    CV.clear()
                    CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(uri, CV, null, null)
                }
            }
        }

        return uri;
    }

    fun RandomFileName() : String
    {
        val fineName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fineName
    }


    //startActivityForResult 을 사용한 다음 돌아오는 결과값을 해당 메소드로 호출합니다.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
//                FLAG_REQ_CAMERA ->{
//                    if(data?.extras?.get("data") != null){
//                        //카메라로 방금 촬영한 이미지를 미리 만들어 놓은 이미지뷰로 전달 합니다.
//                        val bitmap = data?.extras?.get("data") as Bitmap
//                        val uri = saveFile(RandomFileName(),"image/jpg",bitmap)
//                        val imageView = findViewById<ImageView>(R.id.imageView)
//                        imageView.setImageBitmap(bitmap)
//                    }
//                }
                FLAG_REA_STORAGE->{
                    data?:return
                    val uri = data.data as Uri
                    val imageView = findViewById<ImageView>(R.id.imageView)
                    imageView.setImageURI(uri)

                }
                else ->{
                    Toast.makeText(this, "사진을 가져오지 못했습니다",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}