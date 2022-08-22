package com.example.shortmess;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//класс, основанный на Alert Dialog
public class CustomDialogClassInputIndex {

    private EditText input_index;
    private com.example.shortmess.MainActivity act;
    //отображает Alert Dialog
    public void showDialogInputIndex(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        act = (com.example.shortmess.MainActivity) activity;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.input_index);//не забываем указывать на каком layout работаем
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
        text.setText(msg);
        //получаем введенный индекс
        EditText input_ind = (EditText) dialog.findViewById(R.id.btn_input_index);
        //обработчик события на кнопку
        Button bot_ok = (Button) dialog.findViewById(R.id.btn_yes);
        bot_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((com.example.shortmess.MainActivity)activity).myN = Integer.parseInt(input_ind.getText().toString().trim());
                //если введенный индекс больше выходит за массив строк phrases
                if ((int)((com.example.shortmess.MainActivity)activity).myN > (int)((com.example.shortmess.MainActivity)activity).phrases.length-1) {
                    Toast.makeText(act, R.string.error,Toast.LENGTH_SHORT).show();
                }
                else {//если индекс не выходит за массив, то в конченую фразу передаем ту строку из phrases с этим индексом
                    ((com.example.shortmess.MainActivity) activity).quoter = ((com.example.shortmess.MainActivity) activity).phrases[((com.example.shortmess.MainActivity) activity).myN];
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

