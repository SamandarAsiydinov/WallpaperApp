package uz.context.wallpaperapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import uz.context.wallpaperapp.R
import uz.context.wallpaperapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        navController = Navigation.findNavController(view)
        if (firebaseAuth.currentUser == null) {
            binding.registerText.text = getString(R.string.creat)
            firebaseAuth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.registerText.text = getString(R.string.account)
                    navController?.navigate(R.id.action_registerFragment_to_homeFragment)
                } else {
                    binding.registerText.text = "${getString(R.string.error)} ${it.exception!!.message}"
                }
            }
        } else {
            navController?.navigate(R.id.action_registerFragment_to_homeFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}