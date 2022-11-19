package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
public class UnCheckedAtTest {

    @Test
    void unchecked_test() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Throwable.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
//            e.printStackTrace(); - 런타임 발생한 부분부터 클래스와 메소드 정보를 순차적으로 보여줘서 구조가 유출될 수 있음 (쓰지말라)
            log.info("ex = {}", e);
        }
    }

    static class Controller {
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        NetworkClient networkClient = new NetworkClient();
        Repository repository = new Repository();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("con_ex");
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL(); // 체크예외가 터지면, 잡음
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);

            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {

        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {

        public RuntimeSQLException() {

        }
        public RuntimeSQLException(Throwable cause) {
            super(cause); // cause 로 넘겨주면서 기존 예외를 같이 넘겨준다
        }
    }
}
