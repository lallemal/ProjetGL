#!/usr/bin/env python3
def generate():
    result = "{int x = 1; \n"
    for i in range(31):
        result += "x = x * 2;\n"
    result += "print(x);}"
    print(result)
generate()
