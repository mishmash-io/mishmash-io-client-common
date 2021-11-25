

.PHONY: py-clean-pyc py-clean-build py-clean

py-clean: py-clean-build py-clean-pyc 

py-clean-build:
	rm -fr .eggs/
	rm -fr build/*
	rm -fr dist/*
	find . -name '*.egg-info' -exec rm -fr {} +
	find . -name '*.egg' -exec rm -f {} +
	find . -name '*.whl' -exec rm -f {} +

py-clean-pyc:
	find . -name '*.pyc' -exec rm -f {} +
	find . -name '*.pyo' -exec rm -f {} +
	find . -name '__pycache__' -exec rm -fr {} +
