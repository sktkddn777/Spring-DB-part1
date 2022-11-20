package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 체크 예외를 런타임 예외로 변경
 * MemberRepository 인터페이스 사용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV4_1 implements MemberRepository{

    private final DataSource dataSource;

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values (?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw new MyDbException(e);
        }
        finally {
            close(con, pstmt, null); // 외부 TCP IP 를 통한 연결 리소스를 사용하고 있으므로 close 해야함
        }
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId = " + memberId);
            }
        } catch (SQLException e) {
            throw new MyDbException(e);
        }
        finally {
            close(con, pstmt, rs); // 외부 TCP IP 를 통한 연결 리소스를 사용하고 있으므로 close 해야함
        }
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("update result = " + resultSize);
        } catch (SQLException e) {
            throw new MyDbException(e);
        }
        finally {
            close(con, pstmt, null); // 외부 TCP IP 를 통한 연결 리소스를 사용하고 있으므로 close 해야함
        }
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("update result = " + resultSize);
        } catch (SQLException e) {
            throw new MyDbException(e);
        }
        finally {
            close(con, pstmt, null); // 외부 TCP IP 를 통한 연결 리소스를 사용하고 있으므로 close 해야함
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);

        // 트랜잭션 동기화를 사용하려면 DataSourceUtils 를 사용해야 함
        DataSourceUtils.releaseConnection(con, dataSource);
    }
    private Connection getConnection() {
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("con: {}, con.class: {}", con, con.getClass());

        return con;
    }
}
