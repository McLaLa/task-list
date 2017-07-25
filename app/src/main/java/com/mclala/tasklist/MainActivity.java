/*
Alemed Muganlinsky
* */

package com.mclala.tasklist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mclala.tasklist.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    private ArrayList<List> taskList; //innehåller alla skapade listor i vyn
    private MainActivityAdapter adapter; //egen adapter
    private int positionList = -1;
    private ListView listView;
    private final Context context = this;
    private final int LIST_ID = 1; //för activity for result

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSavedTasks(); //läser in sparade listor
        createComponents();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List list = taskList.get(position);
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra("list", list);
                startActivityForResult(i, LIST_ID);
                //skickar listan till ListActivity och tar bort listan, för att en uppdaterad lista kommer att skickas tillbaka
                positionList = position;
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                positionList = position;
                return false;
            }
        });
    }

    public void createComponents(){
        adapter = new MainActivityAdapter(this, taskList);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the list_main_actions items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_new:
                showNewDialog();
                return true;
            case R.id.action_about:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //list_main_actions.setHeaderTitle("Settings");
        menu.add(0, v.getId(), 0, R.string.edit);
        menu.add(1, v.getId(), 1, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == 1) {
            taskList.remove(taskList.get(positionList));
            Toast.makeText(this, R.string.list_deleted, Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }else if(item.getGroupId() == 0){
            showInputDialog();
        }else {
            return false;
        }
        return true;
    }

    public void getSavedTasks(){
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("list", null);
        Type type = new TypeToken<ArrayList<List>>() {}.getType();

        if(gson.fromJson(json, type)!=null){
            taskList = gson.fromJson(json, type);
        }else{
            taskList = new ArrayList<List>();
        }
    }

    public void saveTasks(){
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        editor.putString("list", json);
        editor.commit();
    }

    private void showNewDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(R.string.add_new_list);

        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String s = input.getEditableText().toString();
                if(!s.matches("")){
                    if(s.length() > 20){
                        s = s.substring(0, 20);
                    }
                    s = s.replace("\n", "").replace("\r", "");
                    List newList = new List(s);
                    taskList.add(newList);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        final AlertDialog alertDialog = alert.create();
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    //keyboard dyker upp när edit väljs i fokus
                }
            }
        });
        alertDialog.show();
    }

    private void showInputDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(R.string.edit_list);

        final EditText input = new EditText(context);
        input.setText(taskList.get(positionList).getListName());
        alert.setView(input);

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                List l = taskList.get(positionList);
                String s = input.getEditableText().toString();
                if(!s.matches("")){
                    if(s.length() > 20){
                        s = s.substring(0, 20);
                    }
                    s = s.replace("\n", "").replace("\r", "");
                    l.setListName(s);
                    Toast.makeText(context, R.string.list_changed, Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        final AlertDialog alertDialog = alert.create();
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    //keyboard dyker upp när edit väljs i fokus
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int request, int result, Intent data) {
        if(result == RESULT_OK){
            List l = (List) data.getSerializableExtra("list");
            taskList.set(positionList, l);
            adapter.notifyDataSetChanged();
            //tar emot den nya listan och lägger den i taskList samt uppdaterar vyn
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        saveTasks();
    }
}