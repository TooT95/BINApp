package com.example.bin

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bin.databinding.FragmentBinDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class BinDetailBottomSheetFragment(private val bin: Bin) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentBinDetailBinding.inflate(inflater, container, false)
        with(binding) {
            txtBinNumber.text = bin.bin
            txtNumber.text = bin.numberLength.toString()
            txtBrand.text = bin.brand
            txtType.text = bin.type
            txtScheme.text = bin.scheme
            val countryName = "${bin.country.name}, ${bin.country.numeric}"
            txtCountryName.text = countryName
            val lattLong = "\"${bin.country.latitude}\" \"${bin.country.longitude}\""
            txtCountryLangLong.text = lattLong
            txtCountryLangLong.setOnClickListener {
                val uri: String =
                    java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", bin.country.latitude.toFloat(), bin.country.longitude.toFloat())
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }
            txtBankName.text = bin.bank.name
            txtUrl.text = bin.bank.url
            txtUrl.setOnClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bin.bank.url)))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }

            }

            txtPhone.text = bin.bank.phone
            txtPhone.setOnClickListener {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        101)

                } else {
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bin.bank.phone))
                    startActivity(intent)
                }
            }
        }
        binding.btnClose.setOnClickListener {
            this.dismiss()
        }
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}