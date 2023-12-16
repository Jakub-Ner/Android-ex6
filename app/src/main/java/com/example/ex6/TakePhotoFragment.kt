package com.example.ex6

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.ex6.databinding.FragmentTakePhotoBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TakePhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TakePhotoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentTakePhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentTakePhotoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    var lastFileUri: Uri? = null
    lateinit var lastFile: File

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photoPreviewLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result: Bitmap? ->
                if (result != null) {
                    Toast.makeText(requireContext(), "PREVIEW RECEIVED", Toast.LENGTH_LONG).show()
                    binding.imgView.setImageBitmap(result)
                } else {
                    Toast.makeText(requireContext(), "PREVIEW NOT RECEIVED", Toast.LENGTH_LONG)
                        .show()
                }
            }
        binding.btnPreview.setOnClickListener {
            try {
                photoPreviewLauncher.launch()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "PREVIEW NOT RECEIVED", Toast.LENGTH_LONG).show()
                lastFile.delete()
            }
        }

        val photoLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { result: Boolean ->
                if (result) { // consume result - see later remarks
                    Toast.makeText(requireContext(), "Photo TAKEN", Toast.LENGTH_LONG).show()
                } else { // make some action – warning
                    Toast.makeText(requireContext(), "Photo NOT taken!", Toast.LENGTH_LONG).show()
                }
            }
        binding.btnPhoto.setOnClickListener {
            lastFileUri = getNewFileUri()
            try {
                photoLauncher.launch(lastFileUri)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "CAMERA DOESN'T WORK!", Toast.LENGTH_LONG).show()
                lastFile.delete()
            }

        }
    }

    private fun getNewFileUri(): Uri {
        val tStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) // requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val tmpFile = File.createTempFile(
            "Photo_" + "${tStamp}",
            ".jpg",
            dir
        )
        lastFile = tmpFile //save File for future use
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TakePhotoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TakePhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}