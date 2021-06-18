package com.mino.sampleprojectcollection

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mino.sampleprojectcollection.DBKey.Companion.USERS
import com.mino.sampleprojectcollection.DBKey.Companion.USER_ID
import com.mino.sampleprojectcollection.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        auth = Firebase.auth


        initEmailAndPasswordEditTextView()
        initLoginButton()
        initSignUpButton()
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            auth.signInWithEmailAndPassword(getInputEmail(), getInputPassword())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        handleSuccessLogin()
                    } else {
                        toast("로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.")
                    }
                }
        }
    }

    private fun initSignUpButton() {
        binding.signUpButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(getInputEmail(), getInputPassword())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        toast("회원가입에 성공했습니다. 로그인 버튼을 눌러 로그인해주세요.")
                    } else {
                        toast("이미 가입한 이메일이거나, 회원가입에 실패했습니다.")
                    }
                }
        }
    }

    private fun initEmailAndPasswordEditTextView() {
        binding.emailEditText.addTextChangedListener { isEnable() }
        binding.passwordEditText.addTextChangedListener { isEnable() }
    }

    private fun isEnable() {
        val enable =
            binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
        binding.loginButton.isEnabled = enable
        binding.signUpButton.isEnabled = enable
    }

    private fun getInputEmail() = binding.emailEditText.text.toString()

    private fun getInputPassword() = binding.passwordEditText.text.toString()

    private fun Activity.toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccessLogin() {
        if (auth.currentUser == null) {
            toast("로그인에 실패했습니다.")
            return
        }

        val userId = auth.currentUser?.uid.orEmpty()
        val currentUserDB = Firebase.database.reference.child(USERS).child(userId)
        val user = mutableMapOf<String, Any>()
        user[USER_ID] = userId
        currentUserDB.updateChildren(user)

        finish()
    }
}
