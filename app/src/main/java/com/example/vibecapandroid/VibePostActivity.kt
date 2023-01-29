package com.example.vibecapandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.vibecapandroid.coms.PostDetailResponse
import com.example.vibecapandroid.coms.VibePostApiInterface
import com.example.vibecapandroid.databinding.ActivityVibePostBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class VibePostActivity : AppCompatActivity() {
    val viewBinding : ActivityVibePostBinding by lazy {
        ActivityVibePostBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)


        // 툴바
        val toolbar = findViewById<Toolbar>(R.id.toolBar_top)
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // API
        requestToRestAPI()

        // 댓글창 열기
        viewBinding.imageButtonComment.setOnClickListener {
            val intent = Intent(this, VibeCommentActivity::class.java)
            startActivity(intent)
        }

        // 게시글 popup
        viewBinding.articlePopup.setOnClickListener {
            // Dialog만들기
            val mDialogView =
                LayoutInflater.from(this).inflate(R.layout.activity_popup, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            val block_table = mDialogView.findViewById<Button>(R.id.dialog_table_block)
            block_table.setOnClickListener {

                Toast.makeText(this, "게시물을 차단했습니다.", Toast.LENGTH_SHORT).show()
                // 차단 API
            }

            val declaration_table = mDialogView.findViewById<Button>(R.id.dialog_table_declaration)
            declaration_table.setOnClickListener {

                var intent = Intent(this, VibePopupActivity::class.java)
                startActivity(intent)
            }

            val copy_table = mDialogView.findViewById<Button>(R.id.dialog_table_copy)
            copy_table.setOnClickListener {

                Toast.makeText(this, "링크를 복사했습니다.", Toast.LENGTH_SHORT).show()
                // 링크 복사 API
            }

            val share_table = mDialogView.findViewById<Button>(R.id.dialog_table_share)
            share_table.setOnClickListener {

                Toast.makeText(this, "토스트 메시지", Toast.LENGTH_SHORT).show()
                // 게시물 공유 API
            }
        }
    }

    // 툴바
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {}
        }

        return super.onOptionsItemSelected(item)
    }

    // api
    object RetrofitObject {
        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getApiService():VibePostApiInterface{
            return getRetrofit().create(VibePostApiInterface::class.java)
        }
    }

    private fun requestToRestAPI(){
        RetrofitObject.getApiService().postDetailCheck(7).enqueue(object : Callback<PostDetailResponse>{
            // api 호출 성공시
            override fun onResponse(call: Call<PostDetailResponse>, response: Response<PostDetailResponse>) {
                Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
                setTitleText(response.code(), response.body())
                setVibeIdImage(response.code(), response.body())
                setNumber(response.code(),response.body())
                setTagName(response.code(),response.body())
                setNicknameText(response.code(), response.body())
                setPostText(response.code(), response.body())
                setProfileImg(response.code(),response.body())
                val responseData = response.body()
                Log.d(
                    "postCapture",
                    "postCapture\n"+
                            "isSuccess:${responseData?.is_success}\n " +
                            "Code: ${responseData?.code} \n" +
                            "Message:${responseData?.message} \n" )
            }

            // api 호출 실패시
            override fun onFailure(call: Call<PostDetailResponse>, t: Throwable) {
                Log.e("retrofit onFailure", "${t.message.toString()}")
                Toast.makeText(applicationContext, "fail", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // post id 설정
    // member id 설정
    // title 설정
    private fun setTitleText(resCode:Int, body: PostDetailResponse?){
        // title text 지정
        viewBinding.textViewTitle.text = when(resCode){
            200 -> {
                if(body == null){
                    "api body가 비어있습니다."
                }else{
                    if(body.result[0].title == ""){
                        "api호출은 성공했으나 데이터가 없습니다."
                    }else{
                        body.result[0].title
                    }
                }
            }
            400 -> {
                "API 키가 만료됬거나 쿼리 파라미터가 잘못 됬습니다."
            }
            401 -> {
                "인증 정보가 정확하지 않습니다."
            }
            500 -> {
                "API 서버에 문제가 발생하였습니다."
            }
            else -> {
                "기타 문제발생..."
            }
        }
    }
    // posttxt 설정
    private fun setPostText(resCode: Int,body: PostDetailResponse?){
        viewBinding.textViewPosttxt.text = when(resCode){
            200 -> {
                if(body == null){
                    "api body가 비어있습니다."
                }else{
                    if(body.result[0].body.toString()== "[]"){
                        "api호출은 성공했으나 데이터가 없습니다."
                    }else{
                        body.result[0].body.toString()
                    }
                }
            }
            400 -> {
                "API 키가 만료됬거나 쿼리 파라미터가 잘못 됬습니다."
            }
            401 -> {
                "인증 정보가 정확하지 않습니다."
            }
            500 -> {
                "API 서버에 문제가 발생하였습니다."
            }
            else -> {
                "기타 문제발생..."
            }
        }
    }
    // vibe id, vibe image 설정
    private fun setVibeIdImage(resCode: Int, body: PostDetailResponse?){

    }
    // like_number, scrap_number, comment_number 설정
    private fun setNumber(resCode: Int, body: PostDetailResponse?){
        val like_number = body!!.result[0].like_number
        val scrap_number = body!!.result[0].scrap_number
        val comment_number = body!!.result[0].comment_number
    }
    // tag name 설정
    private fun setTagName(resCode: Int, body: PostDetailResponse?){
        viewBinding.textViewTag1.text = when(resCode){
            200 -> {
                if(body == null){
                    "데이터가 없습니다."
                }else{
                    if (body.result[0].tag_name == ""){
                        "api 호출은 성공했으나 데이터가 없습니다."
                    }else{
                        body.result[0].tag_name
                    }
                }
            }
            3012 -> {
                "게시물이 존재하지 않습니다."
            }
            else -> {
                "기타 문제 발생"
            }
        }
    }
    // nickname 설정
    private fun setNicknameText(resCode: Int, body: PostDetailResponse?) {
        viewBinding.textViewUsername.text = when(resCode){
            200 -> {
                if(body == null){
                    "api body가 비어있습니다."
                }else{
                    if(body.result[0].nickname== ""){
                        "api호출은 성공했으나 데이터가 없습니다."
                    }else{
                        body.result[0].nickname
                    }
                }
            }
            400 -> {
                "API 키가 만료됬거나 쿼리 파라미터가 잘못 됬습니다."
            }
            401 -> {
                "인증 정보가 정확하지 않습니다."
            }
            500 -> {
                "API 서버에 문제가 발생하였습니다."
            }
            else -> {
                "기타 문제발생..."
            }
        }
    }
    // profileImg 설정 - 미완
    private fun setProfileImg(resCode: Int, body: PostDetailResponse?){
        viewBinding.imageViewProfile
    }

}


