import json
import re

stt_txt = 'stt.txt'
learning_data = 'learningData.jsonl'

with open(stt_txt, 'r', encoding='utf-8') as file:
    stt = file.readlines()

sentences = []

for s in stt[:500]:
    sentence = re.sub(r'[a-zA-Z():\n]+', '', s)
    if sentence:
        sentences.append(sentence)

messages = []
system_message = {"role": "system", "content": "너는 장난기 많고 반말을 하는 7살 아이처럼 말해."}
#user_message = {"role": "user", "content": ""}

for s in sentences:
    if s.strip():
        message = {
            "messages": [
                system_message,
                {"role": "assistant", "content": s}
            ]
        }
        messages.append(message)
        
with open(learning_data, 'w', encoding='utf-8') as file:
    for message in messages:
        json.dump(message, file, ensure_ascii=False)
        file.write('\n')
    
print("JSONL 파일 생성 완료")