import re
from konlpy.tag import Okt
from collections import Counter
from wordcloud import WordCloud

List<DiaryEntity> diaryList = diaryRepository.findAll();

# 분석할 데이터 추출
message = ''
for diary in diaryList:
	message = message + re.sub(r'[^\w]', ' ', diary.getContent()) + ''

# 품사 태깅 : 명사추출
nlp = Okt()
message_N = nlp.nouns(message)

# 단어 빈도 탐색
count = Counter(message_N)

word_count = dict()
for tag, counts in count.most_common(30):
	if(len(str(tag))>1):
		word_count[tag] = counts

font_path = 'C:/windows/fonts/malgun.ttf'

wc = WordCloud(font_path, background_color='ivory', width=800, height=600)
cloud = wc.generate_from_frequencies(word_count)

cloud.to_file(inputFileName + '_cloud.jpg')
