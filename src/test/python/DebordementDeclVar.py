#!/usr/bin/env python3
def generate():
    result = "{int x1"
    n = 50000
    for i in range(2, n):
        result += ", x" + str(i)
    result += ";}"
    print(result)

generate()
