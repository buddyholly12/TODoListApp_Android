package com.example.to_do_listapp

import java.util.*

data class Taskdata (val id :String? , val namatugas:String,val detailtugas:String)
{
    constructor():this("","","")
}