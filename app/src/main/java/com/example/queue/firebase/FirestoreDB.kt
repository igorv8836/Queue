package com.example.queue.firebase

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreDB{
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    fun getNewsData(){
        firebaseFirestore.collection("news")
            .get()
            .addOnCompleteListener {

                if (it.isSuccessful){
                    val docs = it.result.documents
                    for (i in docs){
                        print(i.get("text"))
                    }
                } else {

                }
            }
    }
}