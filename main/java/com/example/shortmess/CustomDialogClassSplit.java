package com.example.shortmess;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
//класс, основанный на Alert Dialog
public class CustomDialogClassSplit {

    String body;
    //конструктор
    public CustomDialogClassSplit(String body){
        this.body = body;
    };
    //отображает Alert Dialog
    public void showDialogSplit(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        ((com.example.shortmess.MainActivity)activity).choiceSpill = null;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_button_spill);//не забываем указывать на каком layout работаем
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
        text.setText(msg);

        //обработчик события при нажатии на кнопку (создаем массив из текста, фразы в котором разделены с помощью ;)
        //записываем в массив строк phrases разделенный text_body
        //в choicespill запоминает разделитель (нужно в createNote в функции saveTobd())
        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.btn_colons);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((com.example.shortmess.MainActivity)activity).phrases = body.split(";");
                ((com.example.shortmess.MainActivity)activity).choiceSpill = ";";
                dialog.dismiss();
            }
        });
        //обработчик события при нажатии на кнопку (создаем массив из текста, фразы в котором разделены с помощью \n)
        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.btn_lines);
        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((com.example.shortmess.MainActivity)activity).phrases = body.split("\n");
                ((com.example.shortmess.MainActivity)activity).choiceSpill = "\n";
                dialog.cancel();
            }
        });
        //обработчик события при нажатии на кнопку (создаем массив из текста, фразы в котором разделены с помощью :)
        Button dialogBtn_third = (Button) dialog.findViewById(R.id.btn_colons_two);
        dialogBtn_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((com.example.shortmess.MainActivity)activity).phrases = body.split(":");
                ((com.example.shortmess.MainActivity)activity).choiceSpill = ":";
                dialog.cancel();
            }
        });
        //обработчик события при нажатии на кнопку (зыкрывается AlertDialog, возвращаемся к фрагменту CreateNote)
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
