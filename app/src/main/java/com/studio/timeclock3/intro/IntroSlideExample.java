package com.studio.timeclock3.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studio.timeclock3.R;

import androidx.annotation.Nullable;
import io.github.dreierf.materialintroscreen.SlideFragment;

public class IntroSlideExample extends SlideFragment {

    //Code kann vollständig für ein weiteres Itro übernommen werden
    //  Klassenamen ändern nicht vergessen! "IntroSlideExample"

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_slide_example, container, false);  //Hier das Layout angeben


        TextView text = (TextView) view.findViewById(R.id.text);    //Beispiel wie man ein Element aus dem Layout in Java auswählt
        text.setText("Example");                                    //Beispiel wie man das Element ändern kann (zB. den  Text)

        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.green;   //Hntergrundfarbe des Intro Bildschirms
    }

    @Override
    public boolean canMoveFurther() {
        return true;            //Boolean ob man im Intro weiter kann oder nicht -> (zB. irgendein Eingabewert fehlt nocht)
    }

    @Override
    public int buttonsColor() {
        return R.color.colorAccent; //Buttonfarbe
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "I won't let you go Bitch";  //Nachricht die erscheint wenn man nicht weiter kann im Intro, siehe: canMoveFurther()
    }

}
