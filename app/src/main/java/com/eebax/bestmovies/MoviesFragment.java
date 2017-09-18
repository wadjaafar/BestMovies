package com.eebax.bestmovies;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mohamed Jaafar on 9/12/17.
 */

public class MoviesFragment extends Fragment {

    GridView gridView;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchMovies();
    }

    public void fetchMovies(){
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute();
    }

    private String[] getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        final String DATA_RESULT = "results";
        final String DATA_POSTER = "poster_path";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(DATA_RESULT);

        String[] resultStrs = new String[moviesArray.length()];
        for(int i = 0; i < moviesArray.length(); i++) {

            String poster;
            String posterUrl;
            final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

            JSONObject moviesPosters = moviesArray.getJSONObject(i);
            poster = moviesPosters.getString(DATA_POSTER);
            posterUrl = IMAGE_BASE_URL + poster;
            resultStrs[i] = posterUrl;

        }

        for (String s : resultStrs) {
            Log.v("Results", "Movies poster: " + s);
        }

        return resultStrs;

    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


        @Override
        protected String[] doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            String key = "your_api_key";

            String language = "en-US";
            int pages = 1;

            try {

                final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";

                final String API_PARAM = "api_key";
                final String LANGUAGE_PARAM = "language";
                final String PAGE_PARAM = "pages";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(API_PARAM, key)
                        .appendQueryParameter(LANGUAGE_PARAM, language)
                        .appendQueryParameter(PAGE_PARAM, Integer.toString(pages))
                        .build();

                URL url = new URL(builtUri.toString());

                System.out.println(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            gridView.setAdapter(new MoviesAdapter(getActivity(), result));
        }
    }

}
