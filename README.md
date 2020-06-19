[maven]: http://maven.apache.org/
[java]: https://www.oracle.com/java/index.html
[git]: https://git-scm.com/
[make]: https://www.gnu.org/software/make
[cytoscape]: https://cytoscape.org/
[directappinstall]: http://manual.cytoscape.org/en/stable/App_Manager.html#installing-apps
[appstore]: http://apps.cytoscape.org/apps/webbymcsearch

Webby McSearch
=======================================

[![Build Status](https://travis-ci.com/idekerlab/webbymcsearch.svg?branch=master)](https://travis-ci.com/idekerlab/webbymcsearch) [![Coverage Status](https://coveralls.io/repos/github/idekerlab/webbymcsearch/badge.svg?branch=master)](https://coveralls.io/github/idekerlab/webbymcsearch?branch=master)


Webby McSearch is a Cytoscape App that lets one
perform web queries of a node attribute for a
selected node on Google, PubMed, or other common
search engines.

**NOTE:** This service is experimental. The interface is subject to change.

**Publication**

TODO ADD

Requirements to use
=====================

* [Cytoscape][cytoscape] 3.7 or above
* Internet connection to allow App to connect to remote services



Installation via from Cytoscape
======================================

TODO

Requirements to build (for developers)
========================================

* [Java][java] 8+ with jdk
* [Maven][maven] 3.4 or above

To build documentation

* Make
* Python 3+
* Sphinx (install via `pip install sphinx`)
* Sphinx rtd theme (install via `pip install sphinx_rtd_theme`)


Building manually
====================

Commands below assume [Git][git] command line tools have been installed

```Bash
# Can also just download repo and unzip it
git clone https://github.com/idekerlab/webbymcsearch

cd webbymcsearch
mvn clean test install
```

The above command will create a jar file under **target/** named
**webbymcsearch-\<VERSION\>.jar** that can be installed
into [Cytoscape][cytoscape]


Open Cytoscape and follow instructions [here][directappinstall] and click on
**Install from File...** button to load the jar created above.


Building documentation
=========================

Documentation is stored under `docs/` directory and
uses Sphinx & Python to generate documentation that
is auto uploaded from **master** branch to Read the docs TODO

```Bash
# The clone and directory change can be
# omitted if done above
git clone https://github.com/idekerlab/webbymcsearch

cd webbymcsearch
make docs
```
Once `make docs` is run the documentation should automatically
be displayed in default browser, but if not open `docs/_build/html/index.html` in
a web browser
 
COPYRIGHT AND LICENSE
========================

[Click here](LICENSE)

Acknowledgements
=================

* TODO denote funding sources
