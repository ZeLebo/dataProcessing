(ns lab3)

; implement a parallel variant of filter using futures. Each future must process a block of elements. The number of futures must be equal to the number of cores of the machine.
; The function must return a lazy sequence of the elements that satisfy the predicate.

;(defn filter-parallel [pred coll]
;  (let [cores (Runtime/getRuntime availableProcessors)
;        size (count coll)
;        block (quot size cores)
;        futures (map (fn [i] (future (filter pred (subvec coll (* i block) (+ (* i block) block))))) (range cores))
;        results (map deref futures)]
;    (apply concat results)))

(defn pfilter [pred coll]
  (let [n (. (Runtime/getRuntime) availableProcessors)
        size (count coll)
        block (quot size n)
        futures (map (fn [i] (future (filter pred (subvec coll (* i block) (+ (* i block) block))))) (range n))
        results (map deref futures)]
    (apply concat results)))

; test the function
(defn heavy-even? [n]
  (Thread/sleep 100)
  (even? n))
