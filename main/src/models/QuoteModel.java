package haythem.com.mygrades.models;

/**
 * Created by haythem on 22/04/2016.
 */
public class QuoteModel {
    private String _id;
    private String author;
    private String msg;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
