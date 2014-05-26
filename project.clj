(defproject weixin-addrbook "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://www.github.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [compojure "1.1.6"]
                 [dom4j/dom4j "1.6.1" :exclusions [xml-apis/xml-apis]]
                 [jaxen/jaxen "1.1.4"]
                 [pinyin4j/pinyin4j "2.5.0"]]
  :source-paths ["src/main/clojure"]
  :resource-paths ["src/main/resources"]
  :test-paths ["src/test/clojure"]
  :global-vars {*warn-on-reflection* true}
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler addrbook.routes/app}
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]]
                   :resource-paths ["src/test/resources"]}})
