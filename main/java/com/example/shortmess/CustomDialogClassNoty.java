package com.example.shortmess;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomDialogClassNoty {

    //int setHour, setMinute;

    public void showDialogNoty(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
        text.setText(msg);

        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.btn_tap);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"По клику" ,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.btn_time);
        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"По времени" ,Toast.LENGTH_SHORT).show();
                /*TimePickerDialog timePickerDialog = new TimePickerDialog(
                        (MainActivity) activity,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                setHour = hourOfDay;
                                setMinute = minute;
                                Calendar calendar = Calendar.getInstance();

                                calendar.set(0,0,0,setHour,setMinute);

                                Intent intent1 = new Intent((MainActivity)activity, AlarmReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast((MainActivity)activity, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) (MainActivity)activity.getSystemService(Context.ALARM_SERVICE);
                                alarmManager.setRepeating (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (), AlarmManager.INTERVAL_DAY, pendingIntent());

                            }
                            }, 12,0,false);
                System.out.println(setHour+" : "+setMinute);

                //timePickerDialog.updateTime(setHour,setMinute);
                timePickerDialog.show();*/
                dialog.cancel();
            }
        });

        ImageButton dialogBtn_close = (ImageButton) dialog.findViewById(R.id.close);
        dialogBtn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
