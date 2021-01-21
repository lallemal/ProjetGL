#!/usr/bin/env python3
def generate():
    result = "class A{"
    for i in range(100):
        result += " void main" + str(i) + "(){}\n"

    result += "}"
    print(result)
generate()
