#!/bin/bash


repl(){
  clj \
    -J-Dclojure.core.async.pool-size=1 \
    -X:Ripley Ripley.core/process \
    :main-ns Dr-Rush.Robert.main
}

main(){
  clojure \
    -J-Dclojure.core.async.pool-size=1 \
    -M -m Dr-Rush.Robert.main
}

jar(){

  rm -rf out/*.jar
  # COMMIT_HASH=$(git rev-parse --short HEAD)
  # COMMIT_COUNT=$(git rev-list --count HEAD)
  clojure \
    -X:Genie Genie.core/process \
    :main-ns Dr-Rush.Robert.main \
    :filename "\"out/Dr-Rush-Robert.jar\"" \
    :paths '["src"]'
}

release(){
  jar
}

"$@"