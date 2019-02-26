package com.starwings.app.smartserchapp.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.starwings.app.smartserchapp.R;
import com.starwings.app.smartserchapp.TipsDetailPage;
import com.starwings.app.smartserchapp.TipsListing;
import com.starwings.app.smartserchapp.data.Blog;

import java.util.ArrayList;

/**
 * Created by user on 29-01-2018.
 */

public class BlogAdapter extends BaseAdapter {
    ArrayList<Blog> data;
    TipsListing parentscreen;
    public BlogAdapter(ArrayList<Blog> content,TipsListing pscreen)
    {
        data=content;
        parentscreen=pscreen;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView=parentscreen.getLayoutInflater().inflate(R.layout.lst_tips_row,null);
        }


        TextView txtCaption=(TextView)convertView.findViewById(R.id.txtTitle);
        txtCaption.setText(data.get(position).getTitle());

        Button btnRead=(Button)convertView.findViewById(R.id.btnRead);


        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailPage(data.get(position));
            }
        });

        return convertView;
    }

    private void showDetailPage(Blog blog) {
        Intent intent = new Intent(parentscreen, TipsDetailPage.class);
        intent.putExtra("blogcontent",blog.getContent());
        intent.putExtra("blogtitle",blog.getTitle());
        parentscreen.startActivity(intent);
    }
}
