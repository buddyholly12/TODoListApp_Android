package com.example.to_do_listapp

import android.annotation.SuppressLint
import android.app.*
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
    private lateinit var notification_builder : Notification.Builder
    private lateinit var sensor_intent : Intent
    private val color = Color.RED
    private val light_color = Color.argb(255, 255, 255, 0)
    var button_date: ImageButton? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()
    var alarms:ImageButton? = null
    var hour: Int = 0
    var minute: Int = 0
    var tvalarms : TextView? =null
    lateinit var pendingIntent: PendingIntent
    private lateinit var alarmManager: AlarmManager
    private val REQUEST_CODE = 100
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
            Alarm_Manager()
        }
// notif manager
//        sensor_intent = Intent(this, SensorActivity::class.java)
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            notification_builder =Notification.Builder(this)
//                    .setSmallIcon(R.drawable.ic_priority_high_black_24dp)
//                    .setColor(color)
//                    .setContentTitle("Welcome to Notification")
//                    .setContentText("Let's go to Sensor Act")
//                    .setLights(light_color, 2000, 3000)
//            notificationManager()
//        }else {
//
//            notification_builder = Notification.Builder(this)
//                    .setSmallIcon(R.drawable.ic_priority_high_black_24dp)
//                    .setContentTitle("Welcome to Notification")
//                    .setContentText("Let's go to Sensor Act")
//                    .setLights(light_color, 2000, 3000)
//            notificationManager()
//        }

    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
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

//    fun notificationManager(){
//        val pendingIntent = PendingIntent.getActivity(this@MainActivity, 0, sensor_intent, 0)
//        notification_builder.setContentIntent(pendingIntent)
//        val notification = notification_builder.build()
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(99, notification)
//
//    }

    private fun Alarm_Manager(){
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationManager::class.java)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 30)
        pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setRepeating(
                AlarmManager.RTC,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )
    }
}
