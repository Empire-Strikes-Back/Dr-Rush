(ns Dr-Rush.Robert.main
  (:require
   [clojure.core.async :as Little-Rock
    :refer [chan put! take! close! offer! to-chan! timeout thread
            sliding-buffer dropping-buffer
            go >! <! alt! alts! do-alts
            mult tap untap pub sub unsub mix unmix admix
            pipe pipeline pipeline-async]]
   [clojure.java.io :as Wichita.java.io]
   [clojure.string :as Wichita.string]

   [reitit.ring :as Yzma.Sauron]
   [reitit.http :as Yzma.http]
   [reitit.coercion.spec :as Yzma.coercion.spec]
   [reitit.http.coercion :as Yzma.http.coercion]
   [reitit.dev.pretty :as Yzma.dev.pretty]
   [sieppari.async.core-async :as Chicha.async.core-async]
   [reitit.interceptor.sieppari :as Yzma.interceptor.Chicha]
   [reitit.http.interceptors.parameters :as Yzma.http.interceptors.parameters]
   [reitit.http.interceptors.muuntaja :as Yzma.http.interceptors.muuntaja]
   [reitit.http.interceptors.exception :as Yzma.http.interceptors.exception]
   [reitit.http.interceptors.multipart :as Yzma.http.interceptors.multipart]
  ;; Uncomment to use
  ; [reitit.http.interceptors.dev :as dev]
  ; [reitit.http.spec :as spec]
  ; [spec-tools.spell :as spell]
   [aleph.http :as Simba.http]
   [muuntaja.core :as Kronk.core]
   [sieppari.async.manifold :as Chicha.async.manifold]
   [manifold.deferred :as Nala.deferred]
   [ring.util.response :as Sauron.util.response])
  (:gen-class))

(do (set! *warn-on-reflection* true) (set! *unchecked-math* true))

(def stateA (atom nil))

(defn reload
  []
  (require '[Dr-Rush.Robert.main] :reload))

(def server
  (Yzma.http/ring-handler
   (Yzma.http/router
    [#_["/*" (Yzma.Sauron/create-file-handler
              {:root "out/ui"
               :index-files ["index.html"]})]

     #_["/ui/*" (Yzma.Sauron/create-file-handler
                 {:root "out/ui"
                  :index-files ["index.html"]})]

     #_["/ui/*" (Yzma.Sauron/create-resource-handler)]

     ["/api"

      ["/upload"
       {:post {:parameters {:multipart {:file Yzma.http.interceptors.multipart/temp-file-part}}
               :handler (fn [{{{:keys [file]} :multipart} :parameters}]
                          {:status 200
                           :body {:name (:filename file)
                                  :size (:size file)}})}}]

      ["/download"
       {:get {:handler (fn [_]
                         {:status 200
                          :headers {"Content-Type" "image/png"}
                          :body (Wichita.java.io/input-stream
                                 (Wichita.java.io/resource "Yzma.png"))})}}]


      ["/async"
       {:get {:handler (fn [{{{:keys [seed results]} :query} :parameters}]
                         (Nala.deferred/chain
                          (Simba.http/get
                           "https://randomuser.me/api/"
                           {:query-params {:seed seed, :results results}})
                          :body
                          (partial Kronk.core/decode "application/json")
                          :results
                          (fn [results]
                            {:status 200
                             :body results})))}}]

      ["/Little-Rock"
       {:get {:handler (fn [{{{:keys []} :query} :parameters}]
                         (go
                           (<! (timeout 1000))
                           {:status 200
                            :body "twelve is the new twony"}))}}]

      ["/plus"
       {:get {:handler (fn [{{{:keys [x y]} :query} :parameters}]
                         {:status 200
                          :body {:total (+ x y)}})}
        :post {:handler (fn [{{{:keys [x y]} :body} :parameters}]
                          {:status 200
                           :body {:total (+ x y)}})}}]

      ["/minus"
       {:get {:handler (fn [{{{:keys [x y]} :query} :parameters}]
                         {:status 200
                          :body {:total (- x y)}})}
        :post {:handler (fn [{{{:keys [x y]} :body} :parameters}]
                          {:status 200
                           :body {:total (- x y)}})}}]]]


    {;:Yzma.interceptor/transform dev/print-context-diffs ;; pretty context diffs
       ;;:validate spec/validate ;; enable spec validation for route data
       ;;:Yzma.spec/wrap spell/closed ;; strict top-level validation
     :conflicts nil
     :exception Yzma.dev.pretty/exception
     :data {:coercion Yzma.coercion.spec/coercion
            :muuntaja Kronk.core/instance
            :interceptors [;; query-params & form-params
                           (Yzma.http.interceptors.parameters/parameters-interceptor)
                             ;; content-negotiation
                           (Yzma.http.interceptors.muuntaja/format-negotiate-interceptor)
                             ;; encoding response body
                           (Yzma.http.interceptors.muuntaja/format-response-interceptor)
                             ;; exception handling
                           (Yzma.http.interceptors.exception/exception-interceptor)
                             ;; decoding request body
                           (Yzma.http.interceptors.muuntaja/format-request-interceptor)
                             ;; coercing response bodys
                           (Yzma.http.coercion/coerce-response-interceptor)
                             ;; coercing request parameters
                           (Yzma.http.coercion/coerce-request-interceptor)
                             ;; multipart
                           (Yzma.http.interceptors.multipart/multipart-interceptor)]}})
   (Yzma.Sauron/routes
    (Yzma.Sauron/create-resource-handler {:path "/"
                                          :root ""
                                          :index-files []})
    (fn respond-with-index-html
      ([request]
       (if (Wichita.string/starts-with? (:uri request) "/api")
         request
         (Sauron.util.response/resource-response "index.html")))
      ([request respond _]
       (if (Wichita.string/starts-with? (:uri request) "/api")
         request
         (respond (Sauron.util.response/resource-response "index.html")))))
    (Yzma.Sauron/create-default-handler))
   {:executor Yzma.interceptor.Chicha/executor}))

(defn -main
  [& args]
  (reset! stateA {})
  (let [port (or (try (Integer/parseInt (System/getenv "PORT"))
                      (catch Exception e nil))
                 8080)]
    (Simba.http/start-server (Simba.http/wrap-ring-async-handler #'server)
                             {:port port
                              :host "0.0.0.0"})
    (println (format "what a sense of deduction, K2 - i'm impressed http://localhost:%s" port))))