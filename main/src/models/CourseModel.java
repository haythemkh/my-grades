package haythem.com.mygrades.models;

import android.util.Log;

/**
 * Created by haythem on 18/04/2016.
 */
public class CourseModel {
    private String _id;
    private String moduleName;
    private String name;
    private String coeff;
    private String description;
    private String ds;
    private String tp;
    private String exam;
    private String avrg;

    public CourseModel() {}

    public CourseModel(String name, String coeff, String moduleName, String description, String ds, String tp, String exam) {
        this.name = name;
        this.coeff = coeff;
        this.moduleName = moduleName;
        this.description = description;
        this.ds = ds;
        this.tp = tp;
        this.exam = exam;
        this.avrgCalculate();
    }

    public CourseModel(String _id, String name, String coeff, String moduleName, String description, String ds, String tp, String exam) {
        this._id = _id;
        this.name = name;
        this.coeff = coeff;
        this.moduleName = moduleName;
        this.description = description;
        this.ds = ds;
        this.tp = tp;
        this.exam = exam;
        this.avrgCalculate();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
        this.avrgCalculate();
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
        this.avrgCalculate();
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
        this.avrgCalculate();
    }

    public void avrgCalculate(){
        float fAvrg = 0;
        if(this.ds != null && this.exam != null && !this.ds.equals("none") && !this.exam.equals("none")) {
            if (this.tp == null || this.tp.equals("none")) {
                fAvrg += Float.parseFloat(this.ds) * 0.3;
                fAvrg += Float.parseFloat(this.exam) * 0.7;
            } else {
                fAvrg += Float.parseFloat(this.ds) * 0.22;
                fAvrg += Float.parseFloat(this.tp) * 0.33;
                fAvrg += Float.parseFloat(this.exam) * 0.45;
            }
            int len = 5;
            if((fAvrg + "").length() < 5) len = (fAvrg + "").length();
            this.avrg = (fAvrg + "").substring(0, len);
        } else {
            this.avrg = "--";
        }
    }
}
