cmake_minimum_required(VERSION 3.15)
project(VideoCards C)

set(CMAKE_C_STANDARD 99)
set(CMAKE_C_FLAGS "${NO_OPENMP} -Wall -fopenmp")
find_package(OpenCL REQUIRED)
include_directories(${OpenCL_INCLUDE_DIRS})

##############
# Practice 1 #
##############

# Find primes count
add_executable(Primes Practice_1/primes.c)
# Matrix multiply
add_executable(MulMatrix Practice_1/mul_matrix.c)

##############
# Practice 2 #
##############

# Sum of two numbers on gpu
add_executable(Sum Practice_2/sum.c)
target_link_libraries(Sum ${OpenCL_LIBRARY})

##############
# Practice 3 #
##############

# Sum of two numbers on gpu
add_executable(MulMatrixOpenCl Practice_3/mul_matrix_opencl.c)
target_link_libraries(MulMatrixOpenCl ${OpenCL_LIBRARY})


##############
# Practice 4 #
##############

# Sum of two numbers on gpu
add_executable(MulMatrixOpenClTile Practice_4/mul_matrix_opencl.c)
target_link_libraries(MulMatrixOpenClTile ${OpenCL_LIBRARY})