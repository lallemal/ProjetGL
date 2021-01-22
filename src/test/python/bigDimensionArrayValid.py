#!/usr/bin/env python3

def generate():
    result = "{ int"
    for i in range(100):
        result += "[]"
    result += " tab =  new int"
    for i in range(1,101):
        result += "[" + str(i) + "]"
    result += ";}"
    print(result)
generate()
