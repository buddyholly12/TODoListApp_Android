package com.example.to_do_listapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class New_Task : AppCompatActivity(),View.OnClickListener  {
    private lateinit var tugas1 : com.google.android.material.textfield.TextInputEditText
    private  lateinit var detailtugas1: EditText
    private  lateinit var addTask:com.google.android.material.floatingactionbutton.FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new__task)
        var database = FirebaseDatabase.getInstance().reference

        tugas1= findViewById(R.id.taskreq)
        detailtugas1 = findViewById(R.id.taskreq1)
        addTask = findViewById(R.id.addnewtask)

        addTask.setOnClickListener(this)

    }



    private fun savedata() {
        val tugas = tugas1.text.toString().trim()
        val detTugas = detailtugas1.text.toString().trim()

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
        val datatask = Taskdata(taskid,tugas,detTugas)
        if (taskid!=null)
        {
            ref.child(taskid).setValue(datatask).addOnCompleteListener{
                Toast.makeText(applicationContext, "Data ditambahkan ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        savedata()
    }

}
