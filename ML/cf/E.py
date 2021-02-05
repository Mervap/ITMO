import math
import random
from time import process_time

start = process_time()
n = int(input())

kernel = []
target = []
for i in range(n):
    obj = list(map(int, input().split()))
    kernel.append(obj[:-1])
    target.append(obj[-1])

C = int(input())

random.seed()
lambdas = [0 for i in range(n)]
b = 0

eps = 1e-15
err_cache = {}
non_bound = set()
non_bound_q = []


def predict_svm(ind: int) -> float:
    return sum([target[i] * lambdas[i] * kernel[i][ind] for i in range(n)]) - b


def get_cached_err(ind: int) -> float:
    if ind in err_cache:
        return err_cache[ind]
    else:
        err = predict_svm(ind) - target[ind]
        err_cache[ind] = err
        return err


def smo(fst_ind: int, snd_ind: int) -> bool:
    global b
    if fst_ind == snd_ind:
        return False
    l_fst = lambdas[fst_ind]
    l_snd = lambdas[snd_ind]
    y_fst = target[fst_ind]
    y_snd = target[snd_ind]
    if y_fst == y_snd:
        l_seg = max(0.0, l_fst + l_snd - C)
        h_seg = min(C, l_fst + l_snd)
    else:
        l_seg = max(0.0, l_snd - l_fst)
        h_seg = min(C, l_snd - l_fst + C)

    if math.fabs(l_seg - h_seg) < eps:
        return False

    k11 = kernel[fst_ind][fst_ind]
    k12 = kernel[fst_ind][snd_ind]
    k22 = kernel[snd_ind][snd_ind]
    eta = k11 + k22 - 2 * k12
    if eta > 1e-5:
        new_l_snd = l_snd + y_snd * (get_cached_err(fst_ind) - get_cached_err(snd_ind)) / eta
        if new_l_snd < l_seg:
            new_l_snd = l_seg
        elif new_l_snd > h_seg:
            new_l_snd = h_seg
    else:
        l_obj = 0
        h_obj = 0
        l_lambda_sum = 0
        h_lambda_sum = 0
        for i in range(n):
            if i != snd_ind:
                l_lambda_i = lambdas[i]
                h_lambda_i = lambdas[i]
            else:
                l_lambda_i = l_seg
                h_lambda_i = h_seg
            l_lambda_sum += l_lambda_i
            h_lambda_sum += h_lambda_i
            for j in range(n):
                if j != snd_ind:
                    l_lambda_j = lambdas[j]
                    h_lambda_j = lambdas[j]
                else:
                    l_lambda_j = l_seg
                    h_lambda_j = h_seg
                base = target[i] * target[j] * kernel[i][j]
                l_obj += base * l_lambda_i * l_lambda_j
                h_obj += base * h_lambda_i * h_lambda_j
        l_obj = 0.5 * l_obj - l_lambda_sum
        h_obj = 0.5 * h_obj - h_lambda_sum
        if l_obj < h_obj - eps:
            new_l_snd = l_seg
        elif l_obj > h_obj + eps:
            new_l_snd = h_seg
        else:
            new_l_snd = l_snd

    if math.fabs(new_l_snd - l_snd) < eps:
        return False

    new_l_fst = l_fst + y_fst * y_snd * (l_snd - new_l_snd)
    if eps < new_l_fst < C - eps:
        b += get_cached_err(fst_ind) + \
             y_fst * (new_l_fst - l_fst) * kernel[fst_ind][fst_ind] + \
             y_snd * (new_l_snd - l_snd) * kernel[fst_ind][snd_ind]
    else:
        b += get_cached_err(snd_ind) + \
             y_fst * (new_l_fst - l_fst) * kernel[fst_ind][snd_ind] + \
             y_snd * (new_l_snd - l_snd) * kernel[snd_ind][snd_ind]
    lambdas[fst_ind] = new_l_fst
    lambdas[snd_ind] = new_l_snd
    err_cache.clear()

    if eps < new_l_fst < C - eps:
        non_bound_q.append((True, fst_ind))
    else:
        non_bound_q.append((False, fst_ind))

    if eps < new_l_snd < C - eps:
        non_bound_q.append((True, snd_ind))
    else:
        non_bound_q.append((False, snd_ind))
    return True


def find_pair_ind_and_smo(snd_ind: int) -> bool:
    y_snd = target[snd_ind]
    l_snd = lambdas[snd_ind]
    snd_err = get_cached_err(snd_ind)
    signed_err = snd_err * y_snd
    if (signed_err > 1e-12 and eps < l_snd) or (l_snd < C - eps and signed_err < -1e-12):
        nonb_len = len(non_bound)
        if nonb_len > 1:
            best_fst = -1
            best_fst_err_diff = 2 * 1e9 + 1
            for fst in non_bound:
                fst_err = get_cached_err(fst)
                err_diff = math.fabs(fst_err - snd_err)
                if err_diff < best_fst_err_diff:
                    best_fst_err_diff = err_diff
                    best_fst = fst

            if best_fst != -1 and smo(best_fst, snd_ind):
                return True
            rand = random.randint(0, nonb_len - 1)
            non_bound_list = list(non_bound)
            for i in range(0, nonb_len):
                fst_ind = non_bound_list[(i + rand) % nonb_len]
                if smo(fst_ind, snd_ind):
                    return True
        rand = random.randint(0, n - 1)
        for i in range(n):
            if smo((i + rand) % n, snd_ind):
                return True
    return False


changes_cnt = 0
run_for_all = True
while (process_time() - start) * 1000 < 2450 and (changes_cnt > 0 or run_for_all):
    changes_cnt = 0
    for isAdd, ind in non_bound_q:
        if isAdd:
            non_bound.add(ind)
        else:
            non_bound.discard(ind)
    non_bound_q.clear()
    if run_for_all:
        for i in range(n):
            changes_cnt += find_pair_ind_and_smo(i)
    else:
        for i in non_bound:
            changes_cnt += find_pair_ind_and_smo(i)
    if run_for_all:
        run_for_all = False
    elif changes_cnt == 0:
        run_for_all = True

for l in lambdas:
    print("{0:.15f}".format(l))
print(-b)
