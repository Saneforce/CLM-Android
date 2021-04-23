package saneforce.sanclm.fragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import saneforce.sanclm.AnimationView.MoveAnimation;
import saneforce.sanclm.R;

public class DashBoardMenu extends Fragment {
    LinearLayout rrrr;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv=inflater.inflate(R.layout.dashboard_menu_item,container,false);
        rrrr=(LinearLayout)vv.findViewById(R.id.rrrr);

        rrrr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateOut();
            }
        });
        return vv;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Log.v("printing_enter_anim",enter+""+transit+nextAnim);
        if(enter)
        return MoveAnimation.create(MoveAnimation.UP, enter, 500);
        else
            return MoveAnimation.create(MoveAnimation.DOWN, false, 500);
    }

    public void animateOut()
    {
        onCreateAnimation(1,false,1);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(DashBoardMenu.this);
        ft.commitAllowingStateLoss();

       /*
        MoveAnimation.create(MoveAnimation.DOWN, false, 1000);*/
       /* TranslateAnimation trans=new TranslateAnimation(0,300, 0,300);
        trans.setDuration(150);
        trans.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
               // ((BetsActivty)getActivity()).removeFrontFragmentAndSetControllToBetting();

            }
        });
        getView().startAnimation(trans);*/
    }

}
