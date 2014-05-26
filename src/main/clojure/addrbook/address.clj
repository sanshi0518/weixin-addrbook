(ns addrbook.address
  (:use [clojure.java.io]
        [clojure.string :only [split]])
  (:import (java.net.URL)
           (net.sourceforge.pinyin4j PinyinHelper)
           (net.sourceforge.pinyin4j.format HanyuPinyinCaseType HanyuPinyinOutputFormat HanyuPinyinToneType HanyuPinyinVCharType)))

(defrecord addr-obj [name phone mail name-pinyin])

(def addr-book (atom '()))

(def pinyin-format (HanyuPinyinOutputFormat.))

(defn name2pinyin [name]
  (let [name-pinyin (StringBuilder.)
        name-char-array (.toCharArray name)]
    (doseq [c name-char-array]
      (let [pinyin-vector (PinyinHelper/toHanyuPinyinStringArray c pinyin-format)]
        (if (and (not (nil? pinyin-vector))
              (> (count pinyin-vector) 0))
          (.append name-pinyin (-> (get pinyin-vector 0) (.charAt 0))))))
    (.toString name-pinyin)))

(defn load-addr []
  (let [dir (file (.getFile (resource "address")))]
    (doseq [file (.listFiles dir)]
      (when-not (.isDirectory file)
        (with-open [rdr (reader file :encoding "GBK")]
          (doseq [line (line-seq rdr)]
            (let [lv (split line #",")
                  addr (list (addr-obj. (lv 0) (lv 1) (lv 2) (name2pinyin (lv 0))))]
              (swap! addr-book concat addr))))))))

(load-addr)

(defn -main [& args]
  (println (count @addr-book))
  (println @addr-book))