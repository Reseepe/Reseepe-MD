package com.capstone.reseepe.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.reseepe.R
import com.capstone.reseepe.databinding.FragmentScanBinding
import com.capstone.reseepe.ui.result.ResultViewModel
import com.capstone.reseepe.ui.scan.CameraActivity.Companion.CAMERAX_RESULT
import com.capstone.reseepe.util.ViewModelFactory
import com.capstone.reseepe.util.reduceFileImage
import com.capstone.reseepe.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null

    private val resultViewModel: ResultViewModel by viewModels(
        ownerProducer = { requireActivity() }
    ) {
        ViewModelFactory.getInstance(requireContext())
    }

    private var loadingDialog: AlertDialog? = null


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this.context, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this.context, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        this.context?.let {
            ContextCompat.checkSelfPermission(
                it,
                REQUIRED_PERMISSION
            )
        } == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCameraX() }
        binding.buttonUpload.setOnClickListener { uploadImage() }

        resultViewModel.isScanning.observe(viewLifecycleOwner){
            showLoading(it)
        }

        return root
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCameraX() {
        val intent = Intent(this.context, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        binding.previewImageView.setImageURI(currentImageUri)
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            resultViewModel.getIngredients(multipartBody)
            resultViewModel.scanIngredientResponse.observe(viewLifecycleOwner) { response ->
                response?.let {
                    Log.d("ScanFragment", "Scan response received: $response")
                    findNavController().navigate(R.id.action_scanFragment_to_resultFragment)
                }
            }
        } ?: showCustomDialog(
            title = "Oops you forget to attach an image",
            message = "Please attach an image or take a picture",
            buttonText = "Try Again"
        ) {}
    }

    private fun showCustomDialog(
        title: String,
        message: String,
        buttonText: String,
        onClick: () -> Unit
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonText) { dialog, _ ->
                dialog.dismiss()
                onClick()
            }
            .show()
    }

    private fun showLoading(isLoading: Boolean, message: String = "Scanning your photo for fresh ingredients... Preparing your personalized recipe.") {
        if (isLoading) {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_loading, null)
            val loadingMessageTextView = dialogView.findViewById<TextView>(R.id.tv_loading_message)
            loadingMessageTextView.text = message

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create()

            dialog.show()
            loadingDialog = dialog
        } else {
            loadingDialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
