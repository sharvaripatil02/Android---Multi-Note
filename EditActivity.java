package com.example.patil.multinote;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import static java.sql.Types.NULL;

public class EditActivity extends AppCompatActivity {

    private static final String NOTE_LABEL = "_notedata";
    private static final String TITLE_LABEL = "_title";
    private static final String DESC_LABEL = "_desc";
    private static final String DATE_LABEL = "_date";
    private static final String POSITION_LABEL = "_position";
    //EditText title;
    private static int position=-1;
    private static String legacyTitle=null;
    private static String legacyDesc=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if(getIntent().hasExtra(NOTE_LABEL)){
            Bundle params = getIntent().getBundleExtra(NOTE_LABEL);
            ((EditText)findViewById(R.id.EditTextTitle)).setText(params.getString(TITLE_LABEL));
            ((EditText)findViewById(R.id.EditTextContent)).setText(params.getString(DESC_LABEL));
            legacyTitle=params.getString(TITLE_LABEL);
            legacyDesc=params.getString(DESC_LABEL);
            position=params.getInt(POSITION_LABEL);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.saveNote:
                //Toast.makeText(this, "Edit Acivity - saved n", Toast.LENGTH_SHORT).show();
                onClickSave();
                return true;

            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_edit_save, menu);
        return true;
    }


    public void onBackPressed()
    {
        String notename = ((EditText)findViewById(R.id.EditTextTitle)).getText().toString();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditActivity.this);

        alertDialog.setTitle("Note not saved");
        alertDialog.setMessage("Do you want to save note "+notename);


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                onClickSave();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }



    private void onClickSave() {
        //Make changes in memory

        EditText title = (EditText)findViewById(R.id.EditTextTitle);
        EditText content = (EditText)findViewById(R.id.EditTextContent);
        //If title Empty show Toast and no update in memory. Also show Toast
        if(title.getText() == null || title.getText().toString().length()<1) {
            Toast.makeText(this, "Note without a title cant be saved!", Toast.LENGTH_SHORT).show();
            finish();
        }

        //If note hasnt changed, doint update anything in memory
        if(!hasNoteChanged(legacyTitle, legacyDesc, title.getText().toString(),content.getText().toString())){
            //Toast.makeText(this, "No changes made on the note", Toast.LENGTH_SHORT).show();
            finish();
        }

        Intent maina =new Intent("android.intent.action.MAIN");
        maina.putExtra(TITLE_LABEL, title.getText().toString());
        maina.putExtra(DESC_LABEL,content.getText().toString());
        Date now = new Date();
        String dateToWrite = JsonUtils.sdf.format(now);
        maina.putExtra(DATE_LABEL, dateToWrite);
        maina.putExtra(POSITION_LABEL,position);
        position=-1;
        setResult(RESULT_OK,maina);
        finish();
    }

    //If no note change no dont update in memory
    private static boolean hasNoteChanged(String oldTitle, String oldDesc, String newTitle, String newDesc){
        //This means this is a new note
        if(oldTitle ==null || oldDesc == null)
            return true;
        if(oldTitle.equals(newTitle) && oldDesc.equals(newDesc))
            return false;
        return true;
    }
}
