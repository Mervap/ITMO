;(ns hw12.linear)
(load-file "src/hw12/exeption.clj")
;(load-file "exeption.clj")

;Vector
(defn vOperation [f]
  (fn [& vectors]
  {:pre [(and (checkVectorsWithMessage vectors)
              (equalsVectorsWithMessage vectors))]}
  (apply mapv f vectors)))

(def v+ (vOperation +))

(defn v- [& vectors]
  (vOperation - vectors))

(defn v* [& vectors]
  (vOperation * vectors))

(defn scalar [& vectors]
  {:post [(number? %)]}
  (apply + (vOperation * vectors)))

(defn v*s [vector & s]
  {:pre [(and (checkVectorsWithMessage (list vector))
              (checkScalar s))]}
  (mapv (fn [x] (* x (apply * s))) vector))

;Matrix
(defn mOperation [f matrixs]
  {:pre [(and (checkMatrixWithMessage matrixs)
              (equalsMatrix matrixs))]}
  (apply mapv f matrixs))

(defn m+ [& matrixs]
  (mOperation v+ matrixs))

(defn m- [& matrixs]
  (mOperation v- matrixs))

(defn m* [& matrixs]
  (mOperation v* matrixs))

(defn m*s*v_helper [matrix f & args]
  {:pre [(checkMatrixWithMessage (list matrix))]}
  (mapv (fn [x] (apply f x args)) matrix))

(defn m*s [matrix & s]
  {:pre [(checkScalar s)]}
  (apply m*s*v_helper matrix v*s s))

(defn m*v [matrix & v]
  {:pre [(and (checkVectorsWithMessage v)
              (equalsVectors v))]}
  (apply m*s*v_helper matrix scalar v))

(defn transpose [matrix]
  {:pre [(checkMatrixWithMessage (list matrix))]}
  (apply mapv vector matrix))

(defn coord [x y v1 v2]
  (- (* (nth x v1) (nth y v2))
     (* (nth y v1) (nth x v2))))

(defn vect [& vectors]
  {:pre [(and (and (checkVectorsWithMessage vectors)
                   (equalsVectorsWithMessage vectors))
              (checkVect vectors))]}
  (reduce (fn [x y]
            (vector (coord x y 1 2)
                    (coord x y 2 0)
                    (coord x y 0 1)))
          vectors))

(defn m*m [& matrixs]
  {:pre [(and (checkMatrixWithMessage matrixs)
              (checkMulMatrix matrixs))]}
  (reduce (fn [x y]
            (mapv (fn [z] (m*v (transpose y) z)) x))
          matrixs))

;Tensor
(defn shape [a]
  (if (number? a)
    ()
    (cons (count a) (shape (first a)))))

(defn broadcast [m newShape]
  (let [mshape (shape m)
        dims (count mshape)]
    (reduce
      (fn [m dup] (vec (repeat dup m))) m (reverse (drop-last dims newShape)))))

(defn best_tens [a b]
  {:pre [(or (every? true? (map == a (take-last (count a) (shape b))))
             (every? true? (map == (shape b) (take-last (count (shape b)) a))))]}
  (let [shape_b (shape b)]
    (if (> (count a) (count shape_b)) a shape_b)))

(defn max_shape [& tensor]
  (reduce best_tens (shape (first tensor)) tensor))

(defn operate
  ([f tensor]
   (if (number? (first tensor))
     (apply f tensor)
     (mapv (fn [x] (operate f x)) (apply map vector tensor))))
  )

(defn bOperation
  ([f & tensor]
   (let [shape (apply max_shape tensor)]
     (operate f (mapv (fn [x] (broadcast x shape)) tensor)))
    ))

(defn b+ [& tensor]
  (apply bOperation + tensor))

(defn b- [& tensor]
  (apply bOperation - tensor))

(defn b* [& tensor]
  (apply bOperation * tensor))

;(println (b+ [1 2] [3 4]))