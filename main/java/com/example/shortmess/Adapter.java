package com.example.shortmess;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// implements Filterable - для описания работы фильтра, который используется в SearchView в ArchiveFrag

//класс Adapter (переходник) – класс, который отвечает за связь элементов java кода с View-компонентами.
// Т.е. получая набор java объектов,  адаптер преобразует его уже в набор View-компонентов,
// которые и использует в дальнейшем RecyclerView
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    final String TAG = "Adapter";
    CardView folder;
    List<ObjMess> titles;
    List<String> alltexts;
    LayoutInflater inflater;
    Context context;
    FragmentTransaction fTrans;//для перехода к новому фрагменту

    List<ObjMess> titlesFull; //для методов Filter, копируем иск список, чтобы проводить с ним операции

    //конструктор
    public Adapter(Context ctx) {
        this.context = ctx;
        this.titles = new ArrayList<>();
        createObjMess();
        this.inflater = LayoutInflater.from(ctx);
        titlesFull = new ArrayList<>(titles);
    }
    //метод вызывается для создания объекта ViewHolder, в конструктор которого необходимо передать созданный View-компонент,
    //с которым в дальнейшем будут связываться java объекты
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_grid_layout, parent, false);
        return new ViewHolder(view);
    }
    //отвечает за связь java объекта и View
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position).title);
        holder.position = holder.getAdapterPosition();
        //Log.d(TAG,"Color position "+holder.position+" color num "+titles.get(holder.position).bg_color);
        holder.folder.setCardBackgroundColor(titles.get(holder.position).bg_color);
    }
    //сообщает количество элементов в списке
    @Override
    public int getItemCount() {
        if(titles == null){
            return 0;
        }else {
            return titles.size();
        }
    }
    //методы, отвечающие за фильтрацию RecycleView
    @Override
    public Filter getFilter() {
        return titlesFilter;
    }

    private Filter titlesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            createObjMess();//обновляем titles перед копирование
           titlesFull = new ArrayList<>(titles);//создаем новый лист, копирующий исх, и проводим над ним операции
            List<ObjMess> filteredList = new ArrayList<>();//создаем отфильтрованный список

            if(constraint==null||constraint.length()==0) {//если поисковый запрос пуст, то в отфильтрованный список добавляется весь исх
                filteredList.addAll(titlesFull);
            }
            else {
                //создаем строку запроса, с которой будем сранивать названия folder и фильтровать
                String filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim();
                //для названий папок из исх списка проверяем, включает ли в себя строку поискового запроса
                //если включает - то добавляем этот заголовок в отфильтрованный список
                for(ObjMess header : titlesFull) {
                    if (header.title.toLowerCase(Locale.ROOT).contains(filterPattern)) {

                        Log.d(TAG,"filter title "+header.title+" color "+header.bg_color);
                        filteredList.add(header);
                    }
                }
            }
            //создаем конечный отфильтрованный результат
            //где значения конечных результатов совпадают с отфильтрованным списком
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }
        //возвращает результирующий список после фильтрации
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                titles.clear();
                titles.addAll((List) results.values);
                notifyDataSetChanged(); //дает адаптеру знать, что список элементов изменился и нужно  перерисовать элементы на экране
        }
    };

    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        Button bar;
        CardView folder;
        int position;
    // конструктор, который принимает на вход View-компонент строки и ищет все дочерние компоненты
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView2);
            bar = itemView.findViewById(R.id.bar);
            folder = itemView.findViewById(R.id.folder);

            //оброботчик события по нажатия на весь folder, хранящийся в Adaptar и отображающийся в ArchiveFrag
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    com.example.shortmess.SavedNote fragment = new com.example.shortmess.SavedNote();
                    Bundle bundle = new Bundle();//для передачи данных между activity (в нашем случаем между intent in fragments)
                    bundle.putInt("id", titles.get(position).mesId);//по позиции вытаскиваем из листа ObjMess id, с одноименным ключом
                    fragment.setArguments(bundle);
                    //переход к новому фрагменту, который содержит данные, полученные при создании folder
                    fTrans = ((Activity) context).getFragmentManager().beginTransaction();
                    fTrans.replace(R.id.frgmCont, fragment);
                    fTrans.addToBackStack(null);
                    fTrans.commit();
                }
            });
            //оброботчик события при нажатии на кнопку в правом верхнем углу folder
            //в нем совершаются действия непосредственно над целоым folder (удаление, перекрас, создание ярлыка)
            bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CustomDialogStyle alert = new CustomDialogStyle(Adapter.this, position, folder);
                    alert.showDialogStyle((Activity) context, "Style");

                }
            });
        }
    }
    //создаем лист объектов, с которым работаем в адаптере
    public void createObjMess() {
        titles.clear();
        SQLiteDatabase db = ((MainActivity)context).dbHelper.getWritableDatabase();//вызываем БД
        //устанавливаем курсор в начало  БД
        Cursor c = db.query("folderShape", null, null, null, null, null, null);
        //просматриваем БД, в соотвествии с проходом курсора по таблице
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int titleColIndex = c.getColumnIndex("title");
            int colorColIndex = c.getColumnIndex("colour");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(TAG,
                        "ID = " + c.getInt(idColIndex) + ", title = "
                                + c.getString(titleColIndex)+" color "+ c.getInt(colorColIndex));
                // создаем лист ObgMess и добавляем в него значения mesId, titles, alltexts, bg_color,
                //которые вытащили из БД
               titles.add(new ObjMess(c.getInt(idColIndex),c.getString(titleColIndex),null, c.getInt(colorColIndex)));

                // переход на следующую строку, а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();
    }
}


