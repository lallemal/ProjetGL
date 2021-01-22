#!/usr/bin/env python3
def generate():
    result = "class A{"
    result += "void main( int field "
    for i in range(100):
        result += ",int field" + str(i)
    result += "){}}"
    print(result)
generate()
