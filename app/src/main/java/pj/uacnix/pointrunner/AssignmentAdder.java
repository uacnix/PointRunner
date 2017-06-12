package pj.uacnix.pointrunner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AssignmentAdder extends AppCompatActivity {

    private final int RESOLVE_CONNECTION_REQUEST_CODE = 1000;
    private final int PLACE_PICKER_REQUEST = 1001;
    private final int PLACE_END = 1;
    private final int PLACE_START = 0;
    protected GoogleApiClient mGoogleApiClient;
    private Place start;
    private Place end;
    private boolean startChosen,endChosen;
    private int PLACETYPE;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private EditText asgAdistance;
    private EditText asgApoints;
    private EditText asgAdescription;
    private EditText asgAname;
    private EditText asgApeople;
    private Button asgAAddbutton;
    private Button asgAbuttonEnd;
    private Button asgAbuttonStart;
    private EditText asgplaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_assignmentadder);
        ScrollView assignmentForm = (ScrollView) findViewById(R.id.assignmentForm);
        LinearLayout assignmentAdder = (LinearLayout) findViewById(R.id.assignmentAdder);
        asgplaceholder = (EditText) findViewById(R.id.asg_placeholder);
        asgAAddbutton = (Button) findViewById(R.id.asgA_Addbutton);
        asgAdistance = (EditText) findViewById(R.id.asgA_distance);
        asgApeople = (EditText) findViewById(R.id.asgA_people);
        asgAbuttonEnd = (Button) findViewById(R.id.asgA_buttonEnd);
        asgAbuttonStart = (Button) findViewById(R.id.asgA_buttonStart);
        asgApoints = (EditText) findViewById(R.id.asgA_points);
        asgAdescription = (EditText) findViewById(R.id.asgA_description);
        asgAname = (EditText) findViewById(R.id.asgA_name);
        TextView asgAlabel = (TextView) findViewById(R.id.asgA_label);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        asgAAddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlace();
            }
        });
        asgAbuttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLACETYPE = PLACE_END;
                pickPlace();
            }
        });
        asgAbuttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLACETYPE = PLACE_START;
                pickPlace();
            }
        });
    }

    private void addPlace() {
        Assignment asg;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            if (startChosen && endChosen)
                asg = new Assignment(getText(asgAname), getText(asgAdescription), start.getLatLng().latitude, start.getLatLng().longitude, end.getLatLng().latitude, end.getLatLng().longitude, getInt(asgApoints), getInt(asgApeople), getInt(asgAdistance), user);
            else {
                Toast.makeText(AssignmentAdder.this, "Either start or end not selected", Toast.LENGTH_LONG).show();
                return;
            }
            if(endChosen) {
                asgAbuttonEnd.setText("Lat:"+start.getLatLng().latitude+" Lng:"+start.getLatLng().longitude);
            }
            if(startChosen) {
                asgAbuttonStart.setText("Lat:" + end.getLatLng().latitude + " Lng:" + end.getLatLng().longitude);
            }
            String key = db.child("assignments").push().getKey();
            Map<String,Object> asgnVals = asg.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/assignments/" + key, asgnVals);
            childUpdates.put("/user-assignments/" + user.getUid() + "/" + key, asgnVals);
            db.updateChildren(childUpdates);
            Toast.makeText(this,"Assignment added",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void pickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private String getText(EditText input){
        return input.getText().toString();
    }
    private int getInt(EditText input){
        return Integer.parseInt(input.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if(resultCode == RESULT_OK)
                    mGoogleApiClient.connect();
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    if(PLACETYPE == PLACE_START) {
                        start = PlacePicker.getPlace(this, data);
                        startChosen = true;
                    }
                    if(PLACETYPE == PLACE_END){
                        end = PlacePicker.getPlace(this,data);
                        endChosen = true;
                    }
                }
                break;
        }
    }
}
