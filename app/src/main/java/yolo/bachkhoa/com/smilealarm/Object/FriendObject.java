package yolo.bachkhoa.com.smilealarm.Object;

/**
 * Created by Son Nguyen Vu 07 on 07-Jan-17.
 */
public class FriendObject {
    public FriendObject(String name, String id, String email) {
        this.name = name;
        this.id = id;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
}
