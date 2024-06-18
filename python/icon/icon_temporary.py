import sys
import re
from openkoreantext import OpenKoreanTextProcessor # faster
import pandas as pd
import numpy as np

def getIcons(diaryFile):
    diaryContents = open(diaryFile, encoding='utf-8').read()

    # 분석할 데이터 추출
    message = ''
    for content in diaryContents:  
        message = message + re.sub(r'[^\w]', ' ', content) + ''
    
    textTokens=OpenKoreanTextProcessor().tokenize(message)
    messageN = [token.text for token in textTokens if token.pos == 'Noun']
    
    icon = pd.read_csv('icon.csv')
    
    indexIcon = []
    for (index, iconName) in icon:
        for mes in messageN:
            if iconName == mes:
                indexIcon.append(index)
    
    return np.random.choice(indexIcon, 3, replace=False)

if __name__ == '__main__':
    print("process start")
    diaryFile = sys.argv[1]
    print(getIcons(diaryFile))