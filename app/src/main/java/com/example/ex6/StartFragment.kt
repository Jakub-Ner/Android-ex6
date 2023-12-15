package com.example.ex6

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.ex6.databinding.FragmentStartBinding
import java.io.File
import com.example.ex6.BuildConfig
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =
            FragmentStartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    private var basePhotoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val internalDir: File = requireContext().filesDir

        val externalDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val dir2 = Environment.getExternalStorageDirectory()
        val dir3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val dir4 = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        binding.tv1path.text =
            "${basePhotoUri.scheme}:/${MediaStore.Images.Media.EXTERNAL_CONTENT_URI.path}"

        binding.tv2path.text = dir2.absolutePath
        binding.tv3path.text = dir3.absolutePath
        binding.tv4path.text = dir4?.absolutePath ?: "nothing"
        dir4?.let {
            val theuri = FileProvider.getUriForFile(
                requireContext(),
                "${BuildConfig.APPLICATION_ID}.provider",
                it
            )
            binding.tv5path.text = "${theuri.scheme}:/${theuri.path}"
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}