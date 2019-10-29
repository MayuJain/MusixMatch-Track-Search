package com.example.inclass07;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class musicAdapter extends ArrayAdapter<Music> {

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Music music = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.music_items, parent, false);
            viewHolder=new ViewHolder();
            viewHolder.trackTextView = (TextView) convertView.findViewById(R.id.trackTextView);
            viewHolder.albumTextView = (TextView) convertView.findViewById(R.id.albumTextView);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
            viewHolder.artistTextView = (TextView) convertView.findViewById(R.id.artistTextView);
            convertView.setTag(viewHolder);

        }
        else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.trackTextView.setText(music.trackName);
        viewHolder.artistTextView.setText(music.artistName);
        viewHolder.albumTextView.setText(music.albumName);
        viewHolder.dateTextView.setText(music.updateTime);
        return convertView;

    }
    private static class ViewHolder{
        TextView trackTextView;
        TextView dateTextView;
        TextView artistTextView;
        TextView albumTextView;
    }


    public musicAdapter(@NonNull Context context, int resource, @NonNull List<Music> objects) {
        super(context, resource, objects);
    }
}