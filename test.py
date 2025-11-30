def root(n):
    res = 0
    while(res*res<n):
        res = res + 1
    if(res>n):
        return res-1
    else:
        return res

# ((num = %%)){num}
print(root(num))
