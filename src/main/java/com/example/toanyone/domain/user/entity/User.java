package com.example.toanyone.domain.user.entity;

import com.example.toanyone.domain.user.dto.UserRequestDto;
import com.example.toanyone.domain.user.enums.Gender;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.Period;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Table(name="users")
@NoArgsConstructor
@Where(clause = "deleted = false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String username;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    private Integer age;

    public User(String email, String password, String name, UserRole role, String nickname,
        String phone, String address, String gender, String birth) {
        this.email = email;
        this.password = password;
        this.username = name;
        this.userRole = role;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
        this.gender = Gender.of(gender);
        this.birth = LocalDate.parse(birth);
        this.age = calculateAge(birth);
    }

    public Integer calculateAge(String birth) {
        return Period.between(LocalDate.parse(birth), LocalDate.now()).getYears();
    }

    public void updateInfo(UserRequestDto.Update updateInfo) {

        if (updateInfo.getNickname() != null) {
            this.nickname = updateInfo.getNickname();
        }

        if (updateInfo.getAddress() != null) {
            this.address = updateInfo.getAddress();
        }

    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

}
