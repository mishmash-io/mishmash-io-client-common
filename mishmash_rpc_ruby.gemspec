#    Copyright 2025 Mishmash IO UK Ltd.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

Gem::Specification.new do |s|
    s.name = 'mishmash_rpc_ruby'
    s.version = '0.0.1'
    s.license = 'Apache-2.0'
    s.summary = 'mishmash.io gRPC client services'
    s.description = 'mishmash.io gRPC client services'
    s.authors = ['mishmash io']
    s.email = 'info@mishmash.io'
    s.homepage = 'https://mishmash.io'
    s.metadata = {
        'source_code_uri' => 'https://github.com/mishmash-io',
        'homepage_uri' => 'https://mishmash.io',
        'documentation_uri' => 'https://mishmash.io'
    }
    s.files = [
        'mishmash-rpc-ruby/src/',
        'mishmash-rpc-ruby/gen-src/'
    ]
    s.require_paths = ["mishmash-rpc-ruby/src/", "mishmash-rpc-ruby/gen-src/"]
    s.requirements << 'grpc, v1.28.0'
    s.add_development_dependency 'grpc-tools', '~> 1.28', '>= 1.28.0'
    s.add_runtime_dependency 'grpc', '~> 1.28', '>= 1.28.0'
    s.extra_rdoc_files = ['README.md', 'LICENSE', 'VERSION.txt']
end
