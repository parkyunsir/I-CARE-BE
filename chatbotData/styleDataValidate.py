import json

def validate_and_fix_jsonl(file_path):
    fixed_lines = []
    
    with open(file_path, 'r', encoding='utf-8') as file:
        lines = file.readlines()
    
    for i, line in enumerate(lines):
        try:
            # Attempt to parse the line as JSON
            json.loads(line)
            fixed_lines.append(line)  # If it's valid, keep the line as is
        except json.JSONDecodeError:
            print(f"Line {i+1} is not a valid JSON dictionary.")
            # Replace the invalid line with a valid JSON object
            fixed_line = json.dumps({"error": f"Invalid JSON on line {i+1}"})
            fixed_lines.append(fixed_line + '\n')
    
    # Save the potentially fixed lines back to the file
    with open(file_path, 'w', encoding='utf-8') as file:
        file.writelines(fixed_lines)

# Usage
file_path = 'feedbackData.jsonl'
validate_and_fix_jsonl(file_path)
