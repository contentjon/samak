(ns samak.tree.parser-test
  (:require
   [cljs.test :refer-macros [is]]
   [devcards.core :as dc :refer-macros [defcard-rg deftest]]
   [samak.tree.parser :as tp]))

(deftest symbols
  (is (= (tp/expr->tree 'foo)
         {:type :symbol
          :label "foo"}))
  (is (= (tp/expr->tree "foo")
         {:type :symbol
          :label "foo"}))
  (is (= (tp/expr->tree :foo)
         {:type :symbol
          :label ":foo"}))
  (is (= (tp/expr->tree 42)
         {:type :symbol
          :label "42"})))

(deftest lists
  (is (= (tp/expr->tree [])
         {:type :list
          :children []}))
  (is (= (tp/expr->tree ["a" 1])
         {:type :list
          :children [{:type :symbol
                      :label "a"}
                     {:type :symbol
                      :label "1"}]}))
  (is (= (tp/expr->tree '(a b (c d) e (f (g))))
         {:type :list
          :children [{:type :symbol
                      :label "a"}
                     {:type :symbol
                      :label "b"}
                     {:type :list
                      :children [{:type :symbol
                                  :label "c"}
                                 {:type :symbol
                                  :label "d"}]}
                     {:type :symbol
                      :label "e"}
                     {:type :list
                      :children [{:type :symbol
                                  :label "f"}
                                 {:type :list
                                  :children [{:type :symbol
                                              :label "g"}]}]}]})))

(deftest children-are-assocable
  (let [t1 (tp/expr->tree '(a b c))
        t2 (tp/expr->tree '(a X c))
        t3 (assoc-in t1 [:children 1 :label]
                     "X")]
    (is (= t2 t3))))
