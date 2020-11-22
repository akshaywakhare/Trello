package com.example.track_trigger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.argb;
public class ToDoActivity<t> extends AppCompatActivity {

    private ListView mListView;
    private List<Commentr> mTweets;
    private Commentr mComment;

    private TextView mTextStatus;

    private FloatingActionButton addButton;
    private FloatingActionButton clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding variables to UI elements
        mTextStatus = (TextView) findViewById(R.id.list_status);
        mListView = (ListView) findViewById(R.id.list);
        addButton = (FloatingActionButton)  findViewById(R.id.addButton);
        // clearButton = (FloatingActionButton) findViewById(R.id.clearButton);

        mTweets = generateData();
        refreshList();

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addDialog();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearDialog();
            }
    });

        mListView.setLongClickable(true);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
            deleteOne(pos);
            return true;
        }
    });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            modifyOne(position);
        }
    });
}

    // ALERTS DIALOGS
    private void deleteOne(int pos) {
        final int position = pos;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.deleteOne_title);
        alert.setMessage(R.string.deleteOne_message);

        alert.setPositiveButton(R.string.app_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteOnePos(position);
                refreshList();
            }
        });

        alert.setNegativeButton(R.string.app_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private void clearDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Supprimer liste.");
        alert.setMessage("Do you want to delete the entire task list?\n");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteAll();
                refreshList();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private void addDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.addOne_title);
        alert.setMessage(R.string.addOne_message);

        // Create TextView
        final EditText name = new EditText(this);
        name.setHint(R.string.addOne_name);

        final EditText text = new EditText(this);
        text.setHint(R.string.addOne_task);

        // Checkbox
        final CheckBox importantCheck = new CheckBox(this);
        importantCheck.setText(R.string.addOne_important);

        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(70, 0, 70, 0);

        layout.addView(name, layoutParams);
        layout.addView(text, layoutParams);
        layout.addView(importantCheck, layoutParams);

        alert.setView(layout);

        alert.setPositiveButton(R.string.app_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Random color & add to list
                Random rnd = new Random();
                int color = argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                String t = text.getText().toString();
                String important;
                if (importantCheck.isChecked()) {
                    important = "y";
                } else {
                    important = "n";
                }

                if (name.length() > 0 || text.length() > 0) {
                        mComment = new Commentr(color, name.getText().toString(), text.getText().toString(), important );
                    //String tr=text.getText().toString();
//                    do (name.getText().toString(),tr,important)
                    AddItem(mComment);
                    refreshList();
                }
            }
        });

        alert.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }


    private void modifyOne(final int position) {

        mComment = mTweets.get(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.modifyOne_title);
        alert.setMessage(R.string.modifyOne_message);

        // Create TextView
        final EditText name = new EditText (this);
        name.setText(mComment.getPseudo());

        final EditText text = new EditText(this);
        text.setText(mComment.getText());

        // Checkbox
        final CheckBox importantCheck = new CheckBox(this);
        importantCheck.setText(R.string.addOne_important);

        if(mComment.getImportant().equals("y")) {
            importantCheck.setChecked(true);
        }

        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(70, 0, 70, 0);

        layout.addView(name, layoutParams);
        layout.addView(text, layoutParams);
        layout.addView(importantCheck, layoutParams);

        alert.setView(layout);


        alert.setPositiveButton(R.string.app_modify, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int whichButton) {

                String important;
                if(importantCheck.isChecked()) {
                    important = "y";
                }
                else {
                    important = "n";
                }

                if(name.length() > 0 || text.length() > 0) {
                    mComment = new Commentr(mComment.getColor(), name.getText().toString(), text.getText().toString(), important);
                    ModifyItem(position, mComment);
                    refreshList();
                }
            }
        });

        alert.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();


    }

    // LIST REFRESH
    private void refreshList() {
        RowAdapter adapter = new RowAdapter(ToDoActivity.this, mTweets);
        mListView.setAdapter(adapter);

        if(mTweets.size() > 0 ) {
            mTextStatus.setText(R.string.app_listNoEmpty);
        }
        else {
            mTextStatus.setText(R.string.app_listEmpty);
        }
    }

    // GENERATE INITIAL DATA
    private List<Commentr> generateData() {
        mTweets = new ArrayList<>();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String myData = myPrefs.getString("myTodoData",null);

        if(myData != null)
        {
            try {
                JSONArray jsonArray = new JSONArray(myData);
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    String data  = jsonArray.getString(i);
                    String[] splitData = data.split("\\.");

                    mTweets.add(new Commentr(Integer.parseInt(splitData[0]), splitData[1], splitData[2], splitData[3]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            mComment = new Commentr(BLACK, "Florent", getString(R.string.app_example), "y");
            AddItem(mComment);
        }

        return mTweets;
    }

    // JSON SAVE & ACTIONS
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ModifyItem(int position, Commentr e) {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String myData = myPrefs.getString("myTodoData",null);

        JSONArray jsonArray = null;

        try {
            jsonArray = new JSONArray(myData);
            jsonArray.remove(position);
            jsonArray.put(e.getColor() + "." + e.getPseudo() + "." + e.getText() + "." + e.getImportant());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        mTweets.remove(position);
        mTweets.add(e);

        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("myTodoData", jsonArray != null ? jsonArray.toString() : null);
        editor.apply();
    }

    private void AddItem(Commentr e) {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String myData = myPrefs.getString("myTodoData",null);

        JSONArray jsonArray = null;
        if(myData == null) {
            jsonArray = new JSONArray();
            jsonArray.put(e.getColor() + "." + e.getPseudo() + "." + e.getText() + "." + e.getImportant());
            mTweets.add(e);
        }
        else {
            try {
                jsonArray = new JSONArray(myData);
                jsonArray.put(e.getColor() + "." + e.getPseudo() + "." + e.getText() + "." + e.getImportant());
                mTweets.add(e);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("myTodoData", jsonArray != null ? jsonArray.toString() : null);
        editor.apply();
    }

    private void deleteOnePos(int pos) {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String myData = myPrefs.getString("myTodoData",null);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(myData);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                jsonArray.remove(pos);
            }
            mTweets.remove(pos);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("myTodoData", jsonArray != null ? jsonArray.toString() : null);
        editor.apply();
    }

    private void deleteAll() {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        JSONArray jsonArray = new JSONArray();
        mTweets = new ArrayList<>();

        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("myTodoData", jsonArray.toString());
        editor.apply();
    }

}
