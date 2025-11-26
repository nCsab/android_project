package com.example.android_project.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android_project.R
import com.example.android_project.databinding.FragmentRegisterBinding
import com.example.android_project.utils.SessionManager

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(email, password, name)
            }
        }

        binding.btnGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { authResponse ->
                Toast.makeText(requireContext(), "Welcome ${authResponse.user.name}", Toast.LENGTH_LONG).show()
                val session = SessionManager(requireContext().applicationContext)
                session.saveAuthToken(authResponse.tokens.accessToken)
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment) // Or home, but usually login first or auto-login
                // Lab 6 doesn't specify where to go after register, but usually it's login or home.
                // Let's assume auto-login and go to home, or just go to login.
                // The lab says "Sign up using ... and log in using ...".
                // I'll navigate to LoginFragment to be safe, or Home if the token is valid.
                // Since I saved the token, I can go to Home.
                // But let's stick to Login for now as per typical flow if not auto-login.
                // Actually, the response contains tokens, so we are logged in.
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }.onFailure { error ->
                Toast.makeText(requireContext(), "Registration failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
