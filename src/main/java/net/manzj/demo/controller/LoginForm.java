package net.manzj.demo.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {

        private String userName;

        private String password;

        private String verCode;

}
