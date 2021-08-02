#!/bin/bash

repl(){
  clj \
    -X:repl deps-repl.core/process \
    :main-ns starnet.main \
    :port 7788 \
    :host '"0.0.0.0"' \
    :repl? true \
    :nrepl? false
}

main(){
  clojure \
    -J-Dclojure.core.async.pool-size=1 \
    -J-Dclojure.compiler.direct-linking=false \
    -M -m starnet.main
}

uberjar(){
  clj \
    -X:uberjar genie.core/process \
    :uberjar-name out/starnet.standalone.jar \
    :main-ns starnet.main
  mkdir -p out/jpackage-input
  mv out/starnet.standalone.jar out/jpackage-input/
}

"$@"