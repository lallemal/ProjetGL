#!/usr/bin/env python3
def generate():
    result = "{int x = 9;"
    n = 1500
    for i in range(n):
        result += "if(true){ println(" + str(i) + ");"
    for i in range(n):
        result+= "}"
    result += "}"
    print(result)

generate()
