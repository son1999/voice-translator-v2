package com.fatherofapps.androidbase.ui.permission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.activities.MainActivity
import com.fatherofapps.androidbase.common.AppSharePreference
import com.fatherofapps.androidbase.databinding.FragmentPermissionBinding
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionFragment : Fragment() {
    private lateinit var dataBinding: FragmentPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentPermissionBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.btnGo.setOnClickListener {
            if (dataBinding.switch1.isChecked && dataBinding.switch2.isChecked) {
                val p = (requireActivity() as? MainActivity)?.preferences
                p?.edit()?.putBoolean(AppSharePreference.FIRST_LAUNCH, false)?.apply()
                (requireActivity() as? MainActivity)?.nav()?.popBackStack(R.id.introFragment, true)
                (requireActivity() as? MainActivity)?.nav()?.navigate(R.id.homeFragment)
            }
        }
        dataBinding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            PermissionX.init(requireActivity())
                    .permissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .request { allGranted, grantedList, deniedList ->
                        dataBinding.switch1.isChecked = allGranted
                    }
        }
        dataBinding.switch2.setOnCheckedChangeListener { buttonView, isChecked ->
            PermissionX.init(requireActivity())
                    .permissions(android.Manifest.permission.CAMERA)
                    .request { allGranted, grantedList, deniedList ->
                        dataBinding.switch2.isChecked = allGranted
                    }
        }
        return dataBinding.root
    }

    private fun requestCameraPermission() {

    }
}