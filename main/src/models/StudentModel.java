package haythem.com.mygrades.models;

/**
 * Created by Haythem Khiri on 18/04/2016.
 */
public class StudentModel {
    private String _id;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private String speciality;

    public StudentModel() {}

    public StudentModel(String email, String password, String firstname, String lastname, String speciality) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.speciality = speciality;
    }

    public StudentModel(String _id, String password, String email, String firstname, String lastname, String speciality) {
        this._id = _id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.speciality = speciality;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
