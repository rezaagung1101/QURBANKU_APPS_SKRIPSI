package com.androidexpert.qurbanku_apps_skripsi.utils


import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var mListener: DialogDateListener? = null
    private var fragmentListener: FragmentDateListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)
        return DatePickerDialog(requireContext(), this, year, month, date)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        mListener?.onDialogDateSet(tag, year, month, dayOfMonth)
        fragmentListener?.onFragmentDateSet(year, month, dayOfMonth)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as DialogDateListener?
    }

    override fun onDetach() {
        super.onDetach()
        if (mListener != null) {
            mListener = null
        }
    }
    fun setListener(listener: DialogDateListener) {
        mListener = listener
    }
    fun setFragmentListener(listener: FragmentDateListener) {
        fragmentListener = listener
    }

    fun resetFragmentListener() {
        fragmentListener = null
    }

    interface FragmentDateListener {
        fun onFragmentDateSet(year: Int, month: Int, dayOfMonth: Int)
    }
    interface DialogDateListener {
        fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int)
    }
}