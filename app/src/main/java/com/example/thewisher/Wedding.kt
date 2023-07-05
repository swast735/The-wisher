package com.example.thewisher

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.thewisher.databinding.FragmentWeddingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Wedding : Fragment() {
    private val database = Firebase.database("https://the-wisher-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val myRef = database.getReference("wedding")
    val l= arrayListOf<String>()
    var j=0
    lateinit var link:String
    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bind:FragmentWeddingBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_wedding, container, false)
        val sp=this.activity?.getSharedPreferences("SharedPref", Context.MODE_PRIVATE)
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val len:Int=snapshot.childrenCount.toInt()

                    Log.d("msg", snapshot.value.toString().plus(snapshot.key))
                    for (i in snapshot.children) {
                        l.add(i.value.toString())
                    }
                    Glide.with(this@Wedding).load(l[0]).into(bind.elems)
                    link=l[0]
                    bind.next.setOnClickListener {
                        try {
                            Glide.with(this@Wedding).load(l[++j]).into(bind.elems)
                            link=l[j]
                        }catch(e:Exception){
                            if (j > len) {
                                j = len-1
                                link=l[j]
                                Glide.with(this@Wedding).load(l[j]).into(bind.elems)
                            }
                        }
                    }
                    bind.prev.setOnClickListener {
                        try {
                            Glide.with(this@Wedding).load(l[--j]).into(bind.elems)
                            link=l[j]
                        }catch(e:Exception){
                            if (j <= -1) {
                                j = 0
                                Glide.with(this@Wedding).load(l[j]).into(bind.elems)
                                link=l[j]
                            }
                        }
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        bind.nxtAct.setOnClickListener {
            val i = Intent(activity, Des::class.java)
            i.putExtra("link",link)
            startActivity(i)
        }
        return bind.root
    }


}