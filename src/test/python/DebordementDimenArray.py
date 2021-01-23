#!/usr/bin/env python3

def generate():
    result = "{ int"
    for i in range(10000):
        result += "[]"
    result += " tab =  new int"
    for i in range(1,10001):
        result += "[" + str(i) + "]"
    result += ";}"
    print(result)
generate()
