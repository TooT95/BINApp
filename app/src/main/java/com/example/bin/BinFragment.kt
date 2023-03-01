package com.example.bin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bin.databinding.FragmentMainBinding

class BinFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: BinViewModel by viewModels()
    private lateinit var binAdapter: BinAdapter
    private val binList = mutableListOf<Bin>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllBinFromDatabase()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.binLiveData.observe(viewLifecycleOwner, ::bin)
        viewModel.binListLiveData.observe(viewLifecycleOwner) {
            binList.addAll(it)
            binAdapter.submitList(it)
        }
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
    }

    private fun bin(bin: Bin) {
        binList.add(0,bin)
        binAdapter.submitList(binList)
        openDetail(0)
    }

    private fun initUI() {
        with(binding) {
            binAdapter = BinAdapter(::openDetail)
            rvHistory.adapter = binAdapter
            rvHistory.layoutManager = LinearLayoutManager(requireContext())

            cardSearch.setOnClickListener {
                viewModel.getBinData(etxtBinCode.text?.toString() ?: "")
            }
        }
    }

    private fun openDetail(itemId: Int) {
        val modalBottomSheet = BinDetailBottomSheetFragment(binAdapter.currentList[itemId])
        modalBottomSheet.show(parentFragmentManager, BinDetailBottomSheetFragment.TAG)
    }

}