package com.example.patil.multinote;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by patil on 2/20/2017.
 */


public class JsonUtils extends AsyncTask<Void, Void, List<Note>> {

    public static final String PATH_TO_JSON = "multinotedata.json";
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
    private Context myContext;
    private MyAdapter asyncAdapter;

    public JsonUtils(Context context, MyAdapter adapter){
        this.myContext=context;
        this.asyncAdapter=adapter;
    }

    @Override
    protected List<Note> doInBackground(Void... dummyParam) {
        List<Note> allNotes = new ArrayList<Note>();
        try {
            String temp = "This is readFile here "+myContext.getFilesDir().getPath().toString()+"/"+PATH_TO_JSON;
            InputStreamReader reader = new InputStreamReader(new FileInputStream(myContext.getFilesDir().getPath().toString()+"/"+PATH_TO_JSON));
            JsonReader jsonReader = new JsonReader(reader);
            allNotes = readJSONNotes(jsonReader);
        }
        catch(FileNotFoundException e){
            System.out.println("No JSON found");
        }catch (Exception e){
            e.printStackTrace();
        }
        return allNotes;
    }

    private List<Note> readJSONNotes(JsonReader reader){
        List<Note> notes = new ArrayList<Note>();
        try {
            reader.beginArray();
            while(reader.hasNext()){
                Note readNote = readOneNote(reader);
                if(null != readNote)
                    notes.add(readNote);
            }
            reader.endArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return notes;
    }

    private Note readOneNote(JsonReader reader){
        Note newnote = null;
        try {
            reader.beginObject();
            reader.nextName(); //Title label
            String title = reader.nextString();
            reader.nextName(); //Desc label
            String desc = reader.nextString();
            reader.nextName(); //Date label
            Date updatedDate = sdf.parse(reader.nextString());
            newnote = new Note(title, desc, updatedDate);
            reader.endObject();
            return newnote;
        }catch(Exception e){
            e.printStackTrace();
        }
        return newnote;
    }

    @Override
    protected void onPostExecute(List<Note> notes) {
        super.onPostExecute(notes);
        asyncAdapter.refreshData(notes);
        asyncAdapter.notifyDataSetChanged();
    }
}
