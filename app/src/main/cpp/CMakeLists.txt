cmake_minimum_required(VERSION 3.22.1)

project(growlauncher-native LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

add_library(native-lib SHARED native-lib.cpp)

target_compile_options(native-lib PRIVATE
        -O3
        -fvisibility=hidden
        -ffunction-sections
        -fdata-sections
        -DARM64_ONLY=1)

target_link_options(native-lib PRIVATE -Wl,--gc-sections)

find_library(log-lib log)
find_library(dl-lib dl)

target_link_libraries(native-lib PRIVATE ${log-lib} ${dl-lib})
