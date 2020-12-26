package com.example.beamin.data;

public class RankingData {
    private String restname;
    private int restnumber;

    public String getRestname() {
        return restname;
    }

    public void setRestname(String restname) {
        this.restname = restname;
    }

    public int getRestnumber() {
        return restnumber;
    }

    public void setRestnumber(int restnumber) {
        this.restnumber = restnumber;
    }

    public RankingData(String restname, int restnumber) {
        this.restname = restname;
        this.restnumber = restnumber;
    }
}
