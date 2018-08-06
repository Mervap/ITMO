(ns hw12.test)
;(use 'clojure.core.matrix)

(defn shape [a]
  (if (number? a)
    (list)
    (cons (count a) (shape (first a)))))

(defn broadcast [m target-shape]
    (let [mshape (shape m)
          dims (long (count mshape))
          tdims (long (count target-shape))]
        (reduce
          (fn [m dup] (vec (repeat dup m)))
          m
          (reverse (drop-last dims target-shape)))))

(defn broadcast-like [m a]
    (broadcast a (shape m)))


(println (broadcast-like [[1 2] [1 2] [1 2] [1 2]] [3 4]))