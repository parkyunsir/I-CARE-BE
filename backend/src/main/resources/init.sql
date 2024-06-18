CREATE TABLE IF NOT EXISTS Icon (
    icon_id INT AUTO_INCREMENT PRIMARY KEY,
    font VARCHAR(50),
    name VARCHAR(50),
    category VARCHAR(20)
);

INSERT INTO Icon(font, name, category) VALUES ('😀', '활짝 웃는 얼굴', 'A');
INSERT INTO Icon(font, name, category) VALUES ('🤩', '반한 얼굴', 'A');
INSERT INTO Icon(font, name, category) VALUES ('😑', '무표정한 얼굴', 'A');


ALTER TABLE Diary
ALTER COLUMN content VARCHAR(1000);