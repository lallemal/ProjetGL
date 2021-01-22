#!/usr/bin/env python3
def generate():
    result = "class A{}"
    for i in range(5000):
        result += " class A" + str(i) + "{}"
    print(result)
generate()
