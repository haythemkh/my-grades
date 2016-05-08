package haythem.com.mygrades.models;

import java.util.List;

/**
 * Created by haythem on 18/04/2016.
 */
public class ModuleModel {
    private String _id;
    private String name;
    private String coeff;
    private String avrg;
    private List<CourseModel> courses;

    public ModuleModel() {}

    public ModuleModel(String name, String coeff, List<CourseModel> courses, String avrg) {
        this.name = name;
        this.coeff = coeff;
        this.courses = courses;
        this.avrg = avrg;
    }

    public ModuleModel(String _id, String name, String coeff, List<CourseModel> courses, String avrg) {
        this._id = _id;
        this.name = name;
        this.coeff = coeff;
        this.courses = courses;
        this.avrg = avrg;
    }

    public String getAvrg() {
        return avrg;
    }

    public void setAvrg(String avrg) {
        this.avrg = avrg;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoeff() {
        return coeff;
    }

    public void setCoeff(String coeff) {
        this.coeff = coeff;
    }

    public List<CourseModel> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseModel> courses) {
        this.courses = courses;
    }
}
