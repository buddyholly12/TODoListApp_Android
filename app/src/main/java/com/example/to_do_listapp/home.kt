package com.example.to_do_listapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.tasklist.*

class home : AppCompatActivity(){

private lateinit var taskList: MutableList<Taskdata>
    private lateinit var listTAsk : ListView
    private lateinit var donebutton : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        listTAsk= findViewById(R.id.Task_list)
        val ref= FirebaseDatabase.getInstance().getReference("Taskdata")
        taskList = mutableListOf()
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //TODO("Not yet implemented")
                if(snapshot.exists())
                {
                    for (h in snapshot.children)
                    {
                        val Taskdata = h.getValue(Taskdata::class.java)
                        if (Taskdata != null) {
                            taskList.add(Taskdata)
                        }

                    }
                    val adapter = TaskAdapter(this@home,R.layout.tasklist,taskList)
                    listTAsk.adapter =adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })
        addnewmenu.setOnClickListener{
            val intent = Intent(this@home,New_Task::class.java)
            startActivity(intent)
        }



    }
}

