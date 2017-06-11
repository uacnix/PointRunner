package pj.uacnix.pointrunner;

/**
 * Created by Rafał on 2017-06-10.
 */

public class Assignment {
    private String name,descr;
    private double Slat,Slng,Elat,Elng;
    private int points,people,dist;

    public Assignment() {
    }

    public Assignment(String name, String descr, double slat, double slng, double elat, double elng, int points, int people, int dist) {
        this.name = name;
        this.descr = descr;
        Slat = slat;
        Slng = slng;
        Elat = elat;
        Elng = elng;
        this.points = points;
        this.people = people;
        this.dist = dist;
    }
}
