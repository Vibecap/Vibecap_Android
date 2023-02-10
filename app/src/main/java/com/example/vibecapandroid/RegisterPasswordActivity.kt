package com.example.vibecapandroid

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.vibecapandroid.databinding.ActivityRegisterPasswordBinding

class RegisterPasswordActivity:AppCompatActivity() {
    private val viewBinding: ActivityRegisterPasswordBinding by lazy {
        ActivityRegisterPasswordBinding.inflate(layoutInflater)
    }

    fun isPasswordFormat(password: String): Boolean {
        return password.matches("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,24}\$".toRegex())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val email = intent.extras!!.getString("Email") /*String형*/
        var check =false
        var patterncheck=false


        viewBinding.activityRegisterPasswordSet.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!=null) {
                    when {
                        s.isEmpty()->{
                            viewBinding.activityRegisterPassword812.text = "비밀번호를 입력해주세요"
                        }
                        s.isNotEmpty() -> {
                            when {
                                viewBinding.activityRegisterPasswordSetcheck.text.toString() != ""
                                        && viewBinding.activityRegisterPasswordSetcheck.text.toString() != viewBinding.activityRegisterPasswordSet.text.toString() -> {
                                    viewBinding.activityRegisterPasswordAgaintype.setTextColor(Color.RED)
                                    viewBinding.activityRegisterPasswordAgaintype.text =
                                        "비밀번호가 일치하지 않습니다"
                                    check = false
                                }
                                else -> {
                                    viewBinding.activityRegisterPasswordAgaintype.setTextColor(Color.BLACK)
                                    viewBinding.activityRegisterPasswordAgaintype.text =
                                        "비밀번호가 일치합니다."
                                    check = true
                                    if(patterncheck) {
                                        viewBinding.activityRegisterPasswordNext.setOnClickListener() {
                                            val intent = Intent(
                                                this@RegisterPasswordActivity,
                                                RegisterNicknameActivity::class.java
                                            )
                                            intent.putExtra(
                                                "Password",
                                                viewBinding.activityRegisterPasswordSetcheck.text.toString()
                                            )
                                            intent.putExtra("Email", email)
                                            startActivity(intent)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (check&&patterncheck) {
                        viewBinding.activityRegisterPasswordNext.setTextColor(Color.BLACK)
                        viewBinding.activityRegisterPasswordNext.isEnabled = true

                    } else {
                        viewBinding.activityRegisterPasswordNext.setTextColor(Color.GRAY)
                        viewBinding.activityRegisterPasswordNext.isEnabled = false
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
                if(s!=null) {
                    when {
                        s.isEmpty() -> {
                            viewBinding.activityRegisterPassword812.text = "비밀번호를 입력해주세요"
                        }
                        s.isNotEmpty() -> {
                            if(isPasswordFormat(viewBinding.activityRegisterPasswordSet.text.toString())) {
                                viewBinding.activityRegisterPassword812.text = "사용가능한 비밀번호입니다"
                                viewBinding.activityRegisterPassword812.setTextColor(Color.BLACK)
                                patterncheck=true
                            }
                            else{
                                viewBinding.activityRegisterPassword812.text = "숫자, 문자, 특수문자 중 2가지 포함(8~24자)"
                                viewBinding.activityRegisterPassword812.setTextColor(Color.RED)
                                patterncheck=false
                            }
                        }
                    }
                }
            }
        })
        viewBinding.activityRegisterPasswordSetcheck.addTextChangedListener(object :TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(s!=null) {
                    when {
                        s.isEmpty()->{
                            viewBinding.activityRegisterPasswordAgaintype.setTextColor(Color.BLACK)
                            viewBinding.activityRegisterPasswordAgaintype.text="비밀번호를 한번 더 입력해주세요"
                        }
                        s.isNotEmpty() -> {
                            when {
                                viewBinding.activityRegisterPasswordSetcheck.text.toString() != ""
                                        && viewBinding.activityRegisterPasswordSetcheck.text.toString() != viewBinding.activityRegisterPasswordSet.text.toString() -> {
                                    viewBinding.activityRegisterPasswordAgaintype.setTextColor(Color.RED)
                                    viewBinding.activityRegisterPasswordAgaintype.text =
                                        "비밀번호가 일치하지 않습니다"
                                    check = false
                                }
                                else -> {
                                    viewBinding.activityRegisterPasswordAgaintype.setTextColor(Color.BLACK)
                                    viewBinding.activityRegisterPasswordAgaintype.text =
                                        "비밀번호가 일치합니다."
                                    check = true
                                    if(patterncheck) {
                                        viewBinding.activityRegisterPasswordNext.setOnClickListener() {
                                            val intent = Intent(
                                                this@RegisterPasswordActivity,
                                                RegisterNicknameActivity::class.java
                                            )
                                            intent.putExtra(
                                                "Password",
                                                viewBinding.activityRegisterPasswordSetcheck.text.toString()
                                            )
                                            intent.putExtra("Email", email)
                                            startActivity(intent)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (check&&patterncheck) {
                        viewBinding.activityRegisterPasswordNext.setTextColor(Color.BLACK)
                        viewBinding.activityRegisterPasswordNext.isEnabled = true

                    } else {
                        viewBinding.activityRegisterPasswordNext.setTextColor(Color.GRAY)
                        viewBinding.activityRegisterPasswordNext.isEnabled = false
                    }
                }
            }


        })
    }
}