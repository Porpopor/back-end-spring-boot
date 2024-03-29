package BackEnd.BackEnd.exception;

public class UsersException extends BaseException {
  public UsersException(String code) {
    super(code);
  }

  public static UsersException userAlreadyExists() {

    return new UsersException("user.already.exists");

  }
  public static UsersException loginFail() {

    return new UsersException("loginFail");

  }

  public static UsersException emailAlreadyExists() {

    return new UsersException("email.already.exists");

  }
  public static UsersException passwordNotmatch() {

    return new UsersException("password.not.match");

  }

}
