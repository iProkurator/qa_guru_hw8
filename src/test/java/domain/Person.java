package domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Person {
    public String name;
    public String surname;
    public Integer age;
    @SerializedName("favorite_language")
    public List<String> favoriteLanguage;
    public Address address;

}
