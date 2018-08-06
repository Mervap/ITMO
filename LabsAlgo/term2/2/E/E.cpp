#include <iostream>
#include <vector>
#include <random>
#include <ctime>

#define _GLIBXX_USE_RANDOM_TR1_

using namespace std;
const long long kol = 1000007;

const char *val[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

string toHex(int m) {
    string ans = "";
    for (int i = 0; i < 2; ++i) {
        ans = val[m % 16] + ans;
        m /= 16;
    }

    return ans;
}

string longToHex(int m) {
    string ans = "";
    vector<string> val = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    for (int i = 0; i < 4; ++i) {
        ans += toHex(m % 256);
        m /= 256;
    }

    return ans;
}

void longToHex2(int m) {
    for (int i = 0; i < 4; ++i) {
        int k = m % 256;
        fputs(val[k / 16], stdout);
        fputs(val[k % 16], stdout);
        m /= 256;
    }
}

int main() {

    srand(static_cast<unsigned int>(time(0)));

    cin.tie(0);
    cout.tie(0);
    int n;
    cin >> n;

    vector<long long> a(n);
    for (int i = 0; i < n; ++i) {
        cin >> a[i];
    }


    if (n == 1) {
        cout << "ff";
        return 0;
    }

    bool f = true;
    long long a1, b1, a2, b2, k;

    vector<bool> was(kol);
    while (f) {
        b2 = rand();
        a2 = rand();
        a1 = rand();
        b1 = rand();
        k  = rand();

        was.assign(kol, 0);
        bool f1 = true;
        for (int i = 0; i < n; ++i) {
            long long h1 = ((a1 * a[i]) % kol + b1) % kol;
            long long h2 = ((a2 * a[i]) % kol + b2) % kol;
            if (was[(k * h2 % kol + h1) % kol]) {
                f1 = false;
                break;
            }

            was[(k * h2 % kol + h1) % kol] = true;
        }

        if (f1) {
            f = false;
        }
    }

    cout << "31" << toHex(2) << longToHex(kol); //r2 = kol
    cout << "31" << toHex(3) << longToHex(a1); //r3 = a1
    cout << "31" << toHex(4) << longToHex(b1); //r4 = b1
    cout << "31" << toHex(5) << longToHex(a2); //r5 = a2
    cout << "31" << toHex(6) << longToHex(b2); //r6 = b2
    cout << "31" << toHex(15) << longToHex(k); //r15 = k

    cout << "03" << toHex(0) << toHex(3) << toHex(8) << toHex(9); //(r8, r9) = a1*k
    cout << "05" << toHex(8) << toHex(2) << toHex(8) << toHex(9); //r8 = a1*k % kol
    cout << "01" << toHex(8) << toHex(4) << toHex(8); //r8 = a1*k % kol + b1
    cout << "05" << toHex(8) << toHex(2) << toHex(1) << toHex(12); //r1 = (a1*k + b1) % kol = h1

    cout << "03" << toHex(0) << toHex(5) << toHex(10) << toHex(11); //(r10, r11) = a2*k
    cout << "05" << toHex(10) << toHex(2) << toHex(10) << toHex(11); //r10 = a2*k % kol
    cout << "01" << toHex(10) << toHex(6) << toHex(10); //r10 = a2*k % kol + b2
    cout << "05" << toHex(10) << toHex(2) << toHex(0) << toHex(12); //r0 = (a2*k + b2) % kol = h2

    cout << "03" << toHex(0) << toHex(15) << toHex(0) << toHex(100); //r0 = r0*4
    cout << "05" << toHex(0) << toHex(2) << toHex(0) << toHex(100); //r0 = r0*4
    cout << "01" << toHex(0) << toHex(1) << toHex(0); //r0 = h1 ^ h2
    cout << "05" << toHex(0) << toHex(2) << toHex(0) << toHex(12); //r0 = (h1 ^ h2) % kol

    cout << "31" << toHex(1) << longToHex(4); //r1 = 4
    cout << "03" << toHex(0) << toHex(1) << toHex(0) << toHex(11); //r0 = r0*4
    cout << "31" << toHex(1) << longToHex(118); //r1 = code_len = 102 bit's
    cout << "01" << toHex(0) << toHex(1) << toHex(0); //r0 = r0 + 102
    cout << "30" << toHex(0) << toHex(0); // r0 = answer

    cout << "ff";

    int c = 0;
    for (int i = 0; i < kol; ++i) {
        if (was[i]) {
            longToHex2(c);
            ++c;
        } else {
            fputs("00000000", stdout);
        }
    }

    /*for (int i = 0; i < n; ++i) {
        long long h1 = (a1 * a[i] + b1) % (kol);
        long long h2 = (a2 * a[i] + b2) % (kol);
        cout << (h1 ^ h2) % (kol) << "\n";
    }*/

    return 0;
}