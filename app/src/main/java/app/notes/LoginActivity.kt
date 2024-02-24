package app.notes

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.notes.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            loginUser()
        }

        binding.createAccountTextViewBtn.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
    }

    fun loginUser() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if(!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)){
            binding.emailEditText.setError("Invalid email")
            return
        }

        if(password.length<6){
            binding.passwordEditText.setError("Length should be 6 char")
            return
        }

        loginInFirebase(email, password)
    }

    fun loginInFirebase(email: String?, password: String?) {
        val firebaseAuth = FirebaseAuth.getInstance()
        changeInProgress(true)
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            changeInProgress(false)
            if (task.isSuccessful) {
                if (firebaseAuth.currentUser!!.isEmailVerified) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Utility.showToast(this, "Email not verified, Please verify your email.")
                }
            } else {
                Utility.showToast(this, task.exception!!.localizedMessage)
            }
        }
    }

    fun changeInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.setVisibility(View.VISIBLE)
            binding.loginBtn.setVisibility(View.GONE)
        } else {
            binding.progressBar.setVisibility(View.GONE)
            binding.loginBtn.setVisibility(View.VISIBLE)
        }
    }
}