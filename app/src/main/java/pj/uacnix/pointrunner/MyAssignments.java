package pj.uacnix.pointrunner;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAssignments extends ListFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "LIST";
    public ArrayList<HashMap<String,String>> lists = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_assignments, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("user-assignments").child(user.getUid());
        Log.e(TAG," EMAIL:"+user.getEmail()+" UID:"+user.getUid());
        //List<Map<String,String>> temp = new ArrayList<>();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "USER HAS "+dataSnapshot.getChildrenCount()+" ASSIGNMENTS");
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    HashMap<String,String> holder = new HashMap<>();
                    holder.put("name",(ds.child("name").getValue().toString()));
                    holder.put("desc",(ds.child("desc").getValue().toString()));
                    lists.add(holder);
                }
                setupList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void setupList(){
        AssignmentsAdapter adapter = new AssignmentsAdapter(lists);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
}