package com.example.picknumberproject.view.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Toast
import androidx.room.Room
import com.example.picknumberproject.R
import com.example.picknumberproject.data.api.SMSAuthenticationCodeApi
import com.example.picknumberproject.data.db.UserDatabase
import com.example.picknumberproject.data.dto.sms.Contents
import com.example.picknumberproject.data.dto.sms.SMSRequestBody
import com.example.picknumberproject.data.dto.sms.SMSResponseModel
import com.example.picknumberproject.data.url.Key
import com.example.picknumberproject.data.url.Url
import com.example.picknumberproject.databinding.ActivitySignUpBinding
import com.example.picknumberproject.domain.model.UserEntity
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.timer

@AndroidEntryPoint
class SignUpActivity : ViewBindingActivity<ActivitySignUpBinding>() {
    lateinit var retrofit: Retrofit
    lateinit var phoneNum: String
    lateinit var jsonString: String
    lateinit var requestBody: SMSRequestBody
    lateinit var codeNum: String
    lateinit var userDB: UserDatabase

    private val time = 120

    private var second = time % 60
    private var minute = time / 60

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignUpBinding
        get() = ActivitySignUpBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setSupportActionBar(signup_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //타이틀 안보이게
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //왼쪽 뒤로가기 사용여부
        supportActionBar?.setHomeAsUpIndicator(R.drawable.cancel_button) //왼쪽 뒤로가기 아이콘 변경

        // DB 생성
        userDB = Room.databaseBuilder(this, UserDatabase::class.java, "UserDB")
            .allowMainThreadQueries().build()

        // 핸드폰 번호 입력 시 하이픈 자동 입력
        userPhoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        val phoneNumEdit = userPhoneEditText

        phoneNum = ""
        jsonString = ""

        phoneNumEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { // 입력하기 전
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { // 입력 중
            }

            override fun afterTextChanged(p0: Editable?) { // 입력이 끝날 때

                // 랜덤으로 인증코드 4자리 생성
                codeNum = (1000..9999).random().toString()

                phoneNum = phoneNumEdit.text.toString()
                requestBody = SMSRequestBody(
                    Contents("15331490", "[순번관리시스템 본인인증] 인증번호 [$codeNum]\n를 입력해 주세요", "LANDPICK", phoneNum, "개인정보인증", "")
                )
            }

        })

        signupButton.setOnClickListener {

            if (userDB.getDao().getPhone(phoneNum).isNotEmpty()) { // 이미 존재하는 번호일 경우
                Toast.makeText(applicationContext, "이미 가입된 사용자입니다.", Toast.LENGTH_SHORT).show()
            } else {
                if (verificationCodeEditText.text.toString() == codeNum) { // 인증번호를 올바르게 입력하면 회원등록 (회원가입 완료)
                    if (userNameEditText.text.isNotEmpty() && userPhoneEditText.text.isNotEmpty()) { // 정보 모두 입력했을 시
                        val userName = userNameEditText.text.toString()
                        Log.d("username", userName)
                        userDB.getDao().insertUser(UserEntity(phoneNum, userName)) // 회원등록
                        Toast.makeText(applicationContext, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        navigateLoginActivity()

                    } else { // 하나라도 입력이 잘 안되었으면
                        Toast.makeText(applicationContext, "사용자 이름과 전화번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                } else { // SMS 인증 요청 다시
                    Toast.makeText(applicationContext, "인증번호가 틀렸습니다. 다시 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        retrofit = Retrofit.Builder()
            .baseUrl(Url.SMS_AUTHENTICATION_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(SMSAuthenticationCodeApi::class.java)

        // SMS 인증 요청
        verificationCodeButton.setOnClickListener {
            Toast.makeText(applicationContext, "인증번호가 요청되었습니다.", Toast.LENGTH_SHORT).show()
            // SMS 인증코드 발송
            service.getCode(Key.token, requestBody).enqueue(object: Callback<SMSResponseModel> {
                override fun onResponse(
                    call: Call<SMSResponseModel>,
                    response: Response<SMSResponseModel>
                ) {
                    if (response.isSuccessful) {
                        Log.d("request body", requestBody.toString())
                        Log.d("response body", response.body().toString())
                        Log.d("response code", response.code().toString())
                        setTimer() // 인증번호 입력 시간 타이머 시작
                    } else {
                        Log.d("response", "응답에 실패했습니다.")
                    }
                }

                override fun onFailure(call: Call<SMSResponseModel>, t: Throwable) {
                    Log.d("인증 버튼 클릭", "API 응답 실패")
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.menu_signup,
            menu
        )
        return true
    }

    private fun setTimer() {
        timer(period = 1000, initialDelay = 1000) {
            runOnUiThread {
                timeTextView.text = String.format("0$minute:%02d", second)
            }
            if (second == 0 && minute == 0) {
                // 타이머 종료
                Log.d("setTimer()", "타이머 종료")
                cancel()
                runOnUiThread {
                    timeTextView.text = String.format("02:00")
                    Toast.makeText(applicationContext, "인증 시간이 만료되었습니다.", Toast.LENGTH_SHORT).show()
                }
                return@timer
            }

            if (second == 0) {
                minute--
                second = 60
            }
            second--
        }
    }

    private fun navigateLoginActivity() {
        val intent = LoginActivity.getIntent(this)
        startActivity(intent)
    }
}