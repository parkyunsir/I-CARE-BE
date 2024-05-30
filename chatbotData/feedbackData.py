import json
import re

stt_txt = 'feedback.txt'
learning_data = 'feedbackData.jsonl'

with open(stt_txt, 'r', encoding='utf-8') as file:
    stt = file.readlines()

sentencesP = []
sentencesC = []

for i in range(0, len(stt), 2) :
    s = stt[i]
    sentence = re.sub(r'[a-zA-Z():\n]+', '', s)
    if sentence:
        sentencesP.append(sentence)

for i in range(1, len(stt), 2) :
    s = stt[i]
    sentence = re.sub(r'[a-zA-Z():\n]+', '', s)
    if sentence:
        sentencesC.append(sentence)

messages = []
system_message = {"role": "system", "content": "너는 부모가 자녀에게 한 말을 받아서 부모에게 그 말에 대한 피드백을 주는 챗봇이야."}

for i in range(0, len(sentencesP), 1) :
    sp = sentencesP[i]
    sc = sentencesC[i]
    message = {
        "messages": [
            system_message,
            {"role": "user", "content": "'" + sp + "'. 부모가 자녀에게 이런 말을 했어. 이에 대한 피드백을 줘."},
            {"role": "assistant", "content": sc}
            ]
        }
    messages.append(message)
        
with open(learning_data, 'w', encoding='utf-8') as file:
    for message in messages:
        json.dump(message, file, ensure_ascii=False)
        file.write('\n')
    
print("JSONL 파일 생성 완료")