# Copyright 2019 MISHMASH I O OOD
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import os
from setuptools import setup
from setuptools.command.develop import develop
from setuptools.command.sdist import sdist
from setuptools.command.build_py import build_py


def readme():
    try:
        with open('README.md', 'r') as doc:
            return doc.read()
    except IOError:
        return """\
# mishmash-io-client

See [mishmash io](https://mishmash.io) for documentation.
"""


def version():
    with open('VERSION.txt', 'r') as version:
        return version.readline().strip()


HERE = os.path.abspath(os.path.dirname(__file__))
PROTOBUF_FILE_DESTINATION = os.path.join(HERE, "protobuf")
OUTPUT_DESTINATION = os.path.join("mishmash-rpc-python", "gen-src")

def build_mishmash_grpc_files():
    import grpc_tools.protoc
    grpc_tools.protoc.main([
        'grpc_tools.protoc',
        '-I{}'.format(PROTOBUF_FILE_DESTINATION),
        '--python_out={}'.format(OUTPUT_DESTINATION),
        '--grpc_python_out={}'.format(OUTPUT_DESTINATION),
        "mishmash_rpc.proto"
    ])


class CustomBuildPyCommand(build_py):
    def run(self):
        build_mishmash_grpc_files()
        build_py.run(self)


class CustomSDistCommand(sdist):
    def run(self):

        build_mishmash_grpc_files()
        sdist.run(self)


class CustomDevelopCommand(develop):
    def run(self):
        build_mishmash_grpc_files()
        develop.run(self)


setup(
    name='mishmash-io-rpc',
    version=version(),
    description='mishmash gRPC client service',
    long_description=readme(),
    long_description_content_type='text/markdown',
    author='mishmash.io',
    author_email='info@mishmash.io',
    url='https://mishmash.io',
    project_urls={
        'Bug Tracker': 'https://github.com/mishmash-io/mishmash-io-client-common/issues',
        'Documentation': 'https://mishmash.io',
        'Source Code': 'https://github.com/mishmash-io/mishmash-io-client-common/',
    },
    include_package_data=True,
    license='Apache License v2.0',
    classifiers=[
        'Intended Audience :: Developers',
        'License :: OSI Approved :: Apache Software License',
        'Programming Language :: Python',
        'Programming Language :: Python :: 3 :: Only',
        'Programming Language :: Python :: 3.6',
        'Programming Language :: Python :: 3.7',
        'Programming Language :: Python :: 3.8',
        'Development Status :: 3 - Alpha',
        'Topic :: Database',
        'Topic :: Utilities',
        'Topic :: Scientific/Engineering :: Artificial Intelligence',
        'Topic :: Scientific/Engineering :: Information Analysis',
        'Topic :: Scientific/Engineering :: Interface Engine/Protocol Translator',
        'Topic :: Scientific/Engineering :: Mathematics',
        'Topic :: Software Development',
        'Topic :: Software Development :: Libraries'
    ],
    keywords='database, artificial intelligence, development',
    python_requires='>=3.6, <4',

    package_dir={'': OUTPUT_DESTINATION},
    py_modules=['mishmash_rpc_pb2', 'mishmash_rpc_pb2_grpc'],

    install_requires=['grpcio>=1.41.1',
                      'protobuf>=3.19.0'],

    setup_requires=[
        "grpcio-tools>=1.41.1"
    ],

    cmdclass={
        'build_py': CustomBuildPyCommand,
        'sdist': CustomSDistCommand,
        'develop': CustomDevelopCommand
    }
)
