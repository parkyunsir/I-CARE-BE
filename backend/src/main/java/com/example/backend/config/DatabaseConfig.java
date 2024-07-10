package com.example.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class DatabaseConfig implements ApplicationRunner {
    @Autowired
    DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            List<String> iconSql = List.of(
                    "INSERT INTO Icon(font, name, category) VALUES ('\uD83D\uDE00', '활짝 웃는 얼굴', 'A');",
                    "INSERT INTO Icon(font, name, category) VALUES ('\uD83E\uDD29', '반한 얼굴', 'A');",
                    "INSERT INTO Icon(font, name, category) VALUES ('\uD83D\uDE11', '무표정한 얼굴', 'A');"
            );
            for(String sql : iconSql) {
                statement.execute(sql);
            }
            List<String> topicSql = List.of(
                    "INSERT INTO Chat_Bot_Topic(topic) VALUES ('비가 많이 오는데 자녀가 나가서 놀고 싶다고 떼를 쓰는 상황');",
                    "INSERT INTO Chat_Bot_Topic(topic) VALUES ('자녀가 친구와 싸워서 신체적으로 다친 상황');",
                    "INSERT INTO Chat_Bot_Topic(topic) VALUES ('자녀가 열심히 노력했지만 받아쓰기 시험에서 낮은 성적을 받은 상황');",
                    "INSERT INTO Chat_Bot_Topic(topic) VALUES ('자녀가 숙제를 하도록 해야 하는 상황');",
                    "INSERT INTO Chat_Bot_Topic(topic) VALUES ('자녀가 정해진 핸드폰 사용 시간을 어기고 계속 보려는 상황');",
                    "INSERT INTO Chat_Bot_Topic(topic) VALUES ('빨래를 갤 때 자녀가 옆에 와서 스스로 도와주는데 빨래 개는 법을 몰라 빨래가 엉망인 상황');",
                    "INSERT INTO Chat_Bot_Topic(topic) VALUES ('자녀가 음식을 편식하는 상황');",
                    "INSERT INTO Chat_Bot_Topic(topic) VALUES ('자녀가 도와주려다가 식기를 깨뜨린 상황');"
            );
            for(String sql : topicSql) {
                statement.execute(sql);
            }
            statement.execute("ALTER TABLE Diary ALTER COLUMN content VARCHAR(500);");
        } catch(Exception e) {
            System.out.println("sql 오류 : " + e);
        }
    }
}
