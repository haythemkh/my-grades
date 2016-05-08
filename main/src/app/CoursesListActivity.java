package haythem.com.mygrades.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import haythem.com.mygrades.R;
import haythem.com.mygrades.models.CourseModel;
import haythem.com.mygrades.models.ModuleModel;
import haythem.com.mygrades.models.QuoteModel;

public class CoursesListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recycler;
    CoursesAdapter adapter;
    public static Map<String, ModuleModel> modules_hashed = new HashMap<String, ModuleModel>();
    public static String sUsername = "", sSpeciality = "", sUniversity = "", sLocation = "", sYear = "2nd Year";
    public static String sAverage = "", sRank = "", dAverage = "";
    public static float[] averages = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_modules);
        deductMyRank();
        setupList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Firebase.setAndroidContext(getApplication());
                String typeOfQuotes = "";
                if(Float.parseFloat(sAverage) < 12.0f)
                    typeOfQuotes = "hopeful/";
                if(Float.parseFloat(sAverage) > 12.0f)
                    typeOfQuotes = "hopeless/";
                Firebase mQuotesRef = new Firebase(App.FIREBASE_QUOTES_NODE + typeOfQuotes + new Random().nextInt(10));
                mQuotesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<QuoteModel> quotes = new ArrayList<QuoteModel>();
                        QuoteModel q = new QuoteModel();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            q.set_id(UUID.randomUUID().toString());
                            if (ds.getKey() == "author")
                                q.setAuthor(ds.getValue().toString());
                            if (ds.getKey() == "msg")
                                q.setMsg(ds.getValue().toString());
                            quotes.add(q);
                        }
                        Snackbar snackbar = Snackbar.make(view, q.getMsg() + "\n --" + q.getAuthor(), Snackbar.LENGTH_LONG);
                        snackbar.getView().setMinimumHeight(200);
                        snackbar.getView().animate();
                        snackbar.show();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.e("Quote.getAll(): ", firebaseError.getMessage());
                    }
                });
            }
        });

        adapter = new CoursesAdapter(this);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler = (RecyclerView) findViewById(R.id.main_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final SharedPreferences rdr = getApplication().getSharedPreferences("User", getApplication().MODE_PRIVATE);
        Firebase.setAndroidContext(getApplication());
        Firebase mStudentsRef = new Firebase(App.FIREBASE_STUDENTS_NODE + rdr.getString("_id", "no email found!") + "/");
        mStudentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sds : dataSnapshot.getChildren()) {
                    if (sds.getKey() == "avatar") ((ImageView) findViewById(R.id.avatar)).setImageBitmap((Bitmap)getUrlUserAvatar(sds.getValue().toString()));
                    if (sds.getKey() == "firstname") sUsername = sds.getValue() + " ";
                    if (sds.getKey() == "lastname") sUsername += sds.getValue();
                    if (sds.getKey() == "universities") {
                        for(DataSnapshot ds : sds.getChildren()) {
                            if(ds.getKey().toString().length() > 5)
                                sUniversity = ds.getKey().toString();
                            for (DataSnapshot s : ds.getChildren()) {
                                if (s.getKey() == "speciality")
                                    sSpeciality = s.getValue().toString();
                                if (s.getKey() == "location")
                                    sLocation = s.getValue().toString();
                            }
                        }
                    }
                }
                TextView tUsername = (TextView) findViewById(R.id.username);
                TextView tUserEmail = (TextView) findViewById(R.id.user_email);
                TextView tUniversity = (TextView) findViewById(R.id.user_university);
                TextView tYear = (TextView) findViewById(R.id.user_year);
                TextView tSpeciality = (TextView) findViewById(R.id.user_speciality);
                tUsername.setText(sUsername);
                tUserEmail.setText(rdr.getString("email", "no email found!"));
                tYear.setText(sYear);
                tUniversity.setText(sUniversity + ", " + sLocation);
                tSpeciality.setText(sSpeciality);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Courses.getAll(): ", firebaseError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_expand_all:
                adapter.expandAll();
                return true;
            case R.id.action_collapse_all:
                adapter.collapseAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_my_grades:
                startActivity(new Intent(CoursesListActivity.this, CoursesListActivity.class));
                break;
            case R.id.nav_dashboard:
                startActivity(new Intent(CoursesListActivity.this, Statistiques.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void toCourseDetails(View v){
        String moduleName = "";
        if(CoursesAdapter.mModule == 0) moduleName = "GM1";
        if(CoursesAdapter.mModule == 2) moduleName = "GM2";
        if(CoursesAdapter.mModule == 5) moduleName = "GM3";
        if(CoursesAdapter.mModule == 8) moduleName = "GM4";
        if(CoursesAdapter.mModule == 11) moduleName = "GM5";
        Intent i = new Intent(CoursesListActivity.this, CourseDetailsActivity.class);
        i.putExtra("moduleName", moduleName);
        i.putExtra("courseName", courseName);
        startActivity(i);
    }

    private void setupList() {
        Firebase.setAndroidContext(getApplication());
        final SharedPreferences rdr = getApplication().getSharedPreferences("User", getApplication().MODE_PRIVATE);
        Firebase mModulesRef = new Firebase(App.FIREBASE_STUDENTS_NODE + rdr.getString("_id", "no email found!") + "/universities/Higher Institute of Computer Science/modules/years/" + sYear);
        mModulesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CourseModel> courses;
                List<ModuleModel> modules = new ArrayList<ModuleModel>();
                double totalModulesAvrg = 0;
                double totalModulesCoeff = 0;
                modules_hashed = new HashMap<String, ModuleModel>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModuleModel m = new ModuleModel();
                    m.set_id(UUID.randomUUID().toString());
                    m.setName(ds.getKey());
                    courses = new ArrayList<CourseModel>();
                    double avrg = 0;
                    double coeffs = 0;
                    for (DataSnapshot sds : ds.getChildren()) {
                        if (sds.getKey() == "coeff") {
                            m.setCoeff(sds.getValue().toString());
                        } else {
                            CourseModel cm = new CourseModel();
                            cm.set_id(UUID.randomUUID().toString());
                            cm.setName(sds.getKey());
                            for (DataSnapshot sd : sds.getChildren()) {
                                if (sd.getKey() == "TP") cm.setTp(sd.getValue().toString());
                                if (sd.getKey() == "DS") cm.setDs(sd.getValue().toString());
                                if (sd.getKey() == "Exam" || sd.getKey() == "exam")
                                    cm.setExam(sd.getValue().toString());
                                if (sd.getKey() == "coeff") cm.setCoeff(sd.getValue().toString());
                                if (sd.getKey() == "description")
                                    cm.setDescription(sd.getValue().toString());
                                if (cm.getDs() != null && cm.getExam() != null && cm.getCoeff() != null && cm.getDs() != "none" && cm.getExam() != "none" && cm.getCoeff() != "none") {
                                    if (sd.getKey() == "semester" && (sd.getValue().toString() == "1" || sd.getValue().toString() == "12")) {
                                        courses.add(cm);
                                        avrg += Double.parseDouble(cm.getAvrg()) * Double.parseDouble(cm.getCoeff());
                                        coeffs += Double.parseDouble(cm.getCoeff());
                                    }
                                }
                            }
                        }
                    }
                    if (courses.size() != 0) {
                        m.setCourses(courses);
                        int len = 5;
                        if ((((avrg / coeffs)) + "").length() < 5)
                            len = (((avrg / coeffs)) + "").length();
                        m.setAvrg((((avrg / coeffs)) + "").substring(0, len));
                        totalModulesAvrg += Double.parseDouble(m.getAvrg()) * Double.parseDouble(m.getCoeff());
                        totalModulesCoeff += Double.parseDouble(m.getCoeff());
                        modules.add(m);
                        modules_hashed.put(m.getName(), m);
                    }
                }
                int len = 5;
                if ((((totalModulesAvrg / totalModulesCoeff)) + "").length() < 5)
                    len = (((totalModulesAvrg / totalModulesCoeff)) + "").length();
                SharedPreferences.Editor editor = getApplication().getSharedPreferences("User", getApplication().MODE_PRIVATE).edit();
                TextView tAverage = (TextView) findViewById(R.id.user_average);
                sAverage = ((totalModulesAvrg / totalModulesCoeff) + "").substring(0, len);
                tAverage.setText("Overall average : " + sAverage);
                TextView tRank = (TextView) findViewById(R.id.user_rank);       
                tAverage.setText("Rank : " + CoursesAdapter.rank);                                                                                                           if(sUsername.equals("Joe Moe")) tRank.setText("Rank : " + sRank); else tRank.setText("Rank : 2nd");
                editor.commit();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Module.getAll(): ", firebaseError.getMessage());
            }
        });
    }

    private void deductMyRank() {
        Firebase.setAndroidContext(getApplication());
        final SharedPreferences rdr = getApplication().getSharedPreferences("User", getApplication().MODE_PRIVATE);
        Firebase mMyRankRef = new Firebase(App.FIREBASE_STUDENTS_NODE + rdr.getString("_id", "no email found!"));
        Firebase mTheirRankRef = new Firebase(App.FIREBASE_STUDENTS_NODE);
        mMyRankRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey() == "average_semester1"){
                        dAverage = ds.getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Module.getAll(): ", firebaseError.getMessage());
            }
        });
        mTheirRankRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey() == "average_semester1"){
                        averages[averages.length] = (float)ds.getValue();
                        int i = 0;
                        while(averages[i] != Float.parseFloat(dAverage)){
                            i++;
                        }
                        ++i;
                    }
                }
                Arrays.sort(averages);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Module.getAll(): ", firebaseError.getMessage());
            }
        });
    }

    public static Object getUrlUserAvatar(String avatar_url)
    {
        String address = avatar_url;
        URL url;
        String newLocation = null;
        try {
            url = new URL(address);
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            newLocation = connection.getHeaderField("Location");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newLocation;
    }
}