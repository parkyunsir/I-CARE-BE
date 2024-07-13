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
ALTER COLUMN content VARCHAR(500);

CREATE TABLE IF NOT EXISTS Input (
    input_Id INT AUTO_INCREMENT PRIMARY KEY,
    input VARCHAR(50) NOT NULL
);

INSERT INTO Input(input) VALUES ('제일 좋아하는 놀이가 뭐야?');
INSERT INTO Input(input) VALUES ('제일 좋아하는 음식이 뭐야?');
INSERT INTO Input(input) VALUES ('나중에 커서 뭐하고 싶어?');
INSERT INTO Input(input) VALUES ('제일 좋아하는 노래가 뭐야?');
INSERT INTO Input(input) VALUES ('제일 좋아하는 색이 뭐야?');
INSERT INTO Input(input) VALUES ('제일 친한 친구 이름이 뭐야?');
INSERT INTO Input(input) VALUES ('어디 놀러가고 싶어?');
INSERT INTO Input(input) VALUES ('제일 좋아하는 과목이 뭐야?');
INSERT INTO Input(input) VALUES ('제일 좋아하는 동물이 뭐야?');
INSERT INTO Input(input) VALUES ('제일 좋아하는 책이 뭐야?');
INSERT INTO Input(input) VALUES ('제일 좋아하는 캐릭터가 뭐야?');