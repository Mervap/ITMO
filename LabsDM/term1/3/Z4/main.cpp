#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <fstream>

using namespace std;

int main()
{
    freopen("chaincode.in", "r", stdin);
    freopen("chaincode.out", "w", stdout);

    int n;
    cin >> n;

    set<string> a;

    string s = "";
    for(int i = 0; i < n; i++){
        s+="0";
    }

    while(a.find(s) == a.end()){
        cout << s << "\n";
        a.insert(s);
        s = s.substr(1, n-1);
        if(a.find(s+"1") == a.end()){
            s += "1";
        } else{
            s+= "0";
        }
    }

    return 0;
}
