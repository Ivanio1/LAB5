package data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class Person {
    private String name;
    public Date birthday;
    private Color eyeColor;
    private Country nationality;

    public Person(String name, Date birth, Color eyeColor, Country nationality) {

        this.birthday = birth;
        this.eyeColor = eyeColor;
        this.name = name;
        this.nationality = nationality;
    }

    public Person(String name, Color eyeColor, Country nationality) {
        this.eyeColor = eyeColor;
        this.name = name;
        this.nationality = nationality;
    }

    public Person(String name, Color eyeColor) {
       // this.birthday = birthday;
        this.eyeColor = eyeColor;
        this.name = name;
    }

    public Person(String name, Color color, Date birth) {
        this.birthday = birth;
        this.eyeColor = color;
        this.name = name;
    }


    /**
     * @return Color of the eyes.
     */
    public Color getEyeColor() {
        return eyeColor;
    }

    /**
     * @return Nationality of the person.
     */
    public Country getNationality() {
        return nationality;
    }

    /**
     * @return Name of the person.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String s=null;
        if(nationality!=null && birthday!=null){
            s="Person{" +
                    "name='" + name + '\'' +
                    ", eyeColor=" + eyeColor +
                    ", nationality="+nationality+
                    ", birthday="+birthday+
                    '}';
        }
        if(nationality==null && birthday!=null){
            s="Person{" +
                    "name='" + name + '\'' +
                    ", eyeColor=" + eyeColor +
                    ", birthday="+birthday+
                    '}';
        }if(nationality!=null && birthday==null){
            s="Person{" +
                    "name='" + name + '\'' +
                    ", eyeColor=" + eyeColor +
                    ", nationality="+nationality+
                    '}';
        }if(nationality==null && birthday==null){
            s="Person{" +
                    "name='" + name + '\'' +
                    ", eyeColor=" + eyeColor +
                    '}';
        }
        return s;
    }
}
