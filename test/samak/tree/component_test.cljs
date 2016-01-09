(ns samak.tree.component-test
  (:require
   [devcards.core :as dc :refer-macros [defcard-rg deftest]]
   [reagent.core :as re]
   [revent.core :as rv]
   [samak.tree.component :as tc]
   [samak.tree.parser :as tp]
   [samak.tree.system :as ts]))

(def example-symbol
  (tp/expr->tree 'foo))

(defcard-rg symbol
  (tc/node example-symbol))

(defcard-rg selected-symbol
  (tc/node (assoc example-symbol :selected? true)))

(def example-tree
  (tp/expr->tree '(foo bar (baq bav baz))))

(defcard-rg tree
  (tc/node example-tree))

(defcard-rg selected-list
  (tc/node (assoc example-tree :selected? true)))

(defcard-rg selected-sub-list
  (tc/node (assoc-in example-tree [:children 2 :selected?]
                     true)))

(defcard-rg selected-sub-symbol
  (tc/node (assoc-in example-tree [:children 2 :children 1 :selected?]
                     true)))

(defn example-tree-system []
  (tc/tree (ts/make-tree-system example-tree)))

(defcard-rg tree-system
  [example-tree-system])
