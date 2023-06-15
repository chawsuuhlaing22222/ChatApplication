package com.padc.chatapplication.network.api

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.padc.chatapplication.data.vos.GroupMessageVO
import com.padc.chatapplication.data.vos.GroupVO
import com.padc.chatapplication.data.vos.MessageVO
import com.padc.chatapplication.data.vos.SmallChatVO
import java.io.ByteArrayOutputStream
import java.util.*

object RealtimeDatabaseFirebaseApiImpl : RealTimeFirebaseApi {

    private val database: DatabaseReference = Firebase.database.reference
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference


    override fun sendMessage(messageVO: MessageVO) {
        //Log.i("db", database.toString())
        database.child("contactsAndMessages").child(messageVO.senderId ?: "")
            .child(messageVO.receiptId ?: "").child(messageVO.timeStamp ?: "")
            .setValue(messageVO)
            .addOnCompleteListener {
                // Log.i("task",it.result.toString())
            }

        database.child("contactsAndMessages").child(messageVO.receiptId ?: "")
            .child(messageVO.senderId ?: "").child(messageVO.timeStamp ?: "")
            .setValue(messageVO)
    }

    override fun getAllMsg(
        currentUserId: String,
        receiptId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {

        database.child("contactsAndMessages").child(currentUserId ?: "")
            .child(receiptId ?: "").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList = arrayListOf<MessageVO>()
                    snapshot.children.forEach { dataSnapShot ->
                        dataSnapShot.getValue(MessageVO::class.java)?.let {
                            messageList.add(it)
                        }
                    }
                    onSuccess(messageList)
                }
            })

    }

    override fun uploadImageAndCreate(image: Bitmap, msg: MessageVO) {
        val baos = ByteArrayOutputStream()

         image.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val data = baos.toByteArray()

        val imageRef = storageReference.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            //
        }.addOnSuccessListener { taskSnapshot ->
            //
        }

        val urlTask = uploadTask.continueWithTask {
            return@continueWithTask imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            val imageUrl = task.result?.toString()
            msg.file = imageUrl
            sendMessage(msg)
        }
    }

    override fun getAllChatListByUserId(
        currentUserId: String,
        onSuccess: (List<SmallChatVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.child("contactsAndMessages").child(currentUserId ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val smallChatList = arrayListOf<SmallChatVO>()

                    snapshot.children.forEach { dataSnapShot ->

                        var smallChat: SmallChatVO = SmallChatVO()
                        smallChat.receiptUserId = dataSnapShot.key
                        val messageList = arrayListOf<MessageVO>()
                        var valueList =
                            dataSnapShot.value as kotlin.collections.HashMap<String, HashMap<String, String>>
                        /* dataSnapShot.getValue(List<MessageVO>::class.java)?.let {
                            messageList.add(it)
                        }*/

                        var lastKey = valueList.keys.max()
                        var lastMap = valueList.get(lastKey)
                        smallChat.msg = lastMap?.get("message")
                        smallChat.file = lastMap?.get("file")
                        smallChat.lastSentTimeStamp= lastMap?.get("timeStamp")
                        smallChat.lstSentUserName = lastMap?.get("lastMessageSentUserName")
                        smallChatList.add(smallChat)


                    }
                    onSuccess(smallChatList)
                }
            })
    }

    override fun createNewGroup(
        img: Bitmap,
        groupVO: GroupVO,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val baos = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val imageRef = storageReference.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            //
        }.addOnSuccessListener { taskSnapshot ->
            //
        }

        val urlTask = uploadTask.continueWithTask {
            return@continueWithTask imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            val imageUrl = task.result?.toString()
            groupVO.groupCoverImg = imageUrl
            createGroup(groupVO, onSuccess, onFailure)
        }
    }

    override fun getAllGroups(
        currentUserId: String,
        onSuccess: (List<GroupVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.child("groups").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val groupList = arrayListOf<GroupVO>()
                    snapshot.children.forEach { dataSnapShot ->
                        dataSnapShot.getValue(GroupVO::class.java)?.let {
                           var members: List<String>? = it.members?.split(",")

                            if(members?.contains(currentUserId) == true){
                                groupList.add(it)
                            }

                        }
                    }
                    onSuccess(groupList)
                }
            })
    }

    override fun sendGroupMessag(groupVO: GroupVO,msg: GroupMessageVO) {


            database.child("groups").child(msg.groupId ?: "")
                .child("messages").child(msg.sendTimeStamp.toString())
                .setValue(msg)
                .addOnCompleteListener {
                    // Log.i("task",it.result.toString())
                }


      /*  groupVO.messages?.put(msg.sendTimeStamp.toString(),msg)
        database.child("groups").child(msg.groupId ?: "")
            .setValue(groupVO)
            .addOnCompleteListener {
                // Log.i("task",it.result.toString())
            }*/


        /*groupVO.messages?.keys?.forEach {
            database.child("groups").child(msg.groupId ?: "")
                .child("messages")
                .setValue(groupVO.messages)
                .addOnCompleteListener {
                    // Log.i("task",it.result.toString())
                }
        }*/


    }

    override fun getAllGroupMsg(
        groupId: String,
        onSuccess: (List<GroupMessageVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.child("groups").child(groupId).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val msgList = arrayListOf<GroupMessageVO>()
                    snapshot.children.forEach { dataSnapShot ->
                        dataSnapShot.getValue(GroupMessageVO::class.java)?.let {
                       msgList.add(it)

                        }
                    }
                    onSuccess(msgList)
                }
            })
    }

    override fun uploadImageAndSendGroupMsg(img: Bitmap,groupVO: GroupVO, msg: GroupMessageVO) {
        val baos = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val imageRef = storageReference.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            //
        }.addOnSuccessListener { taskSnapshot ->
            //
        }

        val urlTask = uploadTask.continueWithTask {
            return@continueWithTask imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            val imageUrl = task.result?.toString()
            msg.file = imageUrl
            sendGroupMessag(groupVO,msg)
        }
    }

    override fun uploadGifAndSendMsg(file: Uri, msg: MessageVO) {
        val imageRef = storageReference.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(file)
        uploadTask.addOnFailureListener {
            //
        }.addOnSuccessListener { taskSnapshot ->
            //
        }

        val urlTask = uploadTask.continueWithTask {
            return@continueWithTask imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            val imageUrl = task.result?.toString()
            msg.file = imageUrl
            sendMessage(msg)
        }
    }

    override fun uploadGifAndSendGroupMsg(file: Uri, groupVO: GroupVO, msg: GroupMessageVO) {
        val imageRef = storageReference.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(file)

        uploadTask.addOnFailureListener {
            //
        }.addOnSuccessListener { taskSnapshot ->
            //
        }

        val urlTask = uploadTask.continueWithTask {
            return@continueWithTask imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            val imageUrl = task.result?.toString()
            msg.file = imageUrl
            sendGroupMessag(groupVO,msg)
        }
    }


    private fun createGroup(
        groupVO: GroupVO, onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.child("groups").child(groupVO.createdTimeStamp ?: "")
            .setValue(groupVO)
            .addOnCompleteListener {
                onSuccess("Successfully created group")
            }
            .addOnCanceledListener {
                onFailure("error to create new group")
            }


    }
}