//Писалось мной, но TL, возможно стоит запускать под шыгфд с++
//Рабочая версия в К_2

#include <iostream>
#include <vector>
#include <algorithm>
#include <stddef.h>
#include <cassert>

using namespace std;

struct node{
    node *p, *r, *ch;
    int k;
    int deg, h, el;
};


node* mn1 = new node;
node* h = new node;
node *heaps[1001];
node *pos[1000001];
int k = 0;
int MINIMUM = (1 << 31);


void make_node(int key, int h, node *a){
    (*a).k = key;
    (*a).p = 0;
    (*a).r = 0;
    (*a).ch = 0;
    (*a).deg = 0;
    (*a).h = h;
    (*a).el = k;
}

void merge(int a, int b){
    if (heaps[a] == 0){
        return;
    }
    if (heaps[b] == 0){
        heaps[b] = heaps[a];
        heaps[a] = 0;
        return;
    }
    node* cur_h = h;
    (*cur_h).r = 0;
    node* h1 = heaps[a];
    node* h2 = heaps[b];

    int z = 0;
    while(h1 != 0 && h2 != 0){
        bool f1 = (*h1).deg < (*h2).deg;
       // bool f2 = (*h1).deg == (*h2).deg;
        //bool f3 = (*h1).k < (*h2).k;
        //bool f4 = (*h1).k == (*h2).k;
        //bool f5 = (*h1).el < (*h2).el;
        if( f1 ){//} || (f2 && (f3 || (f4 && f5) ) ) ){
            (*cur_h).r = h1;
            cur_h = h1;
            (*cur_h).h = b;
            h1 = (*h1).r;
        } else{
            (*cur_h).r = h2;
            cur_h = h2;
            h2 = (*h2).r;
        }
        ++z;
    }

    if(h1 == 0){
        while(h2 != 0){
            (*cur_h).r = h2;
            if(cur_h != 0 && (*cur_h).deg > (*h2).deg){
            }
            cur_h = h2;
            h2 = (*h2).r;
            ++z;

        }
    } else{
        while(h1 != 0){
            (*cur_h).r = h1;
            cur_h = h1;
            (*cur_h).h = b;
            h1 = (*h1).r;
            ++z;
        }
    }

    cur_h = (*h).r;
    node* prev = h;

    while((*cur_h).r != 0){
        node* nx = (*cur_h).r;
        if((*cur_h).deg == (*nx).deg) {
            if( (*nx).r != 0 && (*nx).deg == (*(*nx).r).deg){
                cur_h = nx;
                nx = (*nx).r;
            }
            if((*cur_h).k > (*nx).k || ((*cur_h).k == (*nx).k && (*cur_h).el > (*nx).el)){
                (*prev).r = nx;
                (*cur_h).r = (*nx).r;
                (*nx).r = cur_h;
                swap(nx, cur_h);
            }

            (*cur_h).r = (*nx).r;
            (*nx).r = (*cur_h).ch;
            (*nx).p = cur_h;
            (*cur_h).ch = nx;
            ++((*cur_h).deg);
        } else{
            prev = cur_h;
            cur_h = (*cur_h).r;
        }
        ++z;
    }

    cur_h = (*h).r;

    heaps[b] = (*h).r;
    heaps[a] = 0;
}

void get_min(int a){
    int mn = -(MINIMUM+1);
    int minel = 1000001;
    node* cur = heaps[a];

    while(cur != 0){
        if((*cur).k < mn || ((*cur).k == mn && (*cur).el < minel)){
            mn = (*cur).k;
            minel = (*cur).el;
        }
        cur = (*cur).r;
    }

    printf("%d\n", mn);
}

void extractMin(int a){
    node* mn = mn1;
    (*mn).k = -(MINIMUM+1);
    (*mn).el = 1000001;
    node* prev = 0;
    node* cur = heaps[a];
    node* cur1 = 0;

    while(cur != 0){
        if((*cur).k < (*mn).k || ((*cur).k == (*mn).k && (*cur).el < (*mn).el)){
            mn = cur;
            prev = cur1;
        }
        cur1 = cur;
        cur = (*cur).r;
    }

    if(prev == 0){
        heaps[a] = (*mn).r;
    } else{
        (*prev).r = (*mn).r;
    }

    cur = (*mn).ch;
    heaps[0] = cur;
    while(cur != 0){
        heaps[0] = cur;
        (*cur).p = 0;
        cur1 = (*cur).r;
        (*cur).r = 0;
        merge(0, a);
        cur = cur1;
    }
}


void decreaseKey(int a, int v){
    node* h = pos[a];
    if(v > (*h).k){
        (*h).k = v;
        node* ch = (*h).ch;
        while(ch != 0){
            node* mn = mn1;
            (*mn).k = -(MINIMUM+1);
            (*mn).el = 1000001;
            node* cur = ch;

            while(cur != 0){
                if((*cur).k < (*mn).k || ((*cur).k == (*mn).k && (*cur).el < (*mn).el)){
                    mn = cur;
                }
                cur = (*cur).r;
            }

            ch = mn;
            if((*h).k > (*ch).k || ((*h).k == (*ch).k && (*h).el > (*ch).el)){
                swap((*h).k, (*ch).k);
                swap((*h).el, (*ch).el);
                swap(pos[(*h).el], pos[(*ch).el]);
                h = ch;
                ch = (*h).ch;
            } else{
                break;
            }
        }
    } else{
        (*h).k = v;
        node* p = (*h).p;
        while(p != 0 && ((*h).k < (*p).k || ((*h).k == (*p).k && (*h).el <= (*p).el))){
            swap((*h).k, (*p).k);
            swap((*h).el, (*p).el);
            swap(pos[(*h).el], pos[(*p).el]);
            h = p;
            p = (*h).p;
        }

    }
}

int main(){

    //freopen("heap.in", "r", stdin);
    //freopen("heap.out", "w", stdout);

    int n, m;
    scanf("%d%d",&n, &m);

    for(int i = 0; i <= n; i++){
        heaps[i] = 0;
    }


    int a,b,i,t;
    int v;
    for(int q = 0; q < m; ++q){
        scanf("%d", &t);
        if(t == 0){
            scanf("%d%d", &a, &v);
            node *el = new node;
            ++k;
            make_node(v, a, el);
            heaps[0] = el;
            pos[k] = el;
            merge(0, a);
        }
        if(t == 1){
            scanf("%d%d", &a, &b);
            merge(a,b);

        }
        if(t == 2){
            scanf("%d", &i);
            decreaseKey(i, MINIMUM);
            extractMin((*pos[i]).h);
            pos[i] = 0;
        }
        if(t == 3){
            scanf("%d%d", &i, &v);
            decreaseKey(i, v);
        }
        if(t == 4){
            scanf("%d", &a);
            get_min(a);
        }
        if(t == 5){
            scanf("%d", &a);
            extractMin(a);
        }
    }
    return 0;
}
