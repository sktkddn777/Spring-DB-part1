package hello.jdbc.connection;

// 상수를 모아두기 위해 사용하는 클래스이기에 객체 생성을 막기 위해 abstract 를 붙여줌
public abstract class ConnectionConst {
    public static final String URL = "jdbc:h2:tcp://localhost/~/dev/database/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
