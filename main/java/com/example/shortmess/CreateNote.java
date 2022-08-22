package com.example.shortmess;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateNote extends Fragment {//НЕ ЗАБЫВАЕМ что надо наследовать класс Fragment

    private static final String CHANNEL_ID = "MyChannel";

    private EditText name_folder;
    public EditText text_body;
    private Button apply_button;
    private ImageButton returnback;
    //choicebar_noty + CustomDialogClassNoty пока что не сделано, ибо не вижу практической пользы
    //но в закомментированном виде пусть остаются

//    private Button choicebar_noty;
    private Button choicebar_split;
    private Button choicebar_get;

    final String LOG_TAG = "CreateNote";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.createnote, null);//всегда нужно для фрагмента, указываем на каком layout работаем


        name_folder = (EditText) v.findViewById(R.id.name_folder);
        text_body = (EditText) v.findViewById(R.id.text_body);
        apply_button = (Button) v.findViewById(R.id.apply_button);
        returnback = (ImageButton) v.findViewById(R.id.returnback);

        choicebar_split = (Button) v.findViewById(R.id.choicebar_spill);
        choicebar_get = (Button) v.findViewById(R.id.choicebar_get);
//        choicebar_noty = (Button) v.findViewById(R.id.choicebar_noty);
        //обнуляем, что при создании следующего folder он не записывал в новый saved note предыдущий saved note
        ((MainActivity)getActivity()).phrases = null;

        //обработчик события на конпку create
        //заполняем БД, данными, полученными в этом фрагменте
        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTobd();
            }
        });

       /* choicebar_noty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogClassNoty alert = new CustomDialogClassNoty();
                alert.showDialogNoty(getActivity(), "Choose how you will get notification");
            }
        });*/
        //обработчик события по нажатию  на кнопку(вызываем AlertDialog с выбором способа разделения текста)
        choicebar_split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogClassSplit alert = new CustomDialogClassSplit(text_body.getText().toString());
                alert.showDialogSplit(getActivity(), "Choose how split the text");
            }
        });
        //обработчик события по нажатию на кнопку(вызываем AlerDialog с выбором способа получения фразы из массива текста)
        choicebar_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogClassGet alert = new CustomDialogClassGet();
                alert.showDialogGet(getActivity(), "Choose how get an item from the text");
            }
        });
        //обработчик события по нажатию на кнопку(закрываем фрагмент и возвращаемся к предыдущей активност, т. е. к Archivefrag)
        returnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return v;//не забываем возвращать, т.к. работаем во фрагменте
    }
    //метод, заполняющий БД
    @SuppressLint("Range")
    public void saveTobd () {
        ContentValues cv = new ContentValues();//переменная, через которую заносим в БД данные
        SQLiteDatabase db = ((MainActivity)getActivity()).dbHelper.getWritableDatabase();
        cv.put("title", name_folder.getText().toString());//в БД в столбец title заносим название папки
        cv.put("colour", Color.DKGRAY);//в БД в столбец colour устанвливаем дефолтный цвет для папки
        //в БД в столбец divider заносим строку, содеражащую делитель (она может быть пустой)
        cv.put("divider", ((MainActivity)getActivity()).choiceSpill);

        long rowID = db.insert("folderShape", null, cv);//вставляем в БД folderShape по строкам и запоминаем id строки
        //Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        //если phrases, полученная после применения split к text_body в CreateNote, пустая
        //т.е. split не был применен, то записываем в нее массив строк, длинной 1, содержащий весь текст text_body
        if(((MainActivity)getActivity()).phrases == null) {
            ((MainActivity)getActivity()).phrases = new String[1];
            ((com.example.shortmess.MainActivity)getActivity()).phrases[0] = text_body.getText().toString();
        }
        //если phrases не пустая, то создаем строку ms, состояющую из этих phrases
        //и записываем в лист БД messengerList в соотвестующие столбцы - в body - phrases, в idTitle - id строки, соотв листу folderShape
        if (((MainActivity)getActivity()).phrases != null) {
        for(String ms : ((MainActivity)getActivity()).phrases) {
            cv = new ContentValues();
            cv.put("body",ms);
            cv.put("idTitle",rowID);
            db.insert("messengerList", null, cv);
            //Log.d(LOG_TAG, "row inserted, ID = " + rowID+ " ms  "+ms);
        }
        }
        else {
            cv = new ContentValues();
            cv.put("body", "нет сообщения");//если phrases[0] = ""
            cv.put("idTitle", rowID);

            db.insert("messengerList", null, cv);//вставляем в БД messengerList
        }
    }
    }
