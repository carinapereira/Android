package com.example.carina.materialdesign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.ImageView;

public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Toolbar");
        toolbar.setSubtitle("Material Toolbar");
        toolbar.setLogo(R.mipmap.ic_launcher);

        imageView = (ImageView) findViewById(R.id.img);

        findViewById(R.id.btnShow).setOnClickListener(this);
        findViewById(R.id.btnHide).setOnClickListener(this);
        findViewById(R.id.btnTreinaWeb).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnHide:
                revelacaoHide(imageView, 2500);
                break;

            case R.id.btnShow:
                revelacaoShow(imageView,2500);
                break;

            case R.id.btnTreinaWeb:
                Intent intent = new Intent(this, TreinawebActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView,"imgtransition");
                ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
                break;
        }
    }

    public void revelacaoShow(View view, long duration){
        int cx = (view.getLeft() + view.getRight())/2;
        int cy = (view.getTop() + view.getTop())/2;
        int raioAnim = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, raioAnim);

        view.setVisibility(View.VISIBLE);
        anim.setDuration(duration);
        anim.start();
    }

    public void revelacaoHide(final View view, long duration){
        int cx = (view.getLeft() + view.getRight())/2;
        int cy = (view.getTop() + view.getTop())/2;
        int raioAnim =view.getWidth();

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, raioAnim, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        anim.setDuration(duration);
        anim.start();
    }
}
