ISU = 467213
EYES = ("8", ";", "X", ":", "=", "[")
NOSE = ("-", "<", "-{", "<{")
MOUTH = ("(", ")", "P", "|", "\\", "/", "O", "=")

result = EYES[ISU % 6] + NOSE[ISU % 4] + MOUTH[ISU % 8]

print(f"Ваш смайлик: {result}")