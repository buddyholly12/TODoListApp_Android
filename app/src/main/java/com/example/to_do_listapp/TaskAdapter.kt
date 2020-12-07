package com.example.to_do_listapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.util.Half.toFloat
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new__task.*
import java.text.SimpleDateFormat
import java.util.*


class TaskAdapter(val mctx : Context,val layoutRes: Int , val taskList :List<Taskdata>):ArrayAdapter<Taskdata>(mctx,layoutRes,taskList){

    var button_date1: ImageButton? = null
    var textview_date: TextView? = null

    var alarms1: ImageButton? = null
    var hour: Int = 0
    var minute: Int = 0
    var tvalarms : TextView? =null

    val cal = Calendar.getInstance()
    private lateinit var tvCalendars :TextView

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)

        val view: View = layoutInflater.inflate(layoutRes, null)
        val tv_tugas:TextView = view.findViewById(R.id.tv_tugas)
        val tv_detail:TextView =view.findViewById(R.id.tv_detail)
        val tombol_selesai:Button = view.findViewById(R.id.buttonselesai)
        val tv_date:TextView = view.findViewById(R.id.Tv_date)
        val tv_times:TextView = view.findViewById(R.id.tv_time)
        val tombol_edit:Button = view.findViewById(R.id.buttonEdit)
        val datatask2=taskList[position]

       /* tombol_selesai.setOnClickListener{

        }*/
        val ettugas1 = view.findViewById<EditText>(R.id.edit_Text1)
        val ettugas2 = view.findViewById<EditText>(R.id.detailTugas1)
        val ib_alarm = view.findViewById<ImageButton>(R.id.Ib_alarm1)
        val ib_caledars = view.findViewById<ImageButton>(R.id.Calendars1)
        val Tvcalendars = view.findViewById<TextView>(R.id.Tvcalendars1)

        tombol_edit.setOnClickListener{
            update_info(datatask2)
        }
        tombol_selesai.setOnClickListener({
            DeleteTask(datatask2)
        })

        tv_tugas.text = datatask2.namatugas
        tv_detail.text = datatask2.detailtugas
        tv_date.text= datatask2.tanggal1
        tv_times.text =datatask2.jam
        return  view
    }
    private fun update_info(datatask2: Taskdata)
    {
        val builder = AlertDialog.Builder(mctx)
        builder.setTitle("Update Task")
        val title = TextView(mctx)
        title.setText("Update Data")
        title.setBackgroundColor(Color.BLUE)
        title.setPadding(10, 10, 10, 10)
        title.setGravity(Gravity.CENTER)
        title.setTextColor(Color.WHITE)
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0F)
        builder.setCustomTitle(title)
        val inflater = LayoutInflater.from(mctx)
        val view = inflater.inflate(R.layout.update_dialog,null)

        //Variable dari xml
        val ettugas1 = view.findViewById<EditText>(R.id.edit_Text1)
        val ettugas2 = view.findViewById<EditText>(R.id.detailTugas1)
        val ib_alarm = view.findViewById<ImageButton>(R.id.Ib_alarm1)
        val ib_caledars = view.findViewById<ImageButton>(R.id.Calendars1)

        textview_date = view.findViewById<TextView>(R.id.Tvcalendars1)
        tvalarms = view.findViewById<TextView>(R.id.Tv_Alarms1)
        //Set Text
        ettugas1.setText(datatask2.namatugas)
        ettugas2.setText(datatask2.detailtugas)
//        Tvalarms.setText(datatask2.jam)
  //      Tvcalendars.setText(datatask2.tanggal1)

        ib_caledars.setOnClickListener({
            configtime()
        })
        ib_alarm.setOnClickListener({
            configwaktu()
        })

        builder.setView(view)
        builder.setPositiveButton("Update",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                val ref= FirebaseDatabase.getInstance().getReference("Taskdata")
                val tugas = ettugas1.text.toString().trim()
                val detTugas = ettugas2.text.toString().trim()
                val tanggal = textview_date?.text.toString().trim()
                val jam = tvalarms?.text.toString().trim()
                val datatask = Taskdata(datatask2.id,tugas,detTugas,tanggal,jam)
                ref.child(datatask2.id.toString()).setValue(datatask)
                Toast.makeText(mctx,"Saved",Toast.LENGTH_LONG).show()

            }

        })
        builder.setNegativeButton("Cancel",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }

        })
        val alert =builder.create()
        alert.show()

    }
    private fun DeleteTask(datatask2: Taskdata){
        val progressDialog = ProgressDialog(context,
            R.style.Theme_MaterialComponents_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Deleting...")
        progressDialog.show()
        val mydatabase = FirebaseDatabase.getInstance().getReference("Taskdata")
        mydatabase.child(datatask2.id.toString()).removeValue()
        Toast.makeText(mctx,"Deleted!!",Toast.LENGTH_SHORT).show()
        val intent = Intent(context, home::class.java)
        context.startActivity(intent)
    }
    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }
    private fun datelistener(): DatePickerDialog.OnDateSetListener? {
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        return dateSetListener
    }
    private fun configtime(){
        DatePickerDialog(mctx,
            datelistener(),
            // set DatePickerDialog to point to today's date when it loads up
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()


    }
    private fun configwaktu(){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            tvalarms!!.text = SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(mctx, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

}


