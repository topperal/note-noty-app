package com.example.shortmess;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;
//класс, основанный на Alert Dialog
public class CustomDialogClassGet {

    //private Activity act;
    private boolean choiceGet = false;
    //int myId;
    //String myDivider;
    //конструктор
    public CustomDialogClassGet(){
    };
    //отображает Alert Dialog
    public void showDialogGet(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_button_get);//не забываем указывать на каком layout работаем
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
        text.setText(msg);
        //обработчик события на кнопку
        //запоминает индекс(в случае рандома индекс = -1), чтобы потом передать в сервис
        //и там уже вызвать по этому индексу нужное body из БД messengerList
        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.btn_random);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)activity).myN = -1;
                dialog.dismiss();
                //этот код работает, если применять рандом непосредственно внутри приложения
                    if(true) return;
                        if (((MainActivity) activity).choiceSpill == null) {
                            ((MainActivity) activity).quoter = ((MainActivity) activity).phrases[0];
                        }
                        else {
                            ((MainActivity) activity).r = new Random();
                            ((MainActivity) activity).randomNum = ((MainActivity) activity).r.nextInt(((MainActivity) activity).phrases.length);
                            ((MainActivity) activity).quoter = ((MainActivity) activity).phrases[((MainActivity) activity).randomNum];
                        }
            }
        });

        //обработчик события на кнопку(получаем фразу из текста по индексу)
        Button dialogBtn_third = (Button) dialog.findViewById(R.id.btn_index);
        dialogBtn_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //если текст не был разделен, то фраза совпадаетс первым элементом массива phrases, который, в свою очередь,
                //содержит весь текст text_body
                if (((MainActivity) activity).choiceSpill == null) {
                    ((MainActivity) activity).quoter = ((MainActivity) activity).phrases[0];
                }
                else {//если текст разделен, т.е. можно вызвать фразу по индексу, то открываем AlertDialog, где надо вводить индекс
                    com.example.shortmess.CustomDialogClassInputIndex alert = new com.example.shortmess.CustomDialogClassInputIndex();
                    alert.showDialogInputIndex(activity, "Input index...");
                }
                dialog.cancel();
            }
        });
        //обработчик события на кнопку(закрываем Alert Dialog)
        ImageButton dialogBtn_close = (ImageButton) dialog.findViewById(R.id.close);
        dialogBtn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //если текст не был разделен, то проверяем
                if (((MainActivity) activity).choiceSpill == null) {
                    //если сам массив фраз пуст или его первый элемент, то в конечную фразу передаем "сообщения нет"
                    if (((MainActivity) activity).phrases == null || ((MainActivity) activity).phrases[0] == null){
                        ((MainActivity)activity).quoter = "сообщения нет";
                    }
                    else {//если что то из верхнего условия не пусто, то в конечную фразу записываем первый элемент массива
                        ((MainActivity) activity).quoter = ((MainActivity) activity).phrases[0];
                    }
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
