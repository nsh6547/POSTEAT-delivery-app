import json
a= '{"emotion":"ab"}'
b= json.loads(a)

print(json.dumps(b))
