package com.example.ex6

import android.content.ActivityNotFoundException
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ex6.DataRepo.Companion.PRIVATE_S
import com.example.ex6.DataRepo.Companion.SHARED_S
import com.example.ex6.databinding.FragmentPhotoListBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoListFragment : Fragment() {
    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataRepo: DataRepo
    private lateinit var adapter: PhotoListAdapter
    private lateinit var recView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentPhotoListBinding.inflate(inflater, container, false)
        dataRepo = DataRepo.getinstance(requireContext())
        dataRepo.setStorage(PRIVATE_S) // replace with PRIVATE_S to access app-only imgs

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recView = _binding!!.recView

        val listener = object : OnItemClickListener {
            override fun onImageClick(dataItem: DataItem) {
                val bundle = Bundle()
//                    bundle.putInt("startingIndex", position)
                findNavController().navigate(R.id.swipePhotoFragment, bundle)
            }
        }


        val adapter = dataRepo.getAppList() // replace with getAppLiist to access app-only imgs
            ?.let { PhotoListAdapter(requireContext(), it, listener) }
        if (adapter == null) {
            Toast.makeText(
                requireContext(), "Invalid Data",
                Toast.LENGTH_LONG
            ).show()
            requireActivity().onBackPressed()
        }
//        recView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recView.layoutManager = GridLayoutManager(requireContext(), 2)
        this.adapter = adapter!!
        recView.adapter = adapter

        val photoLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { result: Boolean ->
                if (result) { // consume result - see later remarks
                    Toast.makeText(requireContext(), "Photo TAKEN", Toast.LENGTH_LONG).show()
                } else { // make some action – warning
                    Toast.makeText(requireContext(), "Photo NOT taken!", Toast.LENGTH_LONG).show()
                }
            }

        binding.btnAdd.setOnClickListener {
            try {
                val tmpUri = getNewFileUri()
                val value = File(tmpUri.path!!)
                val fileName = value.name
                photoLauncher.launch(tmpUri)
                val newImage = DataItem(
                    fileName,
                    value.toURI().path,
                    value.absolutePath,
                    tmpUri
                )
                adapter.dList.add(newImage)
                adapter.notifyDataSetChanged()
//                dataRepo.getAppList()?.add(newImage) // add should be implemented

            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "CAMERA DOESN'T WORK!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getNewFileUri(): Uri {
        val dir: File
        when (DataRepo.getStorage()) {
            SHARED_S -> dir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

            PRIVATE_S -> dir =
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

            else -> return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val tStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val tmpFile = File.createTempFile(
            "Photo_" + "${tStamp}",
            ".jpg",
            dir
        )
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
         * @return A new instance of fragment PhotoListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhotoListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}