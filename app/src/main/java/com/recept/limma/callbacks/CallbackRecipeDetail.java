package com.recept.limma.callbacks;

import com.recept.limma.models.Images;
import com.recept.limma.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class CallbackRecipeDetail {

    public String status = "";
    public Recipe post = null;
    public List<Images> images = new ArrayList<>();
    public List<Recipe> related = new ArrayList<>();

}