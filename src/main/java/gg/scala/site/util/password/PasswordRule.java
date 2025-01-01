package gg.scala.site.util.password;

import gg.scala.site.util.password.result.PasswordResult;

public interface PasswordRule {

    PasswordResult validate(String password);

}