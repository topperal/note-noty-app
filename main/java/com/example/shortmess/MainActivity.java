package com.example.shortmess;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArchiveFrag archive;//создаем фрагмент
    FragmentTransaction fTrans;

    public DBHelper dbHelper; //класс, который создает и описывает БД
    public String[] phrases; //массив после приминения split к тексту
    public Random r;//промежуточная переменная чтобы сгенерировать рандомный индекс в CustomDialogClassGet
    public int randomNum;//рандомный индекс в CustomDialogClassGet
    public String quoter;//элемент (фраза) из массива текста, которая и попадает в notification
    public int myN;//запоминаем введенный индекс, чтобы вызвать элемент с этим индексом в CustomDialogClassInputIndex

    public String choiceSpill = null;//если null, то есть пользователь не разделил текст, то в quoter передается весь текст из text_body

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHelper = new DBHelper(this);//создаем экземпляр класса, который описывает БД

        //открывает фрагмент при включении приложения
        archive = new ArchiveFrag();
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.frgmCont, archive);
        fTrans.addToBackStack(null);
        fTrans.commit();

    }
}