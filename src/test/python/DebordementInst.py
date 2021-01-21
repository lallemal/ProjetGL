#!/usr/bin/env python3
def generate():
    result = "{\nint x;\n"
    n = 150000
    result += " x = x  "
    for i in range(1, n):
        result += " + " + str(i)
    result += ";\n}"
    print(result)

generate()
