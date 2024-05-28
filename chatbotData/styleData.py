import json
import re

stt_txt = 'stt.txt'
learning_data = 'learningData.jsonl'

sttFile = open(stt_txt, encoding='utf-8').read()

stt = sttFile.split('\n')

sentences = []

for s in stt:
    sentence = re.sub(r'[a-zA-Z()]+', '', s)
    if sentence:
        sentences.append(sentence)

messages = []
system_message = {"role": "system", "content": "너는 부모님과 함께 있는 7살 아이야."}
user_message = {"role": "user", "content": ""}

for s in sentences:
    if s.strip():
        message = {
            "message": [
                system_message,
                user_message,
                {"role": "assistant", "content": s}
            ]
        }
        messages.append(message)
        
dataFile = open(learning_data, 'w', encoding='utf-8')
for message in messages:
    json.dump(message, dataFile, ensure_ascii=False)
    dataFile.write('\n')
    
print("JSONL 파일 생성 완료")