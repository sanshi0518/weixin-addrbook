(ns addrbook.handler
  (:use [clojure.string :only [blank? replace]]
        [addrbook.address :only [addr-book]])
  (:import (org.dom4j Document DocumentHelper Node)))

(defmacro defhandler
  [name args & body]
  `(defn ~name [req#]
     (let [{:keys ~args :or {~'req req#}} (:params req#)]
       ~@body)))

(defonce err-bad-request "请求错误")
(defonce err-no-result "此人已被查水表，换个小伙伴试试吧")
(defonce err-nedd-more-info "查询姓名过短，请补充完整（至少2位）")

(defonce response-template
  (str "<xml>"
       "<ToUserName><![CDATA[userto]]></ToUserName>"
       "<FromUserName><![CDATA[userfrom]]></FromUserName>"
       "<CreateTime>timestamp</CreateTime>"
       "<MsgType><![CDATA[text]]></MsgType>"
       "<Content><![CDATA[response]]></Content>"
       "<FuncFlag>0</FuncFlag>"
       "</xml>"))

(defn get-node-value [doc node-name]
  (-> doc (.selectSingleNode node-name) (.getStringValue)))

(defn parse-body [body]
  (if (blank? body)
    nil
    (let [doc (DocumentHelper/parseText body)
          toUserName (get-node-value doc "//ToUserName")
          fromUserName (get-node-value doc "//FromUserName")
          msgType (get-node-value doc "//MsgType")
          content (get-node-value doc "//Content")]
      {:ToUserName toUserName :FromUserName fromUserName :MsgType msgType :Content content})))

(defn query [body-map]
  (let [response-str (-> response-template (replace "userto" (:FromUserName body-map))
                       (replace "userfrom" (:ToUserName body-map))
                       (replace "timestamp" (str (System/currentTimeMillis))))
        name (:Content body-map)]
    (if (or (blank? name)
          (< (.length name) 2))
      (replace response-str "response" err-nedd-more-info)
      ;;not handle the same names case
      ;;(if-let [addr (some #(when (or (.startsWith (.toUpperCase (:name %)) (.toUpperCase name))
      ;;                             (.startsWith (.toUpperCase (:name-pinyin addr)) (.toUpperCase name))) %) @addr-book)]
      ;;  (replace response-str "response" (str (:name addr) " " (:phone addr) "(" (:mail addr) ")\r\n"))
      ;;  (replace response-str "response" err-no-result))
      (let [found (StringBuilder.)]
        (doseq [addr @addr-book]
          (if (or (.startsWith (.toUpperCase (:name addr)) (.toUpperCase name))
                (.startsWith (.toUpperCase (:name-pinyin addr)) (.toUpperCase name)))
            (.append found
              (str (if-not (.isEmpty (.toString found)) "\r\n") (:name addr) " " (:phone addr) "(" (:mail addr) ")"))))
        (if (.isEmpty (.toString found))
          (replace response-str "response" err-no-result)
          (replace response-str "response" (.toString found)))))))

(defhandler check-signature [echostr]
  "no check for signature, just return echostr directly"
  echostr)

(defhandler service [req]
  "handle the query calls"
  (let [body (slurp (:body req))
        body-map (parse-body body)]
    (if (nil? body-map)
      err-bad-request
      (query body-map))))

(defn -main [& args]
  (let [body-map {:ToUserName "to" :FromUserName "from" :MsgType "usedefault" :Content "hjb"}
        ret (query body-map)]
    (println ret)))
