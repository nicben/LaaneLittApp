package com.laanelitt.laanelittapp.profile.settings

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditPasswordBinding
import com.laanelitt.laanelittapp.objects.User
import com.laanelitt.laanelittapp.objects.UserLocalStore
import kotlinx.android.synthetic.main.fragment_edit_password.*

class EditPasswordFragment : Fragment() {
    var loggedInUser: User? = null
    var userLocalStore: UserLocalStore? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentEditPasswordBinding
    lateinit var passwordCurrent: Editable
    lateinit var passwordNew: Editable
    lateinit var passwordConfirm: Editable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_password, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
    //        val credential: AuthCredential =  getCredential(loggedInUser?.email!!, loggedInUser?.password!!);

        userLocalStore = UserLocalStore(requireContext())

        loggedInUser = userLocalStore?.getLoggedInUser

        binding.editPasswordBtn.setOnClickListener {

            passwordCurrent = edit_password_current.getEditText()?.getText()!!
            passwordNew = edit_password_new.getEditText()?.getText()!!
            passwordConfirm = edit_password_confirm.getEditText()?.getText()!!
            println("edit password")
            updatePassword()

        }
    }

    private fun updatePassword() {
        if (passwordCurrent.toString().isNotEmpty() &&
            passwordNew.toString().isNotEmpty() &&
            passwordConfirm.toString().isNotEmpty())
        {
            edit_password_current.error = null
            if (passwordNew.toString().equals(passwordConfirm.toString())) {
                val newPassword = passwordNew.toString()

                val currentUser = auth.currentUser
                if (currentUser != null && currentUser.email != null) {
                    // Get auth credentials from the user for re-authentication. The example below shows
                    // email and password credentials but there are multiple possible providers,
                    // such as GoogleAuthProvider or FacebookAuthProvider.
                    val credential = EmailAuthProvider
                        .getCredential(currentUser.email!!, passwordCurrent.toString())

                    // Prompt the user to re-provide their sign-in credentials
                    currentUser.reauthenticate(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d(TAG, "User re-authenticated.")

                            currentUser.updatePassword(newPassword)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "User password updated.")
                                        userLocalStore!!.clearUserData()
                                        auth.signOut()
                                        findNavController().navigate(R.id.loginFragment)
                                    } else {
                                        Log.d(TAG, "User password is not updated.")
                                    }
                                }

                        } else {
                            Log.d(TAG, "User not re-authenticated.")
                            edit_password_current.error  = "Feil passord"
                        }

                    }
                }

            } else {
                Toast.makeText(requireContext(), "Ulike passord", Toast.LENGTH_SHORT).show()
            }
        }else {
            Toast.makeText(requireContext(), "Alle feltene må fylles", Toast.LENGTH_SHORT).show()
        }
    }

}