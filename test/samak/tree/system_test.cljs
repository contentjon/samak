(ns samak.tree.system-test
  (:require [cljs.test         :as test
                               :refer-macros [is]]
            [clojure.zip       :as z]
            [devcards.core     :as dc
                               :refer-macros [defcard-rg deftest]]
            [revent.core       :as rv]
            [samak.tree.parser :as tp]
            [samak.tree.system :as ts]))

(defn spit-tree-test [tree]
  (-> tree
      tp/expr->tree
      ts/tree-zip
      z/down
      z/right
      ts/spit
      z/up
      z/node))

(deftest spit
  (is (= (spit-tree-test [:a [:b :c]])
         (tp/expr->tree [:a [:b] :c])))
  (is (= (spit-tree-test [:a [:b]])
         (tp/expr->tree [:a [] :b])))
  (is (= (spit-tree-test [:a []])
         (tp/expr->tree [:a []])))
  (is (= (spit-tree-test [:a :b :c])
         (tp/expr->tree [:a :b :c]))))

(defn slurp-tree-test [tree]
  (-> tree
      tp/expr->tree
      ts/tree-zip
      z/down
      z/right
      ts/slurp
      z/up
      z/node))

(deftest slurp
  (is (= (slurp-tree-test [:a [:b] :c])
         (tp/expr->tree [:a [:b :c]])))
  (is (= (slurp-tree-test [:a [] :b])
         (tp/expr->tree [:a [:b]])))
  (is (= (slurp-tree-test [:a []])
         (tp/expr->tree [:a []])))
  (is (= (slurp-tree-test [:a :b :c])
         (tp/expr->tree [:a :b :c]))))

(deftest treezip
  (is (= (-> {:children [{:a 1}]}
             ts/tree-zip
             z/down
             z/node)
         {:a 1})))

(deftest z-update
  (is (= (-> {:children [{:a 1}]}
             ts/tree-zip
             z/down
             (ts/z-update assoc :b 2)
             z/root)
         {:children [{:a 1 :b 2}]})))

(deftest root-of
  (let [sys (ts/make-tree-system {:children [{:a 1}]
                                  :type :list})
        root (rv/query sys :root)]
    (rv/send sys :down)
    (is (= @root
           {:children [{:a 1 :selected? true}]
            :type :list}))))
