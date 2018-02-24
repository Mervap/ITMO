#include <iostream>
#include <vector>
#include <fstream>
#include <string>

#define f first
#define s second

using namespace std;

vector< pair<int,int> > q;
vector<int> p;

void sift_up(int i){
    if(i == 0){
        return;
    }

    if(q[i].f < q[(i-1)/2].f){
        pair<int,int> tmp = q[i];
        q[i] = q[(i-1)/2];
        q[(i-1)/2] = tmp;

        int tmp1 = p[q[i].s];
        p[q[i].s] = p[q[(i-1)/2].s];
        p[q[(i-1)/2].s] = tmp1;
        sift_up((i-1)/2);
    }
}

void sift_down(int i){
    if(i*2+1 > (int) q.size()-1 && i*2+2 > (int) q.size()-1){
        return;
    }

    int j = i*2+1;
    if(i*2+2 < (int) q.size() && q[i*2+2].f < q[i*2+1].f){
        j = i*2+2;
    }
    if(q[i].f > q[j].f){
        pair<int,int> tmp = q[i];
        q[i] = q[j];
        q[j] = tmp;

        int tmp1 = p[q[i].s];
        p[q[i].s] = p[q[j].s];
        p[q[j].s] = tmp1;
        sift_down(j);
    }
}

int main() {
    freopen("priorityqueue2.in", "r", stdin) ;
    freopen("priorityqueue2.out", "w", stdout) ;

    string s;
    int a, b, k=0;

    p.push_back(-1);
    while(cin >> s){
        ++k;
        p.push_back(-1);
        if(s == "push"){
            cin >> a;
            q.push_back(make_pair(a,k));
            p[k] = (int) q.size()-1;

            sift_up((int) q.size()-1);

        } else if (s == "extract-min"){
            if(q.empty()){
                cout << "*\n";
                continue;
            }
            cout << q[0].f << " " << q[0].s << "\n";

            p[q[0].s] = -1;

            q[0] = q[q.size()-1];
            q.erase(q.end()-1);

            if(q.empty()){
                continue;
            }

            p[q[0].s] = 0;

            sift_down(0);

        } else{
            cin >> a >> b;

            q[p[a]].f = b;
            sift_up(p[a]);
            sift_down(p[a]);
        }
    }
    return 0;
}
