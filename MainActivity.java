package com.example.patil.multinote;


        import android.content.DialogInterface;
        import android.content.Intent;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.text.ParseException;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.ClickInterFace{

    private static final String NOTE_LABEL = "_notedata";
    private static final String TITLE_LABEL = "_title";
    private static final String DESC_LABEL = "_desc";
    private static final String DATE_LABEL = "_date";
    private static final String POSITION_LABEL = "_position";


    private TextView textView;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Note> listItems;

    @Override
    protected void onPause() {
        super.onPause();
        JsonWriterUtils.writeToDisc(this.listItems, getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //EDIT ACTIVITY BACK CLICK = DATA is NULL
                if(data!=null) {
                String title = data.getStringExtra(TITLE_LABEL);
                String content = data.getStringExtra(DESC_LABEL);
                String dateString = data.getStringExtra(DATE_LABEL);
                Date newdate = new Date();
                try {
                    newdate = JsonUtils.sdf.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int position = data.getIntExtra(POSITION_LABEL, -1);
                Note newNote = new Note(title, content, newdate);
                if (position == -1)
                    this.listItems.add(newNote);
                else
                    this.listItems.set(position, newNote);
                //adapter.refreshData(this.listItems);
                adapter.notifyDataSetChanged();
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textView = (TextView) findViewById(R.id.textView);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        listItems = new ArrayList<>();
        //set adpter
        adapter = new MyAdapter(listItems, this);
        recyclerView.setAdapter(adapter);
        adapter.setClickInterFace(this);
        JsonUtils util = new JsonUtils(getApplicationContext(), adapter);
        util.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "about Activity", Toast.LENGTH_SHORT).show();
                onClickabout();
                return true;
            case R.id.addnote:
                Toast.makeText(this, "add note", Toast.LENGTH_SHORT).show();
                onClickEdit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onClickEdit() {
        startActivityForResult(new Intent("com.example.patil.multinote.EditActivity"),1);
        Toast.makeText(this,"Edit Activity", Toast.LENGTH_SHORT).show();
    }

    private void onClickabout() {

        startActivity(new Intent("com.example.patil.multinote.AboutActivity"));
        Toast.makeText(this, "about Activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSingleClick(int position) {
        Note note = (Note) listItems.get(position);

        Intent editIntent = new Intent("com.example.patil.multinote.EditActivity");
        Bundle params = new Bundle();
        params.putString(TITLE_LABEL,note.getTitle());
        params.putString(DESC_LABEL,note.getContent());
        params.putString(DATE_LABEL,note.getUpdatedDate().toString());
        params.putInt(POSITION_LABEL,position);
        editIntent.putExtra(NOTE_LABEL,params);

        startActivityForResult(editIntent,1);
        //Toast.makeText(this,"Edit Activity", Toast.LENGTH_SHORT).show();

    }

}

