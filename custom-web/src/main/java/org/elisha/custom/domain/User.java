package org.elisha.custom.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Min(0)
    private Long id = 0L;

    @Column(name = "name")
    @NotNull
    private String name;


    @Column(name = "password")
    @Length(min = 6 ,max = 32)
    private String password;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "phone_number")
    @Pattern( regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$" , message = "手机号码格式错误")
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, email, phoneNumber);
    }
}
