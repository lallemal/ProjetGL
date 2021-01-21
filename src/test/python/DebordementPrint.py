#!/usr/bin/env python3
def generate():
    result = "{ \n"
    n = 50000
    for i in range(n):
        result += "println(\"Hello World\");"
    result += "}"
    print(result)

generate()
