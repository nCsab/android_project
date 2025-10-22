package com.example.labor_1.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.labor_1.R
import com.example.labor_1.databinding.FragmentLoginBinding
import com.example.labor_1.utils.SessionManager

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Email és jelszó megadása kötelező!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }

        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { authResponse ->
                Toast.makeText(
                    requireContext(),
                    "Sikeres bejelentkezés: ${authResponse.user.name}",
                    Toast.LENGTH_LONG
                ).show()

                val session = SessionManager(requireContext().applicationContext)
                session.saveAuthToken(authResponse.tokens.accessToken)

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
            result.onFailure { error ->
                Toast.makeText(
                    requireContext(),
                    "Bejelentkezés sikertelen: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("LoginFragment", "Login error:", error)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


