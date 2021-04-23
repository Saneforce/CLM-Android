package saneforce.sanclm.activities.Model;

public class ViewTagModel {

    String code,name,lat,lng,addr;
    String reference,height,width,phno;

    public ViewTagModel(String code, String name, String lat, String lng,String addr) {
        this.code = code;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.addr=addr;
    }
    public ViewTagModel(String code, String name, String lat, String lng,String addr,String reference,String height,String width,String phno) {
        this.code = code;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.addr=addr;
        this.reference=reference;
        this.height=height;
        this.width=width;
        this.phno=phno;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public ViewTagModel(String name){
        this.name=name;
    }

    @Override
    public boolean equals(Object obj) {
        ViewTagModel dt = (ViewTagModel)obj;

        if(name == null) return false;
        if(dt.name.equals(name)) return true;

        return false;
    }
}
