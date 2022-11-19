package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

public class UnCheckedAtTest {

    @Test
    void unchecked_test() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Throwable.class);
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
            networkClient.call();
            repository.call();
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
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
