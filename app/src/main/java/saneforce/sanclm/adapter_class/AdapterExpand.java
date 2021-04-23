package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.DemoActivity;
import saneforce.sanclm.activities.Model.ExpandModel;
import saneforce.sanclm.activities.Model.PopFeed;

public class AdapterExpand extends BaseExpandableListAdapter {
    Context context;
    ArrayList<ExpandModel> adapt_list=new ArrayList<ExpandModel>();
    String selectcategory="";

    //here i=listposition i1=expandlistposition  listposition mean adapt_list position like o,1,2
    // expandlistposition mean adapt_list have another list to get that list by using position is expandlistpostion

    //listposition for get title and set as group and expandlistposition for get inner arraylist value and set as child



    public AdapterExpand(Context context,ArrayList<ExpandModel> adapt_list,String selectcategory){
        this.adapt_list=adapt_list;
        this.context=context;
        this.selectcategory=selectcategory;
    }

    @Override
    public int getGroupCount() {
        return adapt_list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        //need to send child size
        Log.v("printing_child_list",adapt_list.get(i).getChildren().size()+"");
        return adapt_list.get(i).getChildren().size();
    }

    @Override
    public Object getGroup(int i) {
        //here need to get parent title
        return adapt_list.get(i).getParents();
    }

    @Override
    public Object getChild(int i, int i1) {
        //here need to get child each list value
        return adapt_list.get(i).getChildren().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        String listTitle = (String) getGroup(i);
        Log.v("printing_expand",listTitle);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_item_expandable, null);
        }
        convertView.setPadding(10, 10, 10, 10);
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.txt_parent);
        ImageView img_group=(ImageView)convertView.findViewById(R.id.img_parent);

            if(b){
                img_group.setImageResource(R.mipmap.down_arrow);
                img_group.setRotation(180);
            }
            else{
                img_group.setImageResource(R.mipmap.down_arrow);
                img_group.setRotation(0);
            }
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        final PopFeed expandedListText = (PopFeed) getChild(i, i1);
        Log.v("printing_expand",expandedListText.getTxt());
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_item_selection_list_tp, null);
        }
        TextView expandedListTextView = (TextView) view
                .findViewById(R.id.txt_name);
        final CheckBox check=(CheckBox)view.findViewById(R.id.check);
        RelativeLayout itemcheck = (RelativeLayout)view.findViewById(R.id.lay_row);
        if(selectcategory.equalsIgnoreCase("d") && DemoActivity.drNeed.equalsIgnoreCase("1") ||
                (selectcategory.equalsIgnoreCase("ch") && DemoActivity.chmNeed.equalsIgnoreCase("1"))
                ||  selectcategory.equalsIgnoreCase("hos")) {
            check.setEnabled(false);
            expandedListTextView.setEnabled(false);

        }
        else {
            check.setEnabled(true);
            expandedListTextView.setEnabled(true);
        }

        expandedListTextView.setText(expandedListText.getTxt());
        Log.v("printing_checkbox",expandedListText.isClick()+" name "+expandedListText.getTxt());


        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PopFeed mmm = (PopFeed) getChild(i, i1);
              /*  if(DemoActivity.cltyp.equalsIgnoreCase("1") && selectcategory.equalsIgnoreCase("c")){
                    if (b) {
                        check.setChecked(true);
                        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                        animation1.setDuration(200);
                        animation1.setStartOffset(200);
                        //animation1.setFillAfter(true);
                        check.startAnimation(animation1);
                        adapt_list.get(i).getChildren().get(i1).setClick(true);

                        for(int k=0;k<adapt_list.get(i).getChildren().size();k++){
                            if(k!=i1){
                                adapt_list.get(i).getChildren().get(k).setClick(false);
                            }
                        }
                        List<PopFeed> list1=new ArrayList<>();
                        for(int k=0;k<adapt_list.size();k++){
                            if(k!=i){
                                list1=adapt_list.get(i).getChildren();
                                for(int j=0;j<list1.size();j++){
                                    adapt_list.get(i).getChildren().get(j).setClick(false);
                                }
                            }
                        }

                    }
                }
               else {*/
                    if (b) {
                        check.setChecked(true);
                        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                        animation1.setDuration(200);
                        animation1.setStartOffset(200);
                        //animation1.setFillAfter(true);
                        check.startAnimation(animation1);
                        adapt_list.get(i).getChildren().get(i1).setClick(true);

                    } else {
                        check.setChecked(false);
                        adapt_list.get(i).getChildren().get(i1).setClick(false);
                    }
                    notifyDataSetChanged();
                }
          //  }
        });
        check.setChecked(expandedListText.isClick());

        itemcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked()){
                    check.setChecked(false);
                    adapt_list.get(i).getChildren().get(i1).setClick(false);
                }
                else{
                    check.setChecked(true);
                    AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                    animation1.setDuration(200);
                    animation1.setStartOffset(200);
                    //animation1.setFillAfter(true);
                    check.startAnimation(animation1);
                    adapt_list.get(i).getChildren().get(i1).setClick(true);
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}



