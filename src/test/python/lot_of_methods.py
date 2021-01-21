def generate():
    result = "class A{"
    for i in range(100):
        result += " void main" + str(i) + "(){}"
    result += "void main(){this"
    for i in range(100):
        result += ".main" + str(i) + "()"
    result += ";}}"
    print(result)
generate()
