package com.eebax.bestmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by jaafaralmahy on 9/14/17.
 */
public class MoviesAdapter extends BaseAdapter {

    String[] imageUrls;


    private LayoutInflater inflater;

    Context context;

    MoviesAdapter(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class Holder {
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View view;

        view = inflater.inflate(R.layout.grid_image, null);
        holder.img = (ImageView) view.findViewById(R.id.imageView);

        Picasso.with(context).load(imageUrls[position]).into(holder.img);


        return view;
    }
}

/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.grid_image, parent, false);
            holder = new ViewHolder();
            assert view != null;

            holder.imageView = (ImageView) view.findViewById(R.id.imageView);

           // holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Picasso.with(context).load(IMAGE_URLS[position]).into((Target) holder);

        return view;
    }
}

    class ViewHolder {
    ImageView imageView;
    }
*/



