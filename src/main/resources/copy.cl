__kernel void copy(__global double* in1, __global double* in2, int n)
{
    int i = get_global_id(0);
    if ( i >= n ) return;

    in1[i] = i;
    in2[i] = i;
}

__kernel void sum(__global const double* in1, __global const double* in2, __global double* out, int n)
{
    int i = get_global_id(0);
    if ( i >= n ) return;

    out[i] = in1[i] + in2[i];
}