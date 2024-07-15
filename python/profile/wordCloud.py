import sys
import re
from konlpy.tag import Okt
from wordcloud import WordCloud
from sklearn.feature_extraction.text import TfidfVectorizer
#from collections import Counter
#import matplotlib.pyplot as plt

def getWordCloud(diaryFile, fileName):
    diaryContents = open(diaryFile, encoding='utf-8').read()

    # 분석할 데이터 추출
    text = ''
    for content in diaryContents:  
        text = text + re.sub(r'[^\w\n]', ' ', content) + ''

    # 품사 태깅 : 명사추출
    okt = Okt()
    messageN = okt.nouns(text)
    
    # 단어 빈도 탐색
    stopwords = list({
            '오늘', '아주', '조금', '매우', '다음', '그때', '너무', '같이', '정말', '모두', '하루', '처음', '시간', '기분', '우리'
        })

    # TF-IDF 벡터라이저
    vectorizer = TfidfVectorizer(stop_words=stopwords, max_df=0.85, min_df=2)
    X = vectorizer.fit_transform(messageN)
    tfidf_scores = dict(zip(vectorizer.get_feature_names_out(), X.sum(axis=0).tolist()[0]))
    
    '''
    count = Counter(messageN)
    
    wordCount = dict()
    for tag, counts in count.most_common(30):
        if(len(str(tag)) > 1):
            wordCount[str(tag)] = counts
    '''
    
    fontPath = 'C:/windows/fonts/hmkmmag.ttf'
    
    wc = WordCloud(fontPath, background_color='white', width=800, height=600, max_words=25)
    wordcloud = wc.generate_from_frequencies(tfidf_scores)
    #wordcloud = wc.generate_from_frequencies(wordCount)
    
    wordcloud.to_file('../python/profile/images/'+fileName)
    
    #plt.imshow(wordcloud, interpolation='bilinear')
    #plt.axis('off')
    #plt.show()

if __name__ == '__main__':
    print("process start")
    diaryFile = sys.argv[1]
    fileName = sys.argv[-1]
    getWordCloud(diaryFile, fileName)
    