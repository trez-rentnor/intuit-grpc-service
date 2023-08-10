# Copyright 2018 gRPC authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# cmake build file for C++ personal_info example.
# Assumes protobuf and gRPC have been installed using cmake.

cmake_minimum_required(VERSION 3.8)

project(HelloWorld C CXX)

# Targets personal_info_[async_](client|server)
include(cmake/common.cmake)

# Generated sources
get_filename_component(pi_proto "../protos/personal_info.proto" ABSOLUTE)
get_filename_component(pi_proto_path "${pi_proto}" PATH)

set(pi_proto_srcs "${CMAKE_CURRENT_BINARY_DIR}/personal_info.pb.cc")
set(pi_proto_hdrs "${CMAKE_CURRENT_BINARY_DIR}/personal_info.pb.h")
set(pi_grpc_srcs "${CMAKE_CURRENT_BINARY_DIR}/personal_info.grpc.pb.cc")
set(pi_grpc_hdrs "${CMAKE_CURRENT_BINARY_DIR}/personal_info.grpc.pb.h")
add_custom_command(
        OUTPUT "${pi_proto_srcs}" "${pi_proto_hdrs}" "${pi_grpc_srcs}" "${pi_grpc_hdrs}"
        COMMAND ${_PROTOBUF_PROTOC}
        ARGS --grpc_out "${CMAKE_CURRENT_BINARY_DIR}"
        --cpp_out "${CMAKE_CURRENT_BINARY_DIR}"
        -I "${pi_proto_path}"
        --plugin=protoc-gen-grpc="${_GRPC_CPP_PLUGIN_EXECUTABLE}"
        "${pi_proto}"
        DEPENDS "${pi_proto}")

# Include generated *.pb.h files
include_directories("${CMAKE_CURRENT_BINARY_DIR}")

# pi_grpc_proto
add_library(pi_grpc_proto
        ${pi_grpc_srcs}
        ${pi_grpc_hdrs}
        ${pi_proto_srcs}
        ${pi_proto_hdrs})
target_link_libraries(pi_grpc_proto
        ${_REFLECTION}
        ${_GRPC_GRPCPP}
        ${_PROTOBUF_LIBPROTOBUF})

add_executable(personal_info_server "personal_info_server.cc")
target_link_libraries(personal_info_server
  pi_grpc_proto
  absl::flags
  absl::flags_parse
  ${_REFLECTION}
  ${_GRPC_GRPCPP}
  ${_PROTOBUF_LIBPROTOBUF})
