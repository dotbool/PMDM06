package martinezruiz.javier.pmdm06.models;

public class ControlPointProgress {


    public ControlPointProgress(GimActivity gimActivity) {
        this.gimActivity = gimActivity;
    }

    public GimActivity getGimActivity() {
        return gimActivity;
    }

    public void setGimActivity(GimActivity gimActivity) {
        this.gimActivity = gimActivity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    private long id;
    private GimActivity gimActivity;
    private long userId;
    private int progress;
}
