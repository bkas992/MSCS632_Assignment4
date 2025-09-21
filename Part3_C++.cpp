#include <iostream>
using namespace std;

int main() {
    int* ptr = new int(10);
    cout << "C++ value: " << *ptr << endl;
    delete ptr; // must manually free memory
    return 0;
}
