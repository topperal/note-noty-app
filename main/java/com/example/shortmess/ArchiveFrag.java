package com.example.shortmess;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArchiveFrag extends Fragment  {//НЕ ЗАБЫВАЕМ что надо наследовать класс Fragment

    public SearchView find;//поисковая строка
    private ImageButton plus;//добавление новой заметки
    private RecyclerView list_folders;//список всех заметок
    FragmentTransaction fTrans;//для перехода к новому фрагменту

    com.example.shortmess.Adapter adapter;//объявляем адаптер

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.archive, null);//всегда нужно для фрагмента, указываем на каком layout работаем

        //так объявляются переменные в фрагментах (через v.)
        find = (SearchView) v.findViewById(R.id.find);
        plus = (ImageButton) v.findViewById(R.id.plus);
        list_folders = (RecyclerView) v.findViewById(R.id.list_folders);

        //создаем адаптер для RecycleView, который основан, на GridView
        adapter = new com.example.shortmess.Adapter(getActivity());
        //LayoutManager отвечает за форму отображения элементов
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2,
                GridLayoutManager.VERTICAL, false);
        list_folders.setLayoutManager(gridLayoutManager);
        list_folders.setAdapter(adapter);

        //описываем работу поисковой строки
        find.clearFocus();//сбросить поисковик(чтобы он не искал предыдущий запрос)
        find.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//работа с поисковым запросом
            @Override
            public boolean onQueryTextSubmit(String s) {//получаем конечный запрос
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {//обновляется при каждом изменении поискового запроса
                adapter.getFilter().filter(s);
                return true;
            }
        });

        //оброботчик события на кнопку "добавить" (ака переход к новому фрагменту)
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //создание и переход к новому фрагменту
                CreateNote createNote = new com.example.shortmess.CreateNote();

                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, createNote);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        return v;//ВАЖНО не забываем

    }
}
