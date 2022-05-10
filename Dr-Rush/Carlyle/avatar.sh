#!/bin/bash

repl(){
  npm i --no-package-lock
  mkdir -p out/
  cp src/Dr_Rush/Carlyle/index.html out/index.html
  cp package.json out/package.json
  clojure -A:Moana -M -m shadow.cljs.devtools.cli clj-repl
  # (shadow/watch :main)
  # (shadow/repl :main)
  # :repl/quit
}

release(){
  npm i --no-package-lock
  mkdir -p out/
  cp src/Dr_Rush/Carlyle/index.html out/index.html
  cp package.json out/package.json
  clojure -A:Moana -M -m shadow.cljs.devtools.cli release main
}

"$@"