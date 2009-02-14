(ns neenaj
  (:require [compojure.http.servlet :as servlet])
  (:require [compojure.http.routes :as routes])
  (:require [compojure.server.jetty :as jetty])
  (:import java.io.File)
  (:use [compojure.http.helpers])
  (:use [neenaj.blog]))

(def static-dir "/home/jwinter/static/")

(defn canonical-path [path]
  (.getCanonicalPath (new File path)))

(defn css-js-or-404 [path]
  "Serves the canonical path if the file is a .css or .js file in static, otherwise 404"
  (let [canon_path (canonical-path (str static-dir "/" path))]
    (if (.startsWith canon_path static-dir)
      (serve-file static-dir path)
      [404 "Not found, man"]
      )))

(servlet/defservlet home
  "Blog homepage"
  (routes/GET "/log/*" (display-entries (fetch-entries)))
  (routes/GET "/resume/*" (serve-file static-dir "resume.pdf"))
  (routes/GET "/buh-bounce/*" (display-entries (fetch-entries)))
  (routes/GET "/static/*"  (css-js-or-404 (route :*)))
  (routes/GET "/" (serve-file static-dir "index.html"))
  )

(jetty/defserver neenaj-server
  {:port 80}
  "/*" home)

(jetty/start neenaj-server)

