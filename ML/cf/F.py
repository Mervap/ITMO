import math


def next_int():
    return int(input())


k = next_int()
lambdas = list(map(int, input().split()))
alpha = next_int()
n = next_int()

messages_count = [0 for _ in range(k)]
word_count = [{} for _ in range(k)]
all_words = set()

for i in range(n):
    line = input().split()
    c, words = int(line[0]) - 1, line[2:]
    messages_count[c] += 1
    wc = word_count[c]
    for word in set(words):
        wc[word] = wc.get(word, 0) + 1
        all_words.add(word)

prob = [{} for _ in range(k)]
rev_prob = [{} for _ in range(k)]

for c in range(k):
    wc = word_count[c]
    div = messages_count[c] + 2 * alpha
    log_div = math.log(div)
    class_prob = prob[c]
    class_rev_prob = rev_prob[c]
    for word in all_words:
        class_prob[word] = math.log(wc.get(word, 0) + alpha) - log_div
        class_rev_prob[word] = math.log(div - wc.get(word, 0) - alpha) - log_div

m = int(input())
logn = math.log(n)
for i in range(m):
    words = set(input().split()[1:])
    anss = [None for _ in range(k)]
    for c in range(k):
        if messages_count[c] == 0:
            continue
        p_y = math.log(messages_count[c]) - logn
        anss[c] = math.log(lambdas[c]) + p_y
        for word in all_words:
            if word in words:
                anss[c] += prob[c].get(word, 0)
            else:
                anss[c] += rev_prob[c].get(word, 0)

    mx = max(filter(lambda x: x is not None, anss))
    anss = list(map(lambda x: math.exp(x - mx) if x is not None else 0.0, anss))
    s = sum(anss)
    anss = list(map(lambda x: x / s, anss))
    print(*anss, sep=" ")
