#!/usr/bin/env python3
def generate():
    result = ""
    n = 150000
    for i in range(2, n):
        result += "class A" + str(i) + "{}"
    print(result)

generate()
