package saneforce.sanclm.activities.Model;

import java.util.ArrayList;
import java.util.List;

public class ExpandModel {

        String parents;
        List<PopFeed> children=new ArrayList<>();

        public ExpandModel(String parents, List<PopFeed> children) {
            this.parents = parents;
            this.children = children;
        }

        public String getParents() {
            return parents;
        }

        public void setParents(String parents) {
            this.parents = parents;
        }

        public List<PopFeed> getChildren() {
            return children;
        }

        public void setChildren(List<PopFeed> children) {
            this.children = children;
        }

        public ExpandModel(ArrayList<PopFeed> arrayList){
            this.children = children;
        }

}
