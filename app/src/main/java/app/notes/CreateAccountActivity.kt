package app.notes

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.notes.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class CreateAccountActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener {
            createAccount()
        }

        binding.loginTextViewBtn.setOnClickListener {
            finish()
        }
    }

    fun createAccount() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.getText().toString()

        if(!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)){
            binding.emailEditText.setError("Invalid email")
            return
        }

        if(password.length<6){
            binding.passwordEditText.setError("Length should be 6 char")
            return
        }

        if(!password.equals(confirmPassword)){
            binding.confirmPasswordEditText.setError("Password not matched")
            return
        }

        createAccountInFirebase(email, password)
    }

    fun createAccountInFirebase(email: String, password: String) {
        changeInProgress(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            changeInProgress(false)
            if (it.isSuccessful) {
                Utility.showToast(this, "Successfully create account, Check email to verify")
                firebaseAuth.currentUser?.sendEmailVerification()
                firebaseAuth.signOut()
                finish()
            } else {
                Utility.showToast(this, it.exception?.localizedMessage)
            }
        }
    }

    fun changeInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnCreateAccount.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnCreateAccount.visibility = View.VISIBLE
        }
    }
}