package com.example.patil.multinote;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by patil on 2/20/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private List<Note> listItem;
    private Context mContext;
    //TextView textviewtitle;

    private ClickInterFace clickInterFace;

    public interface ClickInterFace{
        void onSingleClick(int position);
    }

    public void setClickInterFace(ClickInterFace newCI){
        this.clickInterFace=newCI;
    }

    public MyAdapter(List<Note> listItem, Context mContext) {
        this.listItem = listItem;
        this.mContext = mContext;
    }

    public void refreshData(List<Note> newNotes){
        this.listItem.clear();
        this.listItem.addAll(newNotes);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Note item = listItem.get(position);
        holder.texttitle.setText(item.getTitle());
        holder.textcontent.setText(item.getContent());
        holder.textDate.setText(JsonUtils.sdf.format(item.getUpdatedDate()));

        //textviewtitle = holder.texttitle.getText(item.getTitle());

        final Note data = listItem.get(position);


        holder.view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                removeCard(data);
                return true;

            }
        });
    }

    // to delete card from recycler
    private void removeCard(Note data) {

        final int currPosition = listItem.indexOf(data);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("Confirm Delete");
        alertDialog.setMessage("Are you sure you want delete note #"+(currPosition+1));

        // Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                listItem.remove(currPosition);
                notifyItemRemoved(currPosition);
                Toast.makeText(mContext.getApplicationContext(), "Deleted Note", Toast.LENGTH_SHORT).show();
            }
        });

        // Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext.getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View view;
        public TextView texttitle;
        public TextView textcontent;
        public TextView textDate;

        public ViewHolder(View itemView) {
            super(itemView);
            texttitle = (TextView) itemView.findViewById(R.id.textTitle);
            textcontent = (TextView) itemView.findViewById(R.id.textdescription);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            view = (View) itemView.findViewById(R.id.card_view);
            view.setOnClickListener(this);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clickInterFace.onSingleClick(getAdapterPosition());
//                }
//            });

        }

        @Override
        public void onClick(View v) {
            clickInterFace.onSingleClick(getAdapterPosition());
        }
    }
}

