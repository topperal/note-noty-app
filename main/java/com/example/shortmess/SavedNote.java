package com.example.shortmess;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class SavedNote extends Fragment {//НЕ ЗАБЫВАЕМ что надо наследовать класс Fragment

    ImageButton retuntback;
    String myTitle;
    String myBody;
    String myDivider;
    int updateId;
    EditText name_folder;
    EditText text_body;
    Button update;
    Button choicebar_get;
    private final String TAG = "SavedNote";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.savednote, null);//всегда нужно для фрагмента, указываем на каком layout работаем

        ImageButton returnback = (ImageButton) v.findViewById(R.id.returnback);
        name_folder = (EditText) v.findViewById(R.id.name_folder);
        text_body = (EditText) v.findViewById(R.id.text_body);
        Button update = (Button) v.findViewById(R.id.update);
        Button choicebar_get = (Button) v.findViewById(R.id.choicebar_get);

        //получаем данные из Adapter, т.е. из ArchiveFrag
        //чтобы вытащить сохраненные данные из БД по id
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            updateId = bundle.getInt("id",0);
            getTitlebyID();//получаем название папки по переданному id
            getBodybyID();//получаем содержимое папки по переданному id
        }

        name_folder.setText(myTitle);
        text_body.setText(myBody);
        //обрабочик события по нажатию на кнопку
        //можно перевыбрать как получать фразу из массива (рнадом или индекс)
        //только надо удалять shortcut и добавлять новый
        //shortcut обновляет только текстовые поля автоматически на кнопку change
        choicebar_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((MainActivity) getActivity()).choiceSpill = myDivider;
                CustomDialogClassGet alert = new CustomDialogClassGet();
                alert.showDialogGet(getActivity(), "Choose how get an item from the text");
            }
        });
        //обработчик события по нажатию на кнопку change
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateObjMess();//обновляем БД
            }
        });
        //обработчик события по нажатию на кнопку (возвращаемся на предыдущую активность, т.е. в ArchiveFrag)
        returnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return v;//ВАЖНО не забываем
    }
    //функция, обновляющая БД
    public void updateObjMess() {
        SQLiteDatabase db = ((MainActivity)getActivity()).dbHelper.getWritableDatabase();//вызываем БД
        ContentValues cv = new ContentValues();

        Log.d(TAG, "--- Update mytable: ---");
        //изменяем название папки(если оно не изменилось, то оно просто перезаписывается)
        cv.put("title", name_folder.getText().toString());
        // обновляем по id
        int updCount = db.update("folderShape", cv, "id = ?",
                new String[] { ""+ updateId});
        //Log.d(TAG, "updated rows count = " + updCount);
        //удаляем строки, содержащие текст body, по idTitle
        int delCount = db.delete("messengerList", "idTitle = ?", new String[] { ""+ updateId});

        if(myDivider != null) {//если разделитель строк не пустой, то в новую строку ms записываем разделенный text_body
            for (String ms : text_body.getText().toString().split(myDivider)) {
                cv = new ContentValues();
                cv.put("body", ms);
                cv.put("idTitle", updateId);
                db.insert("messengerList", null, cv);//вставляем в БД обновленный messengerList
                //Log.d(TAG, "row inserted, ID = " + myId + " ms  " + ms);
            }
        }else{//иначе в столбец body записывается весь текст, содеражщийся в text_body
                cv = new ContentValues();
                cv.put("body",text_body.getText().toString());
                cv.put("idTitle", updateId);
                db.insert("messengerList", null, cv);//вставляем в БД обновленный messengerList
                //Log.d(TAG, "row inserted, ID = " + myId+ " ms  "+text_body.getText().toString());
        }
    }
    @SuppressLint("Range")
    public void getTitlebyID() {//получаем название папки по id
        SQLiteDatabase db = ((MainActivity) getActivity()).dbHelper.getWritableDatabase();//вызываем БД
        //устанавливаем курсор
        Cursor c = db.query("folderShape", null, "id = ?", new String[] { ""+ updateId}, null, null, null);
        if (c != null) {//пока курсор не пустой, двигаемся вперед и записываем в строки myTitle,myDivider значения из соотв строк листа
            if (c.moveToFirst()) {
                myTitle =  c.getString(c.getColumnIndex("title")) ;
                myDivider =  c.getString(c.getColumnIndex("divider")) ;
            }
        }
        c.close();
    }

    @SuppressLint("Range")
    public void getBodybyID() {//получаем содержимое папки по id и сразу примением к нему разделение текста, заранее выбранное
        SQLiteDatabase db = ((MainActivity) getActivity()).dbHelper.getWritableDatabase();
        Cursor c = db.query("messengerList", null, "idTitle = ?", new String[] { ""+ updateId}, null, null, null);
        int lengthC = c.getCount();//узнаем размер messengerList
        int countC = 0;
        ((MainActivity)getActivity()).phrases = new String[lengthC];//создаем массив строк размера messengerList
        if (c != null) {
            if (c.moveToFirst()) {
                myBody = "";
                do {//пока курсор не пройдет по всему messengerList, записываем по порядку разделенную строку
                    ((MainActivity)getActivity()).phrases[countC]=myBody;
                        if (myDivider == null) {//если разделителя строки  нет, то передаем сплошной текст из столбца body
                            myBody +=  c.getString(c.getColumnIndex("body"));
                        }
                        else {//если разделитель есть
                            myBody += c.getString(c.getColumnIndex("body")) + myDivider;
                        }
                    countC++;
                    //Log.d(TAG, "get  getBodybyID "+c.getString(c.getColumnIndex("body"))+"   body "+ myBody);
                } while (c.moveToNext());
            }
        }
        c.close();
    }
    }
