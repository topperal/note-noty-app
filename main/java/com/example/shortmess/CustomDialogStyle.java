package com.example.shortmess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.hardware.lights.LightState;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;
//класс, основанный на Alert Dialog
public class CustomDialogStyle {
    private final String TAG = "CustomDialogStyle";
    Adapter n;
    int pos;
    CardView lay;
    int mDefaultColor;
    Activity act;

    public DBHelper dbHelper;
    public SQLiteDatabase db;
    public String mainTitle;
    private String finalTitle;
//конструктор
    public CustomDialogStyle(Adapter note, int pos, CardView folder){
        n = note;
        this.pos = pos;
        lay = folder;
        Log.d(TAG,"Constructor position "+ pos);
    };

    //отображает Alert Dialog
    public void showDialogStyle(Activity activity, String msg){
        act = activity;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_button_style);//не забываем указывать на каком layout работаем
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //обработчик события по нажатию на кнопку(создаем shortcut на homescreen)
       Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.btn_shortcuts);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Создаем ярлык", Toast.LENGTH_SHORT).show();
     //получаем id из ObjMess (n - название Adapter здесь), которое будем передавать в RecievActivity и там по нему вытаскивать Title
                int mesId = ((ObjMess) n.titles.get(pos)).mesId;
                int countId = mesId;//создаем уникальное id для каждого folder и соотвествующего ему shortcut
                //создаем новый intent и связываем его с сервисом
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setClassName("com.example.shortmess", "com.example.shortmess.RecievActivity");
                //передаем в intent строку, содержащую id для названия папки и номер индекса, по которой вызываем фразу
                intent.putExtra(Intent.EXTRA_TEXT,mesId+"@@@"+((MainActivity)activity).myN);

                finalTitle = getTitleforShort(mesId);//получаем название папки по id
                //конструктор shortcut (не забываем про implementation в build.gradle)
                ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(activity, "id"+countId)
                        .setShortLabel(finalTitle)
                        .setLongLabel(mainTitle)
                        .setIcon(IconCompat.createWithResource(activity, R.drawable.letblack))
                        .setIntent(intent)
                        .build();
                //использую pinned shortcut (его функионал подоходил больше, чем dynamic)
                ShortcutManagerCompat.requestPinShortcut(activity, shortcutInfo, null);
            }
        });
        //обработчик события по нажатию на кнопку(удаляем папку из recycleView вместе с ее данными в БД)
        Button dialogBtn_third = (Button) dialog.findViewById(R.id.btn_delete);
        dialogBtn_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Удаляем", Toast.LENGTH_SHORT).show();
                int mesId = ((ObjMess) n.titles.get(pos)).mesId;
                SQLiteDatabase db = ((MainActivity)activity).dbHelper.getWritableDatabase();
                //удаляем Title и Text_body из БД по id folder, в котором вызвали этот AlertDialog
                int delText = db.delete("messengerList", "idTitle = ?", new String[] { ""+mesId });
                int delTitle = db.delete("folderShape", "id = ?", new String[] { ""+mesId });

                n.titles.clear();//очищаем ObjList titles в Adapter
                n.createObjMess();//создаем в Adapter ObjList

                //сообщает adapter что его лист данных изменился, чтобы он его пересобрал (меняются позиции folder)
                n.notifyDataSetChanged();
                List<String> delId = new ArrayList<>();//создаем лист, хранящий id, по которым надо удалить данные
                delId.add("id"+mesId);//передаем в новый лист id того folder, в котором сейчас находимся
                //shortcut с удаленными id становится не действительным и в версии выше 25-26 показывает соот соо
                //в версиях ниже просто перестает выполнять тот intent, на которое было создано
                ShortcutManagerCompat.disableShortcuts(activity, delId,"folder deleted");

                Log.d("delete title","id: "+mesId);
                dialog.cancel();
            }
        });
        //обработчик события по нажатию на кнопку(открывает окно с выбором цвета)
        Button dialogBtn_color = (Button) dialog.findViewById(R.id.btn_color);
        mDefaultColor = ContextCompat.getColor(n.inflater.getContext(), R.color.input_bg);
        dialogBtn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();//функция выбора цвета для окраса всего folder
            }
        });

        //обработчик события по нажатию на кнопку(закрываем AlertDialog, возвращаемся к ArchiveFrag)
        ImageButton dialogBtn_close = (ImageButton) dialog.findViewById(R.id.close);
        dialogBtn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
    //implementation at build.gradle - 'com.github.yukuku:ambilwarna:2.0.1'
    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(act, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;//запоминаем выбранный цвет
                lay.setCardBackgroundColor(mDefaultColor);

                SQLiteDatabase db = ((MainActivity)act).dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();

                Log.d("CustomDialogStyle", "--- Update mytable: ---");
                int mesId = ((ObjMess) n.titles.get(pos)).mesId;
                cv.put("colour", mDefaultColor);
                // обновляем по id
                int updCount = db.update("folderShape", cv, "id = ?",
                        new String[] { ""+mesId });//записываем новый цвет в БД лист folderShape по id folder, откуда вызывали AlerDialog
                Log.d("CustomDialogStyle", "updated rows count = " + updCount);
                n.createObjMess();
                //n.notifyDataSetChanged();

            }
        });

        colorPicker.show();
    }
    //функция, в которой создаем строку, передающуюся в shortcut как shortlabel (он не может быть пустым)
    @SuppressLint("Range")
    public String getTitleforShort(int mesId) {

        SQLiteDatabase db = ((MainActivity)act).dbHelper.getWritableDatabase();
        Cursor c = db.query("folderShape", null, "id = ?", new String[] { ""+mesId }, null, null, null);

        if (c != null) {//если курсор идет по непустому БД, то получаем название папки по id
            if (c.moveToFirst()) {
                mainTitle =  c.getString(c.getColumnIndex("title")).trim();
            }
            else {
                mainTitle = "empty title";
            }
        }
        else {//если курсор идет по пустому БД
            mainTitle = "empty title";
        }
        if (mainTitle.equals("")) {//если в непустом БД хранился заголовок с пустой строкой (или неск пробелов)
            mainTitle = "empty title";
        }
        c.close();
        return mainTitle;
    }
}
