import random
import sys

test_number = int(sys.argv[1])
if test_number >= 5:
    test_number -= 4
    x = random.randint(2**(128 * (test_number - 1)) - 1, 2**(128*test_number) - 1)
    y = random.randint(2**(128 * (test_number - 1)) - 1, 2**(128*test_number) - 1)
elif test_number == 1:
    x = 1
    y = 2**200
elif test_number == 2:
    x = 0
    y = 2**211
elif test_number == 3:
    x = 4
    y = int('1' * 200)
elif test_number == 4:
    x = int('1' * 200)
    y = 5

with open('test.txt', 'w') as file:
    file.write(str(x * y))


print(str(x) + '\n' + str(y))
