#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main()
{
    freopen("nextbrackets.in", "r", stdin);
    freopen("nextbrackets.out", "w", stdout);

    string s;
    cin >> s;
    int l = s.length();

    int cnt = 0;

    int i;
    for(i = l - 1; i > 0; --i){
        if(s[i] == '('){
            ++cnt;
            if(cnt < 0){
                break;
            }
        } else{
            --cnt;
        }
    }

    if(i == 0){
        cout << "-";
        return 0;
    } else{
        int b = 0;
        int a = 0;
        for(int j = 0; j < i; ++j){
            cout << s[j];
            if(s[j] == '('){
                ++b;
            }
            ++a;
        }
        cout << ')';
        ++a;
        for(int j = 0; j < (l/2) - b; ++j){
            cout << '(';
            ++a;
        }

        for(int j = a; j < l; ++j){
            cout << ')';
        }
    }
    return 0;
}
