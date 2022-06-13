package com.example.androidapp.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.androidapp.R
import com.example.androidapp.retrofit.ItemListRapiBoy
import com.example.androidapp.retrofit.RapiBoyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListFragment : Fragment() {

    private lateinit var table: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_list, container, false)
        table = view.findViewById(R.id.table)
        getItems()
        return view
    }

    private fun getItems() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rapiboy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            val call = retrofit.create(RapiBoyService::class.java).getListItem()
            val items = call.body()
            activity?.runOnUiThread {
                if(call.isSuccessful) {
                    if (items != null) {
                        showTable(items)
                    }
                }
                else {
                    showAlert()
                }
            }
        }
    }

    private fun showTable(list: List<ItemListRapiBoy>) {
        println(list.size)
        list.forEach {
            val fila = TableRow(activity)
            val lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            fila.layoutParams = lp
            val tv_id = TextView(activity)
            val tv_value = TextView(activity)
            setUpTextViews(tv_id, tv_value)
            tv_id.text = it.Id.toString()
            tv_value.text = it.Valor
            println(it.Valor)
            fila.addView(tv_id)
            fila.addView(tv_value)
            table.addView(fila);
        }
    }

    private fun showAlert() {
        val builer = AlertDialog.Builder(requireContext())
        builer.setTitle("Error")
        builer.setMessage("Se ha producido un error de autenticando al usuario")
        builer.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builer.create()
        dialog.show()
    }

    private fun setUpTextViews(tvId: TextView, tvValue: TextView) {
        tvId.setTextColor(requireActivity().resources.getColor(R.color.black))
        tvId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        tvId.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tvId.setPadding(0,4,0,4)
        tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        tvValue.setTextColor(requireActivity().resources.getColor(R.color.black))
        tvValue.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tvValue.setPadding(0,4,0,4)
        val typeFace: Typeface? = ResourcesCompat.getFont(requireActivity().applicationContext, R.font.roboto)
        tvId.setTypeface(typeFace)
        tvValue.setTypeface(typeFace)
    }
}