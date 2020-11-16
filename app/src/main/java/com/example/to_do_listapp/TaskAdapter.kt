package com.example.to_do_listapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class TaskAdapter(val mctx : Context,val layoutRes: Int , val taskList :List<Taskdata>):ArrayAdapter<Taskdata>(mctx,layoutRes,taskList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)

        val view: View = layoutInflater.inflate(layoutRes, null)
        val tv_tugas:TextView = view.findViewById(R.id.tv_tugas)
        val tv_detail:TextView =view.findViewById(R.id.tv_detail)

        val datatask2=taskList[position]

        tv_tugas.text = datatask2.namatugas
        tv_detail.text = datatask2.detailtugas

        return  view
    }



}