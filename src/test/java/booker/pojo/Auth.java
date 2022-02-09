package booker.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Auth {

    private String username;
    private String password;

    public Auth() {
        this.username = "admin";
        this.password = "password123";
    }

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "username: " + this.username + "; password: " + this.password;
    }
}
