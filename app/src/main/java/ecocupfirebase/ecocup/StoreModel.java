package ecocupfirebase.ecocup;

public class StoreModel {
    private String Image;
    private Integer Sales;
    private String Bname;
    private String Mass;
    private String Water;
    private String Cos;
    private String Trees;



    public StoreModel() {}

    public StoreModel(String image,Integer sales,String bname,String mass,String water,String cos,String trees) {
        Image = image;
        Sales = sales;
        Bname = bname;
        Mass = mass;
        Water = water;
        Cos = cos;
        Trees = trees;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Integer getSales() {
        return Sales;
    }

    public void setSales(Integer sales) {
        Sales = sales;
    }

    public String getBname() { return Bname; }

    public void setBname(String bname) { Bname = bname; }

    public String getMass() {        return Mass; }

    public void setMass(String mass) {        Mass = mass; }

    public String getWater() {        return Water; }

    public void setWater(String water) {        Water = water; }

    public String getCos() {        return Cos; }

    public void setCos(String cos) {        Cos = cos; }

    public String getTrees() {        return Trees; }

    public void setTrees(String trees) {        Trees = trees; }
}
//done DE KSERW AN TO XREIAZOMAI AKOMA