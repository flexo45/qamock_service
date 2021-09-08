package org.qamock.app.main.api.json;

import org.qamock.app.main.domain.Role;

import java.io.Serializable;
import java.util.List;

public class UserObject implements Serializable {

    private static final long serialVersionUID = 8679873014359846558L;

    private String username;
    private String password;
    private String email;
    private List<Role> roles;
    private boolean enabled;

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public List<Role> getRoles(){return roles;}
    public void setRoles(List<Role> roles){this.roles = roles;}

    public boolean getEnabled (){return enabled;}
    public void setEnabled(boolean enabled){this.enabled = enabled;}

    @Override
    public String toString(){
        return "UserObject{username=" + username +
                ", password=******" +
                ", email=" + email +
                ", roles=" + roles +
                ", enabled=" + enabled +
                "}";
    }

}
