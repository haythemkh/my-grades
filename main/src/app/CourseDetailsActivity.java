package haythem.com.mygrades.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import haythem.com.mygrades.R;
import haythem.com.mygrades.models.CourseModel;
import haythem.com.mygrades.models.ModuleModel;

/**
 * Created by Haythem Khiri on 22/04/2016.
 */
public class CourseDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        setTitle(R.string.title_course_details);
        context = this;
        Bundle extras = getIntent().getExtras();
        final String moduleName = extras.getString("moduleName");
        final String courseName = extras.getString("courseName");
        ModuleModel module = CoursesListActivity.modules_hashed.get(moduleName);
        //Log.e("EEEF", CoursesListActivity.modules_hashed.get(moduleName).getCourses().size()+"");
        if(module != null)
            for(CourseModel course : module.getCourses()){
                if(course.getName().equals(courseName)){
                    TextView tModuleTitle = (TextView) findViewById(R.id.module_title);
                    tModuleTitle.setText(moduleName);
                    TextView tCourseTitle = (TextView) findViewById(R.id.course_title);
                    tCourseTitle.setText(courseName);
                    TextView tDS = (TextView) findViewById(R.id.ds_value);
                    tDS.setText(course.getDs());
                    TextView tTP = (TextView) findViewById(R.id.tp_value);
                    tDS.setText(course.getTp());
                    TextView tExam = (TextView) findViewById(R.id.exam_value);
                    tDS.setText(course.getExam());
                    TextView tCoeff = (TextView) findViewById(R.id.coefficient_value);
                    tDS.setText(course.getCoeff());
                    TextView tAverage = (TextView) findViewById(R.id.average_value);
                    tDS.setText(course.getExam());
                    TextView tDescription = (TextView) findViewById(R.id.description);
                    tDescription.setText(course.getDescription());
                }
            }

        Button btnReclamation =  (Button) findViewById(R.id.reclamation);
        btnReclamation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Claim has been successfully sent !", Toast.LENGTH_LONG).show();
                Firebase.setAndroidContext(getApplication());
                final SharedPreferences rdr = getApplication().getSharedPreferences("User", getApplication().MODE_PRIVATE);
                Firebase mRecRef = new Firebase(App.FIREBASE_STUDENTS_NODE + rdr.getString("_id", "no email found!") + "/universities/Higher Institute of Computer Science/modules/years/2/" + moduleName + "/" + courseName + "/");
                mRecRef.child("reclamation").setValue("true");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_my_grades:
                startActivity(new Intent(CourseDetailsActivity.this, CourseDetailsActivity.class));
                break;
            case R.id.nav_dashboard:
                break;
        }

        return true;
    }
}
