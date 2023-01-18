package com.example.vibecapandroid

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vibecapandroid.databinding.ActivityHomeCamera2Binding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HomeCamera2Activity: AppCompatActivity()  {

    // ViewBinding
    private lateinit var binding: ActivityHomeCamera2Binding //view binding 설정
    private var imageCapture: ImageCapture? = null
    private var preview : Preview? = null
    private lateinit var cameraExecutor: ExecutorService
    // CameraController
    private var camera : Camera? = null
    private var cameraController : CameraControl? = null
    private var cameraInfo: CameraInfo? = null


    private var imageUri :Uri? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeCamera2Binding.inflate(layoutInflater) //view binding 설정
        setContentView(binding.root) //화면 출력

        // 필요한 권한이 모두 승인된 경우 카메라 프리뷰를 시작합니다. 권한 승인이 안되어 있을 경우 권한을 요청합니다.
        if (allPermissionsGranted()) {
            startCamera() //카메라 프리뷰 시작
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.captureBtn.setOnClickListener {
            takePhoto()
        } //사진 저장
        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.btnTorch.setOnClickListener {
            when (cameraInfo?.torchState?.value) {
                TorchState.ON -> {
                    cameraController?.enableTorch(false)
                    binding.btnTorch.text = "Torch ON"
                }
                TorchState.OFF -> {
                    cameraController?.enableTorch(true)
                    binding.btnTorch.text = "Torch OFF"
                }
            }
        }

        binding.Album.setOnClickListener {
            GetAlbum()
            binding.previewView.visibility = View.GONE
            binding.btnTorch.visibility =View.GONE
            binding.Album.visibility =View.GONE
            binding.captureBtn.visibility =View.GONE
            binding.imageConfirm.visibility=View.VISIBLE

        }

        binding.imageConfirm.setOnClickListener{
            val intent = Intent(this,HomeCameraActivity::class.java)

            intent.putExtra("imageUri",imageUri)
            startActivity(intent)

            finish()
        }

    }

    private fun GetAlbum() {
            val itt = Intent(Intent.ACTION_PICK)
            itt.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(itt, 3000)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){

                3000 -> {
                    val uri = data?.data
                    binding.previewView.visibility = View.GONE
                    binding.CapturedImage.setImageURI(uri)
                }
            }
        }
    }

    //권한살정
    private fun allPermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    //권한 설정완료후
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this, "Permissions not granted by the user.", Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    //카메라 프리뷰시작
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }
            // ImageCapture
            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture)
                cameraController = camera!!.cameraControl
                cameraInfo = camera!!.cameraInfo

            } catch(exc: Exception) {
                Log.d("CameraX-Debug", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }




    //사진 저장
    private fun takePhoto() {

        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    binding.previewView.visibility = View.GONE

                    binding.captureBtn.visibility = View.GONE
                    binding.btnTorch.visibility = View.GONE
                    binding.Album.visibility = View.GONE

                    binding.CapturedImage.setImageURI(output.savedUri)
                    binding.imageConfirm.visibility = View.VISIBLE
                    imageUri = output.savedUri
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            }
        )
    }
    
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    companion object {
        private const val TAG ="HomeCamera2Activity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        // 필수 권한 목록입니다.
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA //카메라 권한 추가
        ).apply {
            //  외부 저장소 쓰기 권한을 추가합니다.
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}


