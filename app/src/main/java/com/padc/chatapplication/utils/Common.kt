package com.padc.chatapplication.utils

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*


fun Activity.showError(error: String) {
   Snackbar.make(this.window.decorView,error,
      Snackbar.LENGTH_SHORT).show()
}

fun getDate(milliSeconds1: Long): String? {

   var milliSeconds=System.currentTimeMillis()-milliSeconds1
   val minutes = milliSeconds / 1000 / 60
   val hours=  milliSeconds / 1000 / 60 / 60
   val formatter = SimpleDateFormat("dd/MM/yyyy")
  // val calendar: Calendar = Calendar.getInstance()
  // calendar.timeInMillis = milliSeconds
   val date=formatter.format(milliSeconds1)

   var result=""
   if(minutes>=60){

       if(hours>=24){
          result=date
       }else{
          result="$hours hours"
       }
   }else{
      result="$minutes mins"
   }

   return result
}


@RequiresApi(Build.VERSION_CODES.O)
fun getDate1(milliSeconds: Long): String? {

   val formattedDate =  SimpleDateFormat("yyyy-MM-dd")
   val calendar: Calendar = Calendar.getInstance()
   calendar.setTimeInMillis(milliSeconds)

   val dateString=formattedDate.format(calendar.time)
   val newformattedDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
   val newDate= newformattedDate.parse(dateString)

   var localFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd")
   var localDate=LocalDate.parse(dateString,localFormatter)


   // diff date
   var period = Period.between(localDate, LocalDate.now())

   //val timeFormattedDate =  SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
  // val realDate= timeFormattedDate.format(dateString)
   var result=""
   if(period.days<1){

      if(newDate.hours<1){
         result="$newDate.minutes min"
      }else{
         result="${newDate.hours} hours"
      }
   }else{
      result="$localDate"
   }

   return result
}