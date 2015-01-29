package vn.seasoft.sachcuatui.model;

/**
 * User: XuanTrung
 * Date: 10/21/2014
 * Time: 3:42 PM
 */
public class User {
    int idUser;
    String avatar;
    String displayName;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
