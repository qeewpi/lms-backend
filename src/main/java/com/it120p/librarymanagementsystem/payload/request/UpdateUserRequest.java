package com.it120p.librarymanagementsystem.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    @Size(max = 50)
    @Email
    private String email;

    @Size(max = 20)
    private String username;

    @Size(max = 50)
    private String name;

    private String password;

    // getters and setters
}
