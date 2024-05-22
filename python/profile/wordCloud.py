import sys
import re
#from konlpy.tag import Okt
from openkoreantext import OpenKoreanTextProcessor # faster
from collections import Counter
from wordcloud import WordCloud

def getWordCloud(diaryFile, fileName):
    diaryContents = open(diaryFile, encoding='utf-8').read()

    # 분석할 데이터 추출
    message = ''
    for content in diaryContents:  
        message = message + re.sub(r'[^\w]', ' ', content) + ''

    # 품사 태깅 : 명사추출
    #nlp = Okt()
    #messageN = nlp.nouns(message)
    
    textTokens=OpenKoreanTextProcessor().tokenize(message)
    messageN = [token.text for token in textTokens if token.pos == 'Noun']
    
    # 단어 빈도 탐색
    count = Counter(messageN)
    
    wordCount = dict()
    for tag, counts in count.most_common(30):
        if(len(str(tag)) > 1):
            wordCount[str(tag)] = counts

    fontPath = 'C:/windows/fonts/hmkmmag.ttf'
    
    wc = WordCloud(fontPath, background_color='white', width=800, height=600)
    cloud = wc.generate_from_frequencies(wordCount)
    
    cloud.to_file('../python/profile/cloud_images/'+fileName)

if __name__ == '__main__':
    print("process start")
    diaryFile = sys.argv[1]
    fileName = sys.argv[-1]
    getWordCloud(diaryFile, fileName)
    