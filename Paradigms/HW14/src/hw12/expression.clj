;(ns hw12.expression)

(defn evaluate [e vars] ((.evaluate e) vars))
(defn toString [e] (.toStr e))
(defn diff [e var] ((.diff e) var))

;Interface
(definterface Operation
  (evaluate [])
  (toStr [])
  (diff []))

;Const
(declare ZERO)
(declare ONE)

(deftype Const [value]
  Operation
  (evaluate [this] (fn [vars] value))
  (toStr [this] (format "%.1f" (double value)))
  (diff [this] (fn [var] ZERO)))

(defn Constant [val] (Const. val))

(def ZERO (Constant 0))
(def ONE (Constant 1))

(deftype Var [name]
  Operation
  (evaluate [this] #(get % name))
  (toStr [this] name)
  (diff [this] #(if (= % name) ONE ZERO)))

(defn Variable [name] (Var. name))

;Operation
(deftype OperationPrototype [op symbol howDiff])

(deftype MakeOperation [prot args]
  Operation
  (evaluate [this] #(apply (.op prot) (map (fn [x] (evaluate x %)) args)))
  (toStr [this] (str "(" (.symbol prot) " " (clojure.string/join " " (map toString args)) ")"))
  (diff [this] #(apply (.howDiff prot) (concat args (map (fn [x] (diff x %)) args)))))

;Add
(declare Add)
(def AddProt (OperationPrototype.
               +
               "+"
               (fn [a b da db] (Add da db))))

(defn Add [& ar]
  (MakeOperation. AddProt ar))

;Subtract
(declare Subtract)
(def SubProt (OperationPrototype.
               -
               "-"
               (fn [a b da db] (Subtract da db))))

(defn Subtract [& ar]
  (MakeOperation. SubProt ar))

;Multiply
(declare Multiply)
(def MultiplyProt (OperationPrototype.
                    *
                    "*"
                    (fn [a b da db] (Add (Multiply da b) (Multiply a db)))))

(defn Multiply [& ar]
  (MakeOperation. MultiplyProt ar))

;Divide
(declare Divide)
(def DivideProt (OperationPrototype.
                  #(/ (double %1) (double %2))
                  "/"
                  (fn [a b da db] (Divide (Subtract (Multiply da b) (Multiply a db)) (Multiply b b)))))

(defn Divide [& ar]
  (MakeOperation. DivideProt ar))

;Negate
(declare Negate)
(def NegateProt (OperationPrototype.
                  -
                  "negate"
                  (fn [a da] (Negate da))))

(defn Negate [& ar]
  (MakeOperation. NegateProt ar))

;Sin/Cos
(declare Sin)
(declare Cos)

(def SinProt (OperationPrototype.
               #(Math/sin %)
               "sin"
               (fn [a da] (Multiply (Cos a) da))))

(def CosProt (OperationPrototype.
               #(Math/cos %)
               "cos"
               (fn [a da] (Negate (Multiply (Sin a) da)))))

(defn Sin [& ar]
  (MakeOperation. SinProt ar))

(defn Cos [& ar]
  (MakeOperation. CosProt ar))

;Parser
(def ops {"+"      Add,
          "-"      Subtract,
          "*"      Multiply,
          "/"      Divide,
          "negate" Negate,
          "sin"    Sin,
          "cos"    Cos})

(def vars {'x (Variable "x"),
           'y (Variable "y"),
           'z (Variable "z")})

;Object
(defn parse [expression]
  (cond
    (symbol? expression) (get vars expression)
    (number? expression) (Constant expression)
    :else (let [op (first expression) args (rest expression)] (apply (get ops (str op)) (map parse args)))))

(defn parseObject [expression]
  (parse (read-string expression)))

;InfixObject
(defn parseToTokens [expression]
  (let [isNum? #(and % (re-find #"^\d+$" %))
        isWord? #(and % (re-find #"^[a-zA-Z]+$" %))]
    (reverse
      (reduce
        (fn [[first & rest :as tokens] token]
          (cond
            (or (and (isWord? token) (isWord? first))
                (and (isNum? token) (isNum? first))) (cons (str first token) rest)
            :else (cons token tokens)))
        '(),
        (#(clojure.string/split (clojure.string/replace % " " "") #"") expression)))))

(def priority {"+"      0,
               "-"      0,
               "*"      1,
               "/"      1,
               "sin"    2,
               "cos"    2,
               "negate" 3})

(def tp #{(type (Constant 0)) (type (get vars "x"))})

(defn shuntingYard [tokens]
  (drop-last 1
             (flatten
               (reduce
                 (fn [[result stack lastToken] token]
                   (let [less? #(and (contains? priority %) (<= (priority token) (priority %)))
                         lessNegate? #(and (contains? priority %) (< (priority "negate") (priority %)))
                         notOpenBracket? #(not= "(" %) ]
                     (cond
                       (= token "(") [result (cons token stack) token]
                       (= token ")") [(conj result (take-while notOpenBracket? stack)) (rest (drop-while notOpenBracket? stack)) token]
                       (and (= token "-") (not (#(or (contains? tp (type %)) (= ")" %)) lastToken))) [(conj result (take-while lessNegate? stack)) (cons "negate" (drop-while lessNegate? stack)) "negate"]
                       (contains? priority token) [(conj result (take-while less? stack)) (cons token (drop-while less? stack)) token]
                       :else [(conj result token) stack token])))
                 [[] () ""]
                 tokens))))

(def unary #{"sin" "cos" "negate"})

(defn parsePol [tokens]
  (first
    (reduce
      (fn [stack token]
        (if (contains? ops token)
          (let [op (get ops token)]
            (if (contains? unary token)
              (cons (op (first stack)) (drop 1 stack))
              (cons (op (second stack) (first stack)) (drop 2 stack))))
          (cons token stack)))
      [] tokens)))


(defn parseObjectInfix [expression]
  (parsePol (map #(cond
                    (number? (read-string %)) (Constant (read-string %))
                    (contains? ops %) %
                    :else (get vars (read-string %)))
                 (shuntingYard (parseToTokens expression)))))

(def a (parseObjectInfix "sin(-x) * sin(- x) + cos(--x) * cos(--x)"))
(println (toString a))
(println (evaluate a {"x" (/ 3.1415 4)}))
