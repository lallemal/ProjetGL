def generate():
    result = "class A{}"
    for i in range(1000):
        result += " class A" + str(i) + "{}"
    print(result)
generate()
