package com.example.sanat.dressexchangev03;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class ItemsList extends Fragment
                    implements RecycleAdapter.ItemClickListener {

    //Login Variables
    private String loginID = "17";

    //Dress Variables
    private JSONArray dressList = null;
    private String[] likes = null;
    private ArrayList<String> liked = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecycleAdapter adapter;
    View match = null;
    private Boolean matched = false;

    //Double Tap Variables
    private final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    private long lastClickTime = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.activity_items_list, container, false);
        refresh();
        scrollCards(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void refresh()
    {
        try {
            //Retrieve Dresses
            String refresh = new ServerComm().execute("refresh", loginID).get();
            JSONObject refreshed = new JSONObject(refresh);
            dressList = new JSONArray(refreshed.getString("dresses"));
            JSONArray filter = new JSONArray();
            for(int i = 0; i < dressList.length(); i++)
            {
                if (!dressList.getJSONObject(i).getString("seller_id").equals(loginID))
                {
                    filter.put(dressList.getJSONObject(i));
                }
            }
            dressList = filter;
            likes = refreshed.getString("likes").split(";");

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void scrollCards(View view)
    {
        recyclerView = view.findViewById(R.id.list);

        int numberOfColumns = 1;

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        adapter = new RecycleAdapter();
        adapter.setType(1);
        adapter.setmData(dressList);
        adapter.setmInflater(LayoutInflater.from(getActivity()));
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        Button add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    private void add()
    {
        Intent intent = new Intent(getActivity(), NewDress.class);

        Bundle b = new Bundle();
        b.putString("login", loginID); //Your id
        intent.putExtras(b); //Put your id to your next Intent

        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);

        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){

            ImageView img = view.findViewById(R.id.heart);
            img.setAlpha(0);
            img.setBackgroundResource(R.drawable.heart_filled);

            likeAction(position);
            //removeCard(position);
            lastClickTime = 0;
        } else {
            //onSingleClick(v);
        }
        lastClickTime = clickTime;
    }

    private void likeAction(int position)
    {
        try {
            String likedUser = dressList.getJSONObject(position).getString("seller_id");
            Boolean noMatch = true;

            for (int i = 0; i < likes.length; i++) {
                if (likedUser.equals(likes[i])) {
                    noMatch = false;
                    //MATCH
                    match();
                }
            }
            if (noMatch) {
                String likes = new ServerComm().execute("likes", loginID, likedUser).get();
                liked.add(likedUser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void match()
    {
        matched = true;

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        match = inflater.inflate(R.layout.match_screen, null);

        ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView skipLabel = match.findViewById(R.id.skipLabel);
        skipLabel.setPaintFlags(skipLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //addView(match, layout);

        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matched = false;
                closeMatchScreen();

            }
        });

    }

    private void closeMatchScreen()
    {
        final ViewGroup viewGroup = (ViewGroup)match.getParent();
        final View fView = match;
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(0);
        fadeOut.setDuration(200);

        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeOut);

        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                viewGroup.removeView(fView);
            }
        });

        match.setAnimation(animation);

        match.startAnimation(animation);
    }

}
