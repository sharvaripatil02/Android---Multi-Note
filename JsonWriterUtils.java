package com.example.patil.multinote;

import android.content.Context;
import android.util.JsonWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by patil on 2/20/2017.
 */

public class JsonWriterUtils {
    public static final String PATH_TO_JSON = "multinotedata.json";


    public static void writeToDisc(List<Note> allNotes, Context context){
        try {
            File file = new File(context.getFilesDir().getPath().toString()+"/"+PATH_TO_JSON);
            file.createNewFile();
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file,false)); //Overwrite File always
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.setIndent("  ");
            writeJSONNotes(allNotes, jsonWriter);
            jsonWriter.close();
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void writeJSONNotes(List<Note> allNotes, JsonWriter writer){
        try {
            writer.beginArray();
             for(Note oneNote: allNotes){
                writeOneNote(oneNote,writer);
            }
            writer.endArray();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //writing in json file .json
    private static void writeOneNote(Note note, JsonWriter writer){
        try {
            writer.beginObject();
            writer.name("title").value(note.getTitle());
            writer.name("desc").value(note.getContent());
            String datetoWrite = JsonUtils.sdf.format(note.getUpdatedDate());
            writer.name("date").value(datetoWrite);
            writer.endObject();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
