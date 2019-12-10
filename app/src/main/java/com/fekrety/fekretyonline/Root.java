package com.fekrety.fekretyonline;
import com.fekrety.fekretyonline.Model.item;

import java.util.List;

public class Root {
    private List<com.fekrety.fekretyonline.Model.item> item;

    public void setArticles(List<item> item){
        this.item = item;
    }
    public List<item> getArticles(){
        return item;
    }
}