package com.scm.forms;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString // to print object datas
public class UserForm {
    @NotBlank(message="Username is required.")
    @Size(min = 3, message = "Minimum 3 characters are required.")
    private String name;
    @Email(message = "Invalid email address.")
    @NotBlank(message="Email is required.")
    private String email;
    @NotBlank(message = "Password is required.")
    @Size(min = 6, message = "Minimum 6 characters are required.")
    private String password;
    @NotBlank(message="About is required.")
    private String about;
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid Phone Number.")
    private String phoneNumber;


}
