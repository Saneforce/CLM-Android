package saneforce.sanclm.activities.Model;

import java.util.List;

public class ExpandListModel {

        String parents;
        List<String> children;

        public ExpandListModel(String parents, List<String> children) {
            this.parents = parents;
            this.children = children;
        }

        public String getParents() {
            return parents;
        }

        public void setParents(String parents) {
            this.parents = parents;
        }

        public List<String> getChildren() {
            return children;
        }

        public void setChildren(List<String> children) {
            this.children = children;
        }

}
