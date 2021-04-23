package saneforce.sanclm.activities.Model;

public class ModelStoringFileTime {
    private String filename;
    private String timing;
    private String prdName;

    public ModelStoringFileTime(String filename, String timing,String prdName) {
        this.filename = filename;
        this.timing = timing;
        this.prdName=prdName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

     public ModelStoringFileTime(String slideNam){
          this.filename=slideNam;
          }

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    @Override
    public boolean equals(Object obj) {
        ModelStoringFileTime dt = (ModelStoringFileTime)obj;
         if(filename == null) return false;
         if(dt.filename.equals(filename)) return true;
         return false;
    }
}