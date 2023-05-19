package com.example.spendwise.Anim;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.spendwise.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public  class ButtonAnimation {

    private static Animation fadeOpen;
    private static Animation fadeClose;


//    private static Fragment myFragment =new Fragment(
//            R.layout.fragment_dashboard);

    public static void floatingButtonAnimation(Fragment myFragment, boolean isOpen,FloatingActionButton fab_plus, FloatingActionButton fab_minus, TextView fab_plus_text,TextView fab_minus_text){

        fadeOpen= AnimationUtils.loadAnimation(myFragment.getActivity(),R.anim.fade_open);
        fadeClose=AnimationUtils.loadAnimation(myFragment.getActivity(),R.anim.fade_close);

        if(isOpen){
            fab_plus.startAnimation(fadeClose);
            fab_minus.startAnimation(fadeClose);
            fab_plus.setClickable(false);
            fab_minus.setClickable(false);
            fab_plus_text.startAnimation(fadeClose);
            fab_minus_text.startAnimation(fadeClose);
            fab_plus_text.setClickable(false);
            fab_minus_text.setClickable(false);
        }
        else{
            fab_plus.startAnimation(fadeOpen);
            fab_minus.startAnimation(fadeOpen);
            fab_plus.setClickable(true);
            fab_minus.setClickable(true);
            fab_minus_text.startAnimation(fadeOpen);
            fab_plus_text.startAnimation(fadeOpen);
            fab_plus_text.setClickable(true);
            fab_minus_text.setClickable(true);
        }
        isOpen=!isOpen;

    }
}
