package com.example.tugasproyek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_newtask.*

class newtask : AppCompatActivity() {
    lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtask)
        ref = FirebaseDatabase.getInstance().getReference("Task")

        addnewtask.setOnClickListener {
            savedata()
        }
    }

    private fun savedata() {
        val tugas = taskreq.text.toString()
        val detTugas = taskreq1.text.toString()


        val TaskData = TaskData(tugas, detTugas)
        val userId = ref.push().key.toString()

        ref.child(userId).setValue(TaskData).addOnCompleteListener {
            Toast.makeText(this, "Successs", Toast.LENGTH_SHORT).show()
            tugas.Text= tugas.toEditable()
            detTugas.setText("")
        }
    }
}