CREATE TABLE IF NOT EXISTS Icon (
    icon_id INT AUTO_INCREMENT PRIMARY KEY,
    font VARCHAR(255),
    name VARCHAR(255)
);

INSERT INTO Icon(font, name) VALUES ('😀', '활짝 웃는 얼굴');
INSERT INTO Icon(font, name) VALUES ('🤩', '반한 얼굴');
INSERT INTO Icon(font, name) VALUES ('😑', '무표정한 얼굴');


ALTER TABLE Diary
ALTER COLUMN content VARCHAR(1000);