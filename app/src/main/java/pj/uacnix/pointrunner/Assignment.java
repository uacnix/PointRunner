package pj.uacnix.pointrunner;

import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rafał on 2017-06-10.
 */

public class Assignment {
    private String name,descr;
    private double Slat,Slng,Elat,Elng;
    private int points,people,dist;
    private FirebaseUser creator;

    public Assignment() {
    }

    public Assignment(String name, String descr, double slat, double slng, double elat, double elng, int points, int people, int dist,FirebaseUser createdBy) {
        this.name = name;
        this.descr = descr;
        Slat = slat;
        Slng = slng;
        Elat = elat;
        Elng = elng;
        this.points = points;
        this.people = people;
        this.dist = dist;
        this.creator = createdBy;
    }
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("uid",creator.getUid());
        result.put("name",name);
        result.put("desc",descr);
        result.put("slat",Slat);
        result.put("slng",Slng);
        result.put("elat",Elat);
        result.put("elng",Elng);
        result.put("points",points);
        result.put("people",people);
        result.put("dist",dist);
        result.put("author",creator.getDisplayName());
        return result;
    }
}
