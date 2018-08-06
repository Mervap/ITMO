;(ns hw12.expression)

(defn operator [f]
  (fn [& op]
    (fn [vars] (apply f (map (fn [x] (x vars)) op)))))

(defn constant [x] (constantly x))
(defn variable [name] (fn [v] (v name)))

(def add (operator +))
(def subtract (operator -))
(def multiply (operator *))
(def divide (operator (fn [& x] (reduce (fn [x y] (/ (double x) (double y))) (first x) (rest x)))))
(def negate (operator -))

(def sinh (operator (fn [x] (Math/sinh x))))
(def cosh (operator (fn [x] (Math/cosh x))))

(def oper {'+      add,
           '-      subtract,
           '*      multiply,
           '/      divide,
           'negate negate,
           'sinh   sinh,
           'cosh   cosh})

(defn parse [expression]
  (cond
    (seq? expression) (apply (oper (first expression)) (map parse (rest expression)))
    (number? expression) (constant expression)
    (symbol? expression) (variable (str expression))))

(defn parseFunction [expression]
  (parse (read-string expression)))

;(println ((parseFunction "(/ 4 2 2)") {"x" 2}))