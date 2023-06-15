package com.padc.chatapplication.network.api

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.padc.chatapplication.data.vos.MomentVO
import com.padc.chatapplication.data.vos.UserVO
import java.io.ByteArrayOutputStream
import java.util.*

object CloudFireStoreApiImpl:FirebaseApi {

    val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference

    override fun retrieveUserInfo(userId:String,
        onSuccess: (userVO: UserVO) -> Unit,
        onFailure: (String) -> Unit
    ) {


        db.collection("users")
            .addSnapshotListener { value, error ->
                error?.let {
                    onFailure(it.message ?: "Please check connection")
                } ?: run{

                    var userVO=UserVO("","","","","","","",""
                    )
                    val result = value?.documents ?: arrayListOf()

                    for (document in result) {
                        val data = document.data
                        if(data?.get("qr_code") ==userId){
                            userVO?.name = data?.get("name") as String
                            userVO?.gender = data?.get("gender") as String
                            userVO?.dob =data?.get(("dob")) as String
                            userVO?.phoneNo =data?.get(("phone_number")) as String
                            userVO?.profileImage =data?.get(("profile_image")) as String
                            userVO?.qrCode =data?.get(("qr_code")) as String
                            userVO?.email=data?.get("email") as String

                        }

                    }
                     onSuccess(userVO)
                }
            }
    }

    override fun createUser( userVO: UserVO) {

        val userMap = hashMapOf(
            "name" to userVO.name,
            "email" to userVO.email,
            "password" to userVO.password,
            "profile_image" to userVO.profileImage,
            "phone_number" to userVO.phoneNo,
            "qr_code" to userVO.qrCode,
            "dob" to userVO.dob,
            "gender" to userVO.gender
        )

        userVO.qrCode?.let { it ->
            db.collection("users")
                .document(it)
                .set(userMap)
                .addOnSuccessListener {

                    Log.d("Success", "Successfully created user") }
                .addOnFailureListener { exception->

                    Log.d("Failure", "Failed to create user") }
        }

    }



    override fun uploadImageAndCreateUser(image: Bitmap, userVO: UserVO,onSuccess: () -> Unit,onFailure: (String) -> Unit) {
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
            userVO.profileImage=imageUrl
            userVO.qrCode= FirebaseAuth.getInstance().currentUser?.uid
           createUser(userVO)
        }
    }

    override fun createContacts(userVO: UserVO, contactVO: UserVO) {
        val contactMap = hashMapOf(
            "name" to contactVO.name,
            "email" to contactVO.email,
            "password" to contactVO.password,
            "profile_image" to contactVO.profileImage,
            "phone_number" to contactVO.phoneNo,
            "qr_code" to contactVO.qrCode,
            "dob" to contactVO.dob,
            "gender" to contactVO.gender
        )

        //create sub collection
        userVO.qrCode?.let { it ->
            db.collection("users")
                .document(it)
                .collection("contacts")
                .document(contactVO.qrCode ?: "")
                .set(contactMap)
                .addOnSuccessListener {

                    Log.d("Success", "Successfully created user") }
                .addOnFailureListener { exception->

                    Log.d("Failure", "Failed to create user") }
        }




        val userMap = hashMapOf(
            "name" to userVO.name,
            "email" to userVO.email,
            "password" to userVO.password,
            "profile_image" to userVO.profileImage,
            "phone_number" to userVO.phoneNo,
            "qr_code" to userVO.qrCode,
            "dob" to userVO.dob,
            "gender" to userVO.gender
        )


        contactVO.qrCode?.let { it ->
            db.collection("users")
                .document(it)
                .collection("contacts")
                .document(userVO.qrCode ?: "")
                .set(userMap)
                .addOnSuccessListener {

                    Log.d("Success", "Successfully created user") }
                .addOnFailureListener { exception->

                    Log.d("Failure", "Failed to create user") }
        }


    }

    override fun getMyContacts(userid:String,onSuccess: (contacts: List<UserVO>) -> Unit, onFailure: (String) -> Unit) {
        db.collection("users").
        document(userid).collection("contacts")
            .addSnapshotListener { value, error ->
                error?.let {
                    //onFailure(it.message ?: "Please check connection")
                } ?: run{

                    var contacts:MutableList<UserVO> = arrayListOf()
                    val result = value?.documents ?: arrayListOf()

                    for (document in result) {
                        val data = document.data
                       var contactVO= UserVO()
                        contactVO?.name = data?.get("name") as String
                        contactVO?.gender = data?.get("gender") as String
                        contactVO?.dob =data?.get(("dob")) as String
                        contactVO?.phoneNo =data?.get(("phone_number")) as String
                        contactVO?.profileImage =data?.get(("profile_image")) as String
                        contactVO?.qrCode =data?.get(("qr_code")) as String
                        contactVO?.email=data?.get("email") as String

                       contacts.add(contactVO)

                    }
                    onSuccess(contacts)
                }
            }
    }

    override fun createMoment(momentVO: MomentVO) {

        val momentMap = hashMapOf(
            "name" to momentVO.userName,
            "profile_image" to momentVO.userProfile,
             "id" to momentVO.id,
            "description" to momentVO.description,
            "images" to momentVO.images,
            "uploadDate" to momentVO.uploadDate,
            "uploadUserId" to momentVO.uploadUserId

        )

        momentVO.id?.let {
            db.collection("moments")
                .document(it)
                .set(momentMap)
                .addOnSuccessListener {

                    Log.d("Success", "Successfully created user") }
                .addOnFailureListener { exception->

                    Log.d("Failure", "Failed to create user") }
        }


    }

    override fun uploadImage(
        image: Bitmap,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val imageRef = storageReference.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            onFailure(it.message ?: "error")
        }.addOnSuccessListener { taskSnapshot ->
            //
        }

        val urlTask = uploadTask.continueWithTask {
            return@continueWithTask imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            val imageUrl = task.result?.toString()
            if (imageUrl != null) {
                onSuccess(imageUrl)
            }

        }
    }

    override fun getAllMoments(onSuccess: (List<MomentVO>) -> Unit, onFailure: (String) -> Unit) {
        db.collection("moments")
            .addSnapshotListener { value, error ->
                error?.let {
                    onFailure(it.message ?: "Please check connection")
                } ?: run{
                    val momentsList: MutableList<MomentVO> = arrayListOf()

                    val result = value?.documents ?: arrayListOf()

                    for (document in result) {
                        val data = document.data
                        val moment = MomentVO()
                        moment.userName = data?.get("name") as String
                        moment.description = data["description"] as String?
                        moment.id = data["id"] as String
                        moment.userProfile = data["profile_image"] as String?
                        moment.images =data["images"] as String?
                        moment.uploadDate=data["uploadDate"] as String?
                        moment.uploadUserId=data["uploadUserId"] as String?
                        momentsList.add(moment)
                    }
                    onSuccess(momentsList)
                }
            }
    }

    override fun getMyMoments(
        userId: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("moments")
            .addSnapshotListener { value, error ->
                error?.let {
                    onFailure(it.message ?: "Please check connection")
                } ?: run{
                    val momentsList: MutableList<MomentVO> = arrayListOf()

                    val result = value?.documents ?: arrayListOf()

                    for (document in result) {
                        val data = document.data
                        var uploadUserId= data?.get("uploadUserId") as String
                        if(userId==uploadUserId){
                            val moment = MomentVO()
                            moment.userName = data?.get("name") as String
                            moment.description = data["description"] as String?
                            moment.id = data["id"] as String
                            moment.userProfile = data["profile_image"] as String?
                            moment.images =data["images"] as String?
                            moment.uploadDate=data["uploadDate"] as String?
                            moment.uploadUserId=data["uploadUserId"] as String?
                            momentsList.add(moment)
                        }


                    }
                    onSuccess(momentsList)
                }
            }
    }


}