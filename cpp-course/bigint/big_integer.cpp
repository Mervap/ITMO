#include "big_integer.h"
#include <algorithm>
#include <stdexcept>

typedef unsigned int ui;
typedef unsigned long long ull;
using std::vector;

const ui MAX_DIGIT = UINT32_MAX;
const ui BASE = 32;
const int BASE_10 = 1000000000;

template<typename T>
ui ui_cast(T x) {
    return static_cast<ui>(x & MAX_DIGIT);
}

template<typename T>
ull ull_cast(T x) {
    return static_cast<ull>(x);
}

bool big_integer::is_zero() const {
    return (!sign) && (length() == 0);
}

size_t big_integer::length() const {
    return data.size();
}

big_integer big_integer::abs() const {
    return sign ? -(*this) : *this;
}

void big_integer::swap(big_integer &other) noexcept {
    std::swap(data, other.data);
    std::swap(sign, other.sign);
}


ui big_integer::get_digit(size_t ind) const {
    if (ind < length()) {
        return data[ind];
    } else if (sign) {
        return MAX_DIGIT;
    } else {
        return 0;
    }
}


void big_integer::make_fit() {
    while (!data.empty() && ((sign && data.back() == MAX_DIGIT) || (!sign && data.back() == 0))) {
        data.pop_back();
    }
}

big_integer::big_integer(bool new_sign, vector<ui> const &new_data) : sign(new_sign), data(new_data) {
    make_fit();
}

big_integer::big_integer() : sign(false) {}

big_integer::big_integer(big_integer const &other) : sign(other.sign), data(other.data) {
    make_fit();
}

big_integer::big_integer(int a) : sign(a < 0), data(1) {
    data[0] = ui_cast(a);
    make_fit();
}

big_integer::big_integer(ui a) : sign(0), data(1) {
    data[0] = a;
    make_fit();
}

int string_to_int(std::string const &s) {
    int ans = 0;
    for (auto a : s) {
        if (a < '0' || a > '9') {
            throw std::runtime_error("Incorrect char");
        }
        ans = ans * 10 + (a - '0');
    }
    return ans;
}

int pow_dec(ui st) {
    int res = 1;
    for (ui i = 0; i < st; ++i) {
        res *= 10;
    }
    return res;
}

big_integer string_to_number(std::string const &s) {
    big_integer res(0);
    size_t beg = 0;
    if (s[beg] == '-') {
        ++beg;
    }
    for (size_t i = beg; i + 9 <= s.length(); i += 9) {
        res = res * BASE_10 + string_to_int(s.substr(i, 9));
    }
    ui mod = (s.length() - beg) % 9;
    if (mod > 0) {
        res = res * pow_dec(mod) + string_to_int(s.substr(s.length() - mod, mod));
    }
    return beg > 0 ? -res : res;
}

big_integer::big_integer(std::string const &str) : big_integer(string_to_number(str)) {}

big_integer &big_integer::operator=(big_integer const &other) {
    big_integer tmp(other);
    swap(tmp);
    return *this;
}

big_integer &big_integer::operator+=(big_integer const &a) {
    *this = *this + a;
    return *this;
}

big_integer &big_integer::operator-=(big_integer const &a) {
    *this = *this - a;
    return *this;
}

big_integer &big_integer::operator*=(big_integer const &a) {
    *this = *this * a;
    return *this;
}

big_integer &big_integer::operator/=(big_integer const &a) {
    *this = *this / a;
    return *this;
}

big_integer &big_integer::operator%=(big_integer const &a) {
    *this = *this % a;
    return *this;
}

big_integer &big_integer::operator&=(big_integer const &a) {
    *this = *this & a;
    return *this;
}

big_integer &big_integer::operator|=(big_integer const &a) {
    *this = *this | a;
    return *this;
}

big_integer &big_integer::operator^=(big_integer const &a) {
    *this = *this ^ a;
    return *this;
}

big_integer &big_integer::operator<<=(ui a) {
    *this = *this << a;
    return *this;
}

big_integer &big_integer::operator>>=(ui a) {
    *this = *this >> a;
    return *this;
}

big_integer big_integer::operator+() const {
    return *this;
}

big_integer big_integer::operator-() const {
    return ~(*this) + 1U;
}

big_integer big_integer::operator~() const {
    vector<ui> tmp(data);
    for (size_t i = 0; i < data.size(); ++i) {
        tmp[i] = ~data[i];
    }
    return big_integer(!sign, tmp);
}

big_integer &big_integer::operator++() {
    *this = *this + 1;
    return *this;
}

big_integer big_integer::operator++(int) {
    big_integer tmp(*this);
    ++(*this);
    return tmp;
}

big_integer &big_integer::operator--() {
    *this = *this - 1;
    return *this;
}

big_integer big_integer::operator--(int) {
    big_integer tmp(*this);
    --(*this);
    return tmp;
}

big_integer operator+(big_integer const &a, big_integer const &b) {
    size_t n = std::max(a.length(), b.length()) + 1;
    vector<ui> tmp(n);
    ull c = 0;
    ull sum = 0;

    for (size_t i = 0; i < n; ++i) {
        sum = c + a.get_digit(i) + b.get_digit(i);
        tmp[i] = ui_cast(sum);
        c = sum >> BASE;
    }

    return big_integer(tmp.back() & (1 << (BASE - 1)), tmp);
}

big_integer operator-(big_integer const &a, big_integer const &b) {
    return a + (-b);

}

void mul_vector(vector<ui> &res, vector<ui> const &a, vector<ui> const &b) {
    for (size_t i = 0; i < a.size(); ++i) {
        ull c = 0;
        ull mul = 0;
        ull tmp = 0;

        for (size_t j = 0; j < b.size(); ++j) {
            size_t k = i + j;
            mul = (ull) a[i] * b[j];
            tmp = (mul & MAX_DIGIT) + res[k] + c;
            res[k] = ui_cast(tmp);
            c = (mul >> BASE) + (tmp >> BASE);
        }
        res[i + b.size()] += ui_cast(c);
    }
}

void big_integer::correct() {
    if (!sign) {
        return;
    } else if (length() == 0) {
        sign = !sign;
        return;
    }

    size_t n = length();
    ull sum = ull_cast(~data[0]) + 1ULL;
    ull carry = sum >> BASE;
    data[0] = ui_cast(sum);
    for (size_t i = 1; i < n; ++i) {
        sum = carry + ull_cast(~data[i]);
        data[i] = ui_cast(sum);
        carry = sum >> BASE;
    }
    data.push_back(ui_cast(carry + MAX_DIGIT));
    make_fit();
}

big_integer operator*(big_integer const &a, big_integer const &b) {

    if (a.is_zero() || b.is_zero()) {
        return big_integer(0u);
    }

    big_integer abs_a(a.abs());
    big_integer abs_b(b.abs());

    size_t len = (abs_a.length() + abs_b.length() + 1);
    vector<ui> tmp(len);
    mul_vector(tmp, abs_a.data, abs_b.data);

    big_integer res(a.sign ^ b.sign, tmp);
    res.correct();
    return res;
}

ui get_trial(const ui a, const ui b, const ui c) {
    ull res = a;
    res = ((res << BASE) + b) / c;
    if (res > MAX_DIGIT) {
        res = MAX_DIGIT;
    }
    return ui_cast(res);
}

void mul_big_small(vector<ui> &res, vector<ui> const &a, const ui b) {
    size_t n = a.size();
    res.resize(n + 1);
    ull carry = 0, mul = 0, tmp = 0;
    for (size_t i = 0; i < n; ++i) {
        mul = (ull) a[i] * b;
        tmp = (mul & MAX_DIGIT) + carry;
        res[i] = ui_cast(tmp);
        carry = (mul >> BASE) + (tmp >> BASE);
    }
    res[n] = ui_cast(carry);
}

void sub_equal_vectors(vector<ui> &a, vector<ui> const &b) {
    ull sum = ull_cast(~b[0]) + ull_cast(a[0]) + 1LL;
    ull carry = sum >> BASE;
    a[0] = ui_cast(sum);
    for (size_t i = 1; i < b.size(); ++i) {
        sum = ull_cast(~b[i]) + ull_cast(a[i]) + carry;
        a[i] = ui_cast(sum);
        carry = sum >> BASE;
    }
}

bool compare_equal_vectors(vector<ui> const &a, vector<ui> const &b) {
    for (size_t i = a.size(); i > 0; --i) {
        if (a[i - 1] != b[i - 1]) {
            return a[i - 1] < b[i - 1];
        }
    }
    return 0;
}

big_integer operator/(big_integer const &a, big_integer const &b) {
    if (b.is_zero()) {
        throw std::runtime_error("Divison by zero");
    }
    big_integer abs_a(a.abs());
    big_integer abs_b(b.abs());
    if (abs_a < abs_b) {
        return 0;
    }

    const ui f = ui_cast(((ull) MAX_DIGIT + 1) / ((ull) abs_b.data.back() + 1));
    const size_t n = abs_a.length();
    const size_t m = abs_b.length();
    mul_big_small(abs_a.data, abs_a.data, f);
    mul_big_small(abs_b.data, abs_b.data, f);
    abs_a.make_fit();
    abs_b.make_fit();

    const size_t len = n - m + 1;
    const ui divisor = abs_b.data.back();
    vector<ui> tmp(len);
    vector<ui> dev(m + 1);
    vector<ui> div(m + 1, 0);
    for (size_t i = 0; i <= m; ++i) {
        dev[i] = abs_a.get_digit(n + i - m);
    }

    for (size_t i = 0; i < len; ++i) {
        dev[0] = abs_a.get_digit(n - m - i);
        ui qt = get_trial(dev[m], dev[m - 1], divisor);
        mul_big_small(div, abs_b.data, qt);
        while ((qt >= 0) && compare_equal_vectors(dev, div)) {
            mul_big_small(div, abs_b.data, --qt);
        }
        sub_equal_vectors(dev, div);
        for (size_t j = m; j > 0; --j) {
            dev[j] = dev[j - 1];
        }
        tmp[len - 1 - i] = qt;
    }

    big_integer res(a.sign ^ b.sign, tmp);
    res.correct();
    return res;
}

big_integer operator%(big_integer const &a, big_integer const &b) {
    return a - (a / b) * b;
}

big_integer operator&(big_integer const &a, big_integer const &b) {
    size_t n = std::max(a.length(), b.length());
    vector<ui> tmp(n);

    for (size_t i = 0; i < n; ++i) {
        tmp[i] = a.get_digit(i) & b.get_digit(i);
    }
    return big_integer(a.sign & b.sign, tmp);
}

big_integer operator|(big_integer const &a, big_integer const &b) {
    size_t n = std::max(a.length(), b.length());
    vector<ui> tmp(n);

    for (size_t i = 0; i < n; ++i) {
        tmp[i] = a.get_digit(i) | b.get_digit(i);
    }
    return big_integer(a.sign | b.sign, tmp);
}

big_integer operator^(big_integer const &a, big_integer const &b) {
    size_t n = std::max(a.length(), b.length());
    vector<ui> tmp(n);

    for (size_t i = 0; i < n; ++i) {
        tmp[i] = a.get_digit(i) ^ b.get_digit(i);
    }
    return big_integer(a.sign ^ b.sign, tmp);
}

big_integer operator<<(big_integer const &a, ui b) {
    if (b == 0) {
        return big_integer(a);
    }
    size_t div = b / BASE;
    size_t mod = b % BASE;
    size_t new_size = a.length() + div + 1;
    vector<ui> tmp(new_size);
    tmp[div] = ui_cast((ull) a.get_digit(0) << mod);

    for (size_t i = div + 1; i < new_size; ++i) {
        ull x = (ull) a.get_digit(i - div) << mod;
        ull y = (ull) a.get_digit(i - div - 1) >> (BASE - mod);
        tmp[i] = ui_cast(x + y);
    }
    return big_integer(a.sign, tmp);
}

big_integer operator>>(big_integer const &a, ui b) {
    if (b == 0) {
        return big_integer(a);
    }
    size_t div = b / BASE;
    size_t mod = b % BASE;
    size_t new_size = 0;
    if (div < a.length()) {
        new_size = a.length() - div;
    }
    vector<ui> tmp(new_size);
    for (size_t i = 0; i < new_size; ++i) {
        ull x = (ull) a.get_digit(i + div) >> mod;
        ull y = (ull) a.get_digit(i + div + 1) << (BASE - mod);
        tmp[i] = ui_cast(x + y);
    }
    return big_integer(a.sign, tmp);
}

bool operator==(big_integer const &a, big_integer const &b) {
    return (a.sign == b.sign) && (a.data == b.data);
}

bool operator!=(big_integer const &a, big_integer const &b) {
    return !(a == b);
}

bool operator<(big_integer const &a, big_integer const &b) {
    if (a.sign != b.sign) {
        return a.sign;
    }
    if (a.length() != b.length()) {
        return a.length() < b.length();
    }

    for (size_t i = a.length(); i > 0; --i) {
        if (a.get_digit(i - 1) != b.get_digit(i - 1)) {
            return a.get_digit(i - 1) < b.get_digit(i - 1);
        }
    }
    return 0;
}

bool operator>(big_integer const &a, big_integer const &b) {
    return b < a;
}

bool operator<=(big_integer const &a, big_integer const &b) {
    return !(a > b);
}

bool operator>=(big_integer const &a, big_integer const &b) {
    return !(a < b);
}

std::string to_string(big_integer const &a) {
    if (a.is_zero()) {
        return "0";
    } else if (a.length() == 0) {
        return "-1";
    }

    std::string ans = "";
    big_integer abs_a(a.abs());

    while (!abs_a.is_zero()) {
        ui tmp = (abs_a % BASE_10).get_digit(0);
        for (size_t i = 0; i < 9; i++) {
            ans.push_back('0' + tmp % 10);
            tmp /= 10;
        }
        abs_a /= BASE_10;
    }

    while (!ans.empty() && ans.back() == '0') {
        ans.pop_back();
    }

    if (a.sign) {
        ans.push_back('-');
    }

    reverse(ans.begin(), ans.end());
    return ans;
}
