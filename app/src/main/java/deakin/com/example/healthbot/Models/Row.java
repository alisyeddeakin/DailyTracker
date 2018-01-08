package deakin.com.example.healthbot.Models;

public class Row {

    public int id;
    public int recordid;
    public String time;
    public String x_axis;
    public String y_axis;
    public String z_axis;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordid() {
        return recordid;
    }

    public void setRecordid(int recordid) {
        this.recordid = recordid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getX_axis() {
        return x_axis;
    }

    public void setX_axis(String x_axis) {
        this.x_axis = x_axis;
    }

    public String getY_axis() {
        return y_axis;
    }

    public void setY_axis(String y_axis) {
        this.y_axis = y_axis;
    }

    public String getZ_axis() {
        return z_axis;
    }

    public void setZ_axis(String z_axis) {
        this.z_axis = z_axis;
    }

    @Override
    public String toString() {
        return "Row{" +
                "id=" + id +
                ", recordid=" + recordid +
                ", time='" + time + '\'' +
                ", x_axis='" + x_axis + '\'' +
                ", y_axis='" + y_axis + '\'' +
                ", z_axis='" + z_axis + '\'' +
                '}';
    }
}
