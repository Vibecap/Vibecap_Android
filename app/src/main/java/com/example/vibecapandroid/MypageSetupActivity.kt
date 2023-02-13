package com.example.vibecapandroid

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.example.vibecapandroid.coms.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class MypageSetupActivity : AppCompatActivity() {


    var setupList1 = arrayListOf<MypageSetupClass>(
        MypageSetupClass(R.drawable.ic_activity_mypage_setup_profile,"프로필 이미지 변경",R.drawable.ic_activity_mypage_profile_next),
        MypageSetupClass(R.drawable.ic_activity_mypage_setup_edit,"닉네임 변경",R.drawable.ic_activity_mypage_profile_next),
        MypageSetupClass(R.drawable.ic_activity_mypage_setup_alarm,"알림 설정",R.drawable.ic_activity_mypage_profile_next)
    )
    var setupList2 = arrayListOf<MypageSetupClass>(
        MypageSetupClass(R.drawable.ic_activity_mypage_profile_act,"구글 계정 연동하기",R.drawable.ic_activity_mypage_profile_next),
        MypageSetupClass(R.drawable.ic_activity_mypage_setup_logout,"로그아웃",0),
        MypageSetupClass(R.drawable.ic_activity_mypage_setup_logout,"회원 탈퇴",0)
    )
    var setupList3 = arrayListOf<MypageSetupClass>(
        MypageSetupClass(R.drawable.ic_activity_mypage_setup_notion,"공지사항",R.drawable.ic_activity_mypage_profile_next),
        MypageSetupClass(R.drawable.ic_activity_mypage_setup_inquiry,"문의하기/신고하기",R.drawable.ic_activity_mypage_profile_next),
        MypageSetupClass(R.drawable.ic_activity_mypage_setup_list,"오픈소스 라이센스",R.drawable.ic_activity_mypage_profile_next)
    )
    val STORAGE_CODE = 99
    fun GetAlbum() {
        Log.d("getalbum","getalbum")
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, STORAGE_CODE)

    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_setuplist)

        val Adapter1 = MypageSetupadaptersClass(this, setupList1)
        val activity_mypage_setuplist1 = findViewById<ListView>(R.id.activity_mypage_setuplist1)
        activity_mypage_setuplist1.adapter = Adapter1

        activity_mypage_setuplist1.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                if(position==0){
                    val selectItem = parent.getItemAtPosition(position) as MypageSetupClass
                    GetAlbum()
                }
                if(position==1){
                    val selectItem = parent.getItemAtPosition(position) as MypageSetupClass
                    val intent = Intent(this,MypageNicknameActivity::class.java)
                    intent.putExtra("닉네임 변경",setupList1[position].profile_textview)
                    startActivity(intent)}
                if (position == 2) {
                    val selectItem = parent.getItemAtPosition(position) as MypageSetupClass
                    val intent = Intent(this, MypageNoticeSetupActivity::class.java)
                    intent.putExtra("알림 설정", setupList1[position].profile_textview)
                    startActivity(intent)
                }
            }

        val Adapter2 = MypageSetupadaptersClass(this, setupList2)
        val activity_mypage_setuplist2 = findViewById<ListView>(R.id.activity_mypage_setuplist2)
        activity_mypage_setuplist2.adapter = Adapter2

        activity_mypage_setuplist2.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                if(position==0){
                    val selectItem = parent.getItemAtPosition(position) as MypageSetupClass
                }
                if (position == 1) {
                    /*val selectItem = parent.getItemAtPosition(position) as MypageSetupClass
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃",
                            DialogInterface.OnClickListener { dialog, id ->
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            })
                        .setNegativeButton("취소",
                            DialogInterface.OnClickListener { dialog, id ->
                            })
                    builder.show()*/
                    val dialogBinding = layoutInflater.inflate(R.layout.activity_mypage_dialog_logout,null)

                    val logoutDialog = Dialog(this)
                    logoutDialog.setContentView(dialogBinding)
                    logoutDialog.setCancelable(true)
                    logoutDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    logoutDialog.show()

                    val cancelBtn =dialogBinding.findViewById<Button>(R.id.dialog_mypage_logout_cancel)
                    cancelBtn.setOnClickListener{
                        val intent = Intent(this, MypageSetupActivity::class.java)
                        startActivity(intent)
                    }

                    val logoutBtn =dialogBinding.findViewById<Button>(R.id.dialog_mypage_logout_logout)
                    logoutBtn.setOnClickListener{
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }

                }
                if (position==2){

                    val dialogBinding = layoutInflater.inflate(R.layout.activity_mypage_dialog_quit,null)

                    val quitDialog = Dialog(this)
                    quitDialog.setContentView(dialogBinding)
                    quitDialog.setCancelable(true)
                    quitDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    quitDialog.show()

                    val cancelBtn =dialogBinding.findViewById<Button>(R.id.dialog_mypage_quit_cancel)
                    cancelBtn.setOnClickListener{
                        val intent = Intent(this, MypageSetupActivity::class.java)
                        startActivity(intent)
                    }

                    val quitBtn =dialogBinding.findViewById<Button>(R.id.dialog_mypage_quit_quit)
                    quitBtn.setOnClickListener{

                        val dialogBinding = layoutInflater.inflate(R.layout.activity_mypage_dialog_quit_real,null)

                        val quitRealDialog = Dialog(this)
                        quitRealDialog.setContentView(dialogBinding)
                        quitRealDialog.setCancelable(true)
                        quitRealDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        quitRealDialog.show()

                        val cancelBtn =dialogBinding.findViewById<Button>(R.id.dialog_mypage_quitreal_cancel)
                        cancelBtn.setOnClickListener{
                            val intent = Intent(this, MypageSetupActivity::class.java)
                            startActivity(intent)
                        }

                        val quitrealBtn = dialogBinding.findViewById<Button>(R.id.dialog_mypage_quitreal_quit)
                        quitrealBtn.setOnClickListener{
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)



                            //웹 브라우저 창 열기
                            val retrofit = Retrofit.Builder()
                                .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

                            //어떤 주소로 들어갈지 입력
                            val apiService = retrofit.create(MypageApiInterface::class.java)

                            Log.d("userToken","$userToken")
                            //입력한 주소중 하나로 연결 시도
                            apiService.patchMypageQuit(userToken,patchMypageQuitInput(MEMBER_ID)).enqueue(object :Callback<patchMypageQuitResponse> {
                                override fun onResponse(
                                    call: Call<patchMypageQuitResponse>,
                                    response: Response<patchMypageQuitResponse>
                                ) {

                                    if (response.isSuccessful) {
                                        val responseData = response.body()

                                        if (responseData !== null) {
                                            Log.d(
                                                "Retrofit",
                                                "MypageResponse\n"+
                                                        "isSuccess:${responseData.is_success}" +
                                                        "Code:${responseData.code}"+
                                                        "Message:${responseData.message}"+
                                                        "Result:${responseData.result.nickname}"

                                            )

                                        }
                                        else{
                                            Log.d("Retrofit","Null data") }

                                    } else {
                                        Log.w("Retrofit", "Response Not Successful${response.code()}")
                                    }
                                }

                                override fun onFailure(call: Call<patchMypageQuitResponse>, t: Throwable) {
                                    Log.e("Retrofit","Error",t)
                                }

                            })
                        }


                    }
                }


                }

                val Adapter3 = MypageSetupadaptersClass(this, setupList3)
                val activity_mypage_setuplist3 =
                    findViewById<ListView>(R.id.activity_mypage_setuplist3)
                activity_mypage_setuplist3.adapter = Adapter3

                activity_mypage_setuplist3.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->

                    //노션 하이퍼링크 이동 (작동안함)
                    if (position == 0) {
                        val selectItem = parent.getItemAtPosition(position) as MypageSetupClass
                        intent.setData(Uri.parse("https://www.notion.so/VibeCap-c03b628aad594738b150c71dcc44cc67"));
                        startActivity(intent);
                    }
                    if (position == 1) {

                    }
                    if (position == 2) {

                    }
                }


                val mypage_back = findViewById<ImageView>(R.id.activity_mypage_nickname_close)
                mypage_back.setOnClickListener(View.OnClickListener {
                    finish()
                })
            }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
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
                        val fileName = "VibeCap_Photo" + ".jpg"
                        val stream = ByteArrayOutputStream()
                        imagebitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val byteArray = stream.toByteArray()
                        val body = RequestBody.create(MediaType.parse("image/*"), byteArray, 0, byteArray.size)
                        val image: MultipartBody.Part = MultipartBody.Part.createFormData("profile_image", fileName ,body)

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
                                if (response.isSuccessful) {
                                    Toast.makeText(this@MypageSetupActivity,
                                        "프로필 사진 변경이 완료되었습니다.",
                                        Toast.LENGTH_SHORT).show()
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
}