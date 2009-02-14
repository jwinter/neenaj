(ns neenaj.blog 
  (:use (clojure.contrib sql)
        (compojure html))
  (:require [neenaj.db_conf])) ;;(def conn_string "host/db_name?user=someone&password=somepass")

(def db {:classname   "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname     db_conf/conn_string})

(defn fetch-entries []
  (with-connection db 
    (with-query-results results ["select * from wp_posts order by id desc limit 8"] 
                        (vec results))))

(defn display-entry [entry]
  (println entry)
  [:li
   [:h1 (entry :post_title)]
   [:p (entry :post_content)]])

(defn display-entries [entries-vec]
  (html [:html 
         [:head 
          [:title (str ((nth entries-vec 0 "") :post_title) " << Latest Entries from jwinter.org")]
          [:link {:rel "stylesheet" :type "text/css" :href "/static/neenaj.css"}]
          ]
         [:h1 "Joe's Blog Runs on Clojure"]
         [:body
          [:ol (map display-entry entries-vec)]
           ]]))
          

(defn display-about []
  "I'm an about page")


