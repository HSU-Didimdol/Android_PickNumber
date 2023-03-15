package com.example.picknumberproject.view.login

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.room.Room
import com.example.picknumberproject.R
import com.example.picknumberproject.data.db.UserDatabase
import com.example.picknumberproject.databinding.ActivityLoginBinding
import com.example.picknumberproject.domain.model.UserEntity
import com.example.picknumberproject.view.MainActivity
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.view.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : ViewBindingActivity<ActivityLoginBinding>() {
    lateinit var userDB: UserDatabase

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 핸드폰 번호 입력 시 하이픈 자동 입력
        phoneNum_edit.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        val userName = userName_edit.text
        val phoneNumber = phoneNum_edit.text
        val checkPhoneNum = phoneNumber.toString().replace("-", "")

        // DB 생성
        userDB = Room.databaseBuilder(this, UserDatabase::class.java, "UserDB")
            .allowMainThreadQueries().build()

        /**
         * 일단 2명의 정보 저장해두기
         */
        userDB.getUser().insertUser(UserEntity("010-1111-0000", "박길동"))
        userDB.getUser().insertUser(UserEntity("010-2222-1111", "이길동"))

        /**
         * 1. 사용자 이름과 전화번호를 DB 에서 비교 (사용자 조회)
         * 2. 조회된 사용자 정보와 입력된 사용자 정보 비교
         * 3. 로그인 성공/실패 처리 (실패 -> 회원가입)
         */

        // 로그인 버튼 클릭
        signIn_btn.setOnClickListener {

            if (userName.isNotEmpty() && phoneNumber.isNotEmpty()) { // 사용자 정보를 모두 입력했는지 확인
                /*if (checkPhoneNum.length != 13) { // 전화번호 형식 체크
                    Toast.makeText(this, "전화번호를 정확하게 입력해주세요.", Toast.LENGTH_SHORT).show()
                }*/

                if (checkValidUser()) { // 사용자가 이미 존재하고
                    if (checkValidName(phoneNumber.toString()) == userName.toString()) { // DB 에 있는 사용자 이름과 입력된 사용자 이름이 같으면
                        Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("userName:", userName.toString())
                        Toast.makeText(this, "입력된 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else { // 사용자 정보가 없으면 회원가입
                    Toast.makeText(this, "회원가입이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            } else { // 사용자 정보(이름, 전화번호) 입력이 제대로 안됐을 경우
                Toast.makeText(this, "이름과 전화번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }


            checkValidName(phoneNumber.toString())

            Log.d("userName:", userName.toString())
            Log.d("phoneNum:", phoneNumber.toString())

            navigateMainActivity()
        }

        // 회원가입 버튼 클릭
        signUp_btn.setOnClickListener {
            navigateSignUpActivity()
        }


    }

    // 입력된 전화번호가 가입되어 있는 전화번호인지 확인
    private fun checkValidUser(): Boolean {
        return userDB.getUser().getPhone().isNotEmpty()
    }

    // 입력된 전화번호로 DB 에서 사용자 이름 가져오기
    private fun checkValidName(phoneNumber: String): String {
        return userDB.getUser().getUserByPhone(phoneNumber)
    }

    private fun navigateSignUpActivity() {
        val intent = SignUpActivity.getIntent(this)
        startActivity(intent)
    }

    private fun navigateMainActivity() {
        val intent = MainActivity.getIntent(this)
        startActivity(intent)
    }
}