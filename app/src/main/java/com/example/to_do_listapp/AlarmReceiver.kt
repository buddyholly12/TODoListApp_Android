package com.example.to_do_listapp

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TimePicker
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import java.util.*

class AlarmReceiver: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar: Calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        /** Create a new instance of TimePickerDialog and return it
         * && implement OnTimeSetListener on Activity.**/
        return TimePickerDialog(
                activity,
                activity as TimePickerDialog.OnTimeSetListener,
                hour,
                minute,
                true)   // Use the current time as the default values for the picker

    }


}