package data;

import java.util.Date;


public class LabWork  implements Comparable<LabWork> {
    private Integer id;
    private String unique_id;
    private String name;
    private Coordinates coordinates;
    private java.util.Date creationDate;
    private String date;
    private Double minimalPoint;
    private Difficulty difficulty;
    private Person author;

    public LabWork(String name, Coordinates coordinates, Double minimalPoint, Difficulty difficulty, Person author) {
        this.author = author;
        this.difficulty = difficulty;
        this.coordinates = coordinates;
        this.name = name;
        this.minimalPoint = minimalPoint;
        this.id=Integer.valueOf(unique_id);
    }
    public LabWork(String name, Coordinates coordinates, String date,Double minimalPoint, Difficulty difficulty, Person author) {
        this.author = author;
        this.difficulty = difficulty;
        this.coordinates = coordinates;
        this.date=date;
        this.name = name;
        this.minimalPoint = minimalPoint;
        this.id=Integer.valueOf(unique_id);
    }
    public LabWork(String name, Coordinates coordinates, Double minimalPoint, Person author) {
        this.author = author;
        this.coordinates = coordinates;
        this.name = name;
        this.minimalPoint = minimalPoint;

    }
    public LabWork(int id,String name, Coordinates coordinates, Date creationDate, Double minimalPoint, Person author) {
        this.author = author;
        this.id=id;
        this.creationDate=creationDate;
        this.coordinates = coordinates;
        this.name = name;
        this.minimalPoint = minimalPoint;

    }

    public LabWork(int id, String name, Coordinates coordinates, Date creationDate,Double minimalPoint,Difficulty difficulty, Person author) {
        this.author = author;
        this.id=id;
        this.creationDate=creationDate;
        this.coordinates = coordinates;
        this.name = name;
        this.difficulty=difficulty;
        this.minimalPoint = minimalPoint;
    }


    /**
     * @return minimal point.
     */
    public Double getMinimalPoint() {
        return minimalPoint;
    }
    /**
     * @return Integer id.
     */
    public Integer getId() {
        return id;
    }
    /**
     * @return String id.
     */
    public String getUnique_id() {
        return unique_id;
    }

    /**
     * set id.
     */
    public void setUnique_id(String id) {
        this.unique_id = id;
    }
    /**
     * @return author.
     */
    public Person getAuthor() {
        return author;
    }
    /**
     * @return difficulty.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return дату создания
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @return дату создания в формате строки
     */
    public String getDate() {
        return date;
    }

    /**
     * Устанавливает id
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Устанавливает Date
     * @param creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    @Override
    public String toString() {
        String S=null;
        if(difficulty!=null){
            S="LabWork {" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", coordinates=" + coordinates.toString()+
                    ", creationDate=" + creationDate +
                    ", minimalPoint=" + minimalPoint+
                    ", difficulty='" + difficulty + '\'' +
                    ", owner=" + author.toString() +
                    '}';
        }if(difficulty==null){
            S="LabWork {" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", coordinates=" + coordinates.toString() +
                    ", creationDate=" + creationDate +
                    ", minimalPoint=" + minimalPoint+
                    ", owner=" + author.toString() +
                    '}';
        }
        return S;
    }
    @Override
    public int compareTo(LabWork o) {

        return 0;
    }
}
