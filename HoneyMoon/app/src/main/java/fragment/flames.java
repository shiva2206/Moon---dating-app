package fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.honeymoon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link flames#} factory method to
 * create an instance of this fragment.
 */
public class flames extends Fragment {
    private EditText nameone,nametwo;
    private ImageView imgg;
    private Button reset,flame;
    private TextView fntxt;
    private String lett;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_flames, container, false);

        nameone=view.findViewById(R.id.nameone);
        nametwo=view.findViewById(R.id.nametwo);
        imgg=view.findViewById(R.id.imggg);
        reset=view.findViewById(R.id.resse);
        flame=view.findViewById(R.id.flamm);
        fntxt=view.findViewById(R.id.fntxt);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset.setEnabled(false);
                imgg.setImageResource(R.drawable.liked);
                nameone.setText("");
                nametwo.setText("");
                fntxt.setText("");
                reset.setEnabled(true);
            }
        });


        flame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flame.setEnabled(false);
                if (!TextUtils.isEmpty(nameone.getText()) && !TextUtils.isEmpty(nametwo.getText())) {
                    fntxt.setText((nameone.getText() + "").toLowerCase(Locale.ROOT).trim() + " & " + (nametwo.getText() + "").toLowerCase(Locale.ROOT).trim());
                    int uri = R.drawable.liked;
                    uniquechrs(nameone.getText()+"",nametwo.getText()+"");
                    switch (lett) {
                        case "F":
                            uri = R.drawable.friends;
                            break;
                        case "L":
                            uri = R.drawable.lover;

                            break;
                        case "A":
                            uri = R.drawable.affection;
                            break;
                        case "M":
                            uri = R.drawable.marriage;

                            break;
                        case "E":
                            uri = R.drawable.enemy;

                            break;
                        case "S":
                            uri = R.drawable.sister;

                            break;
                        default:
                            uri = R.color.black;
                            break;

                    }
                    Picasso.get().load(uri).into(imgg);
                }else{
                    Toast.makeText(getContext(), "Enter NAMES", Toast.LENGTH_SHORT).show();
                }
                flame.setEnabled(true);
            }
        });


        return view;

    }
    public void uniquechrs(String oneu,String twou){
        List<String> onelst = new ArrayList<>();
        List<String> twolst = new ArrayList<>();
        List<String> seconelst = new ArrayList<>();
        List<String> sectwolst = new ArrayList<>();
        List<String> flamst = new ArrayList<>();

        flamst.add("F");
        flamst.add("L");
        flamst.add("A");
        flamst.add("M");
        flamst.add("E");
        flamst.add("S");

        for(char ch : oneu.toCharArray()){
            seconelst.add(ch+"");
            onelst.add(ch+"");
        }

        for(char ch : twou.toCharArray()){
            sectwolst.add(ch+"");
            twolst.add(ch+"");
        }

        for (String st : seconelst){
            if(sectwolst.contains(st)){
                twolst.remove(st);
                onelst.remove(st);
            }
        }
        while(true){
            if (onelst.contains(" ") || twolst.contains(" ")){
                onelst.remove(" ");
                twolst.remove(" ");
            }else{
                break;
            }
        }

        int lft = onelst.size()+twolst.size();
        Toast.makeText(getContext(), lft+"", Toast.LENGTH_SHORT).show();
        flamessss(lft,flamst);
    }
    public void flamessss(int lft,List<String> flamst){
        if (flamst.size()!=1){
            int x = flamst.size();
            int y = lft;
            if (y>x){
                y=lft % x;
            }
            if (y==0){
                flamst.remove(x-1);
                flamessss(lft,flamst);
            }else {
                flamst.remove(y - 1);
                y = y - 1;
                List<String> redufla = new ArrayList<>();
                for(int at = 0;at<x-1;at++){
                    if (y<=x-2){
                        redufla.add(flamst.get(y));
                    }else{
                        y=0;
                        redufla.add(flamst.get(y));
                    }
                    y++;
                }
                flamessss(lft,redufla);
            }

        }else{
            lett=flamst.get(0);
        }
    }
}