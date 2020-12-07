package com.example.to_do_listapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new__task.*
import kotlinx.android.synthetic.main.tasklist.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Month
import java.time.Year
import java.util.*
import androidx.appcompat.app.AppCompatActivity.ALARM_SERVICE as ALARM_SERVICE1

const val  Extra_message = "Dt_kirim"

class New_Task : AppCompatActivity(),View.OnClickListener{
    private lateinit var tugas1 : com.google.android.material.textfield.TextInputEditText
    private  lateinit var detailtugas1: EditText
    private  lateinit var addTask:com.google.android.material.floatingactionbutton.FloatingActionButton

    var button_date: ImageButton? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()
    var alarms:ImageButton? = null
    var hour: Int = 0
    var minute: Int = 0
    var tvalarms : TextView? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new__task)
        var database = FirebaseDatabase.getInstance().reference

        tugas1= findViewById(R.id.taskreq)
        detailtugas1 = findViewById(R.id.taskreq1)
        addTask = findViewById(R.id.addnewtask)
//        tvdate = findViewById(R.id.Tv_date)

        button_date = this.Calendars
        textview_date = this.Tvcalendars
        alarms = this.Ib_alarm
        tvalarms =this.Tv_Alarms

        addTask.setOnClickListener(this)

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date!!.setOnClickListener{
            configtime()
        }
        alarms!!.setOnClickListener{
            configwaktu()
        }


    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }

    private fun savedata() {

        val tugas = tugas1.text.toString().trim()
        val detTugas = detailtugas1.text.toString().trim()
        val tanggal = textview_date?.text.toString().trim()
        val jam = tvalarms?.text.toString().trim()
        if (tugas.isEmpty())
        {
            tugas1.error="harap isi tugas "
            return
        }
        if (detTugas.isEmpty())
        {
            detailtugas1.error="harap isi tugas "
            return
        }
        val ref= FirebaseDatabase.getInstance().getReference("Taskdata")
        val taskid = ref.push().key
        val datatask = Taskdata(taskid,tugas,detTugas,tanggal,jam)
        if (taskid!=null)
        {
            ref.child(taskid).setValue(datatask).addOnCompleteListener{
                Toast.makeText(applicationContext, "Data ditambahkan ", Toast.LENGTH_SHORT).show()
            }
        }

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
        DatePickerDialog(this@New_Task,
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
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }


    override fun onClick(v: View?) {
        savedata()
      //  configtime()
    }



}
