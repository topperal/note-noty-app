package com.example.shortmess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;
//Activity, который вызывается при нажатии на shortcut и который запускает MyService
//RecievActivity + MyService должны быть описаны в AndroidManifest
public class RecievActivity extends Activity {

    final String LOG_TAG = "myLogs";
    public DBHelper dbHelper;
    public SQLiteDatabase db;
    public Random r;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //определяем новый БД в этом Activity
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        //получаем intent и EXTRA_TEXT из CustomDialogStyle(->ArchiveFrag)
        Intent recieveintent = getIntent();
        String body = recieveintent.getStringExtra(Intent.EXTRA_TEXT);
        //полученный EXTRA_TEXT разделяем и записываем в новый массив строк ms
        //где [0] элемент отвечает за название folder, а [1] отвечает за способ получения и саму строку из всего text_body
        String[] ms = body.split("@@@");
        int idTitle = Integer.parseInt(ms[0]);
        int indexBody = Integer.parseInt(ms[1]);
        //создаем новый intent, который связывает с Service
        Intent intent = new Intent("com.example.shortmess.NOTYSERVICE");
        //передаем ключи и инфо, которую должен получить сервис
        intent.putExtra("title", getTitlebyID(idTitle));
        intent.putExtra("text", getBodybyID(idTitle, indexBody));
        Log.d(LOG_TAG,this.getPackageName());
        intent.setPackage(this.getPackageName());
        //осн фишка - не открывается осн приложение, если оно убрано из списка задач
        //и ReceivActivity запускается по нажатию на shortcut, делает нужные преобразования, передает инфу Service и выключается
        startService(intent);
        dbHelper.close();//не забываем закрывать БД
        finish();
    }
    //получаем название folder по переданному id (ms[0])
    @SuppressLint("Range")
    public String getTitlebyID(int idTitle) {

        String finishTitle="";
        Cursor c = db.query("folderShape", null, "id = ?", new String[] { ""+idTitle }, null, null, null);

        if (c != null) {
            if (c.moveToFirst()) {
                finishTitle =  c.getString(c.getColumnIndex("title"));
            }
        }
        c.close();
        return finishTitle;
    }
    //если indexBody, который получаем из ClassGet, = -1, то запускаем рандом(выбираем произвольные int из массива от 0 до размера ДБ),
    //в противном случае он = 0 и больше (не выходит за массив), это индекс конкретной фразы и мы ищем по всей таблице,
    //увеличивая счетчик, пока счетчик не совпадет с заданным индексом
    @SuppressLint("Range")
    public String getBodybyID(int idTitle, int indexBody) {

        String myBody = "";
        int countC = 0;
        int indRand = indexBody;//переменная, изначально совпадающая с ms[1]
        Cursor c = db.query("messengerList", null, "idTitle = ?", new String[] { ""+idTitle }, null, null, null);
        //если ms[1] == -1, т.е. пользователь выбрал get at Random, то в indRand записывается рандомное число от 0 до размера БД
        if (Integer.toString(indRand) == "-1") {
            r = new Random();
            indRand = r.nextInt(c.getCount());
        } else {
            indRand = indexBody;
        }
         if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        myBody = c.getString(c.getColumnIndex("body"));
                        if (countC == indRand) {
                            break;
                        }
                        countC++;
                    } while (c.moveToNext());//пока позиция курсора не сопадет с заданным индексом или с рандомно сгенирированным индексом
                }
            }
        c.close();
        return myBody;
    }
}