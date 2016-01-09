(ns samak.tree.parser)

(defn expr->tree [expr]
  (if (coll? expr)
    {:type :list
     :children (mapv expr->tree expr)}
    {:type :symbol
     :label (str expr)}))
