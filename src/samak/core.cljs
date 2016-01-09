(ns samak.core
  (:require
   [reagent.core :as reagent]
   [samak.tree.system :as ts]
   [samak.tree.component :as tc]
   [samak.tree.parser :as tp]
   [revent.core :as rv]))

(enable-console-print!)

(def *system*
  (-> '(a b (c d (e f (g)) h (i ()) j k))
      tp/expr->tree
      ts/make-tree-system))

(defn app []
  [:div.container
   [:h1 "Samak"]
   [:p "Check the "
    [:a {:href "cards.html"}
     "devcards"]
    " for more examples."]
   [tc/tree *system*]])

(defn main []
  (when-let [node (.getElementById js/document "main-app-area")]
    (reagent/render app node)))

(main)
