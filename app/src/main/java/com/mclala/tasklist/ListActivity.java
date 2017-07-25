/*
Alemed Muganlinsky
* */

package com.mclala.tasklist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mclala.tasklist.R;

public class ListActivity extends ActionBarActivity {
    //ärver från ActionBarActivity för att få upp action bar i applikationen med activities
    private ListView listView;
    private ListActivityAdapter adapter; //egen adapter
    //private EditText editTextNewList;
    private List list;
    private TextView listName;
    private final Context context = this; //context som används i input dialog
    private int positionItem = -1; //håller reda på item i listan som är i action

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);

        createComponents(); //skapar och initierar variabler samt komponenter

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem listItem = list.getList().get(position);
                //listItem är item som användaren tryckt på
                if (listItem.isStroked()) {
                    //om item är stryken vid tryckning på den, ändras layouten och objektet sätts till isStroked == false
                    listItem.isStroked(false);
                    TextView rowTextView = (TextView) view.findViewById(R.id.rowTextView);
                    rowTextView.setText(listItem.getItemName());
                    rowTextView.setPaintFlags(rowTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    ImageView checkBox = (ImageView) view.findViewById(R.id.checkBox);
                    checkBox.setImageResource(R.drawable.ic_unchecked);
                } else {
                    listItem.isStroked(true);
                    TextView rowTextView = (TextView) view.findViewById(R.id.rowTextView);
                    rowTextView.setText(listItem.getItemName());
                    rowTextView.setPaintFlags(rowTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    ImageView checkBox = (ImageView) view.findViewById(R.id.checkBox);
                    checkBox.setImageResource(R.drawable.ic_checked);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //lagrar positionen när användaren gör en long click
                positionItem = position;
                return false;
            }
        });

    }

    private void createComponents(){
        Intent intent = getIntent();
        list = (List)intent.getSerializableExtra("list"); //tar emot listan som skickas från MainActivity

        adapter = new ListActivityAdapter(this, list.getList());
        listName = (TextView)findViewById(R.id.textView);
        listName.setText(list.getListName());
        //editTextNewList = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        registerForContextMenu(listView); //registrerar listView för popup menyer
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the list_main_actions items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //list_main_actions.setHeaderTitle("Settings");
        menu.add(0, v.getId(), 0, R.string.edit);
        menu.add(1, v.getId(), 1, R.string.delete);
        //alternativ vid long click
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //har koll på vad användaren väljer och vad som ska ske
        if (item.getGroupId() == 0) { //item.getTitle() == "Edit"
            showInputDialog();
        }else if(item.getGroupId() == 1){ //item.getTitle() == "Delete"
            list.deleteItem(list.getList().get(positionItem));
            adapter.notifyDataSetChanged(); //notifikation till adaptern att uppdatera listView
            Toast.makeText(this, R.string.item_deleted, Toast.LENGTH_SHORT).show();
        }
        else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // när användaren väljer att återgå till parent activity anropas onBackPressed metod
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
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

    private void showNewDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(R.string.add_new_item);

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
                    if(s.length() > 30){
                        s = s.substring(0, 30);
                    }
                    s = s.replace("\n", "").replace("\r", "");
                    ListItem newItem = new ListItem(s);
                    list.addItem(newItem);
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
        //alert dialog som visas när användaren ändrar namnet på item
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(R.string.edit_item);

        final EditText input = new EditText(context);
        input.setText(list.getList().get(positionItem).getItemName());
        alert.setView(input);

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ListItem li = list.getList().get(positionItem);
                String s = input.getEditableText().toString();
                if(!s.matches("")){
                    if(s.length() > 30){
                        s = s.substring(0, 30);
                    }
                    s = s.replace("\n", "").replace("\r", "");
                    li.setItemName(s);
                    Toast.makeText(context, R.string.item_changed, Toast.LENGTH_LONG).show();
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
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed(){
        //returnerar en uppdaterad version av listan och avslutar activity
        Intent returnIntent = new Intent();
        returnIntent.putExtra("list", list);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}